package tilde.util

import java.io.File
import java.text.ParseException

import scala.collection.mutable.Buffer
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import org.lwjgl.util.vector.{Vector2f, Vector3f}
import tilde.graphics.Mesh
import tilde.log.Log

/**
 * Created by Toni on 8.1.2015.
 */
object ResourceUtil {
  val FLOAT_SIZE = 4

  val VERTEX_DATA_LENGTH = 3
  val NORMAL_DATA_LENGTH = 3
  val UV_DATA_LENGTH = 2
  val COLOR_DATA_LENGTH = 3

  val DATA_OFFSET = VERTEX_DATA_LENGTH + NORMAL_DATA_LENGTH + UV_DATA_LENGTH + COLOR_DATA_LENGTH

  val VERTEX_DATA_STRIDE = 0
  val NORMAL_DATA_STRIDE = VERTEX_DATA_LENGTH
  val UV_DATA_STRIDE = VERTEX_DATA_LENGTH + NORMAL_DATA_LENGTH
  val COLOR_DATA_STRIDE = VERTEX_DATA_LENGTH + NORMAL_DATA_LENGTH + UV_DATA_LENGTH
  
  def loadMesh(path: String): Mesh = {
    def parseVertexPosition(str: String*): Vector3f = new Vector3f(str(0).toFloat,str(1).toFloat,str(2).toFloat)
    def parseTextureCoordinate(str: String*): Vector2f = {new Vector2f(str(0).toFloat,1 - str(1).toFloat)} // Why 1 - str(1)?
    def parseVertexNormal(str: String*): Vector3f = {new Vector3f(str(0).toFloat,str(1).toFloat,str(2).toFloat)}

    Log.debug("Loading mesh", path)
    val lines = scala.io.Source.fromFile(new File(path)).getLines()

    val vertices = Buffer[Vector3f]()
    val texCoords = Buffer[Vector2f]()
    val normals = Buffer[Vector3f]()

    val vertexData = Buffer[VertexData]()
    val elements = Buffer[Int]()

    for(line <- lines){
      val data = line.split(" ")
      data(0) match {
        case "v" => vertices += parseVertexPosition(data(1), data(2), data(3))
        case "vt" => texCoords += parseTextureCoordinate(data(2), data(1))
        case "vn" => normals += parseVertexNormal(data(1), data(2), data(3))
        case "f" => {
          val verts = Vector.tabulate(3)(n => data(n + 1).split("/"))
          for (v <- verts) {
            val vertData = new VertexData(v(0).toInt - 1, v(1).toInt - 1, v(2).toInt - 1)
            // Comment out if you want to minimize duplicate vertices and memory usage
            //var indexOfvertData = vertedDataList.indexWhere(v => v == vertData)
            var indexOfvertData:Int = 0
            //if (indexOfvertData < 0) {
            vertexData += vertData
            //indexOfvertData = vertexData.length - 1
            //}

            elements += (vertexData.length - 1)
          }
        }

        case _ => {}
      }
    }
    //Log.debug("Model had","" + vertices.length + " unique verts")
    //Log.debug("Model had","" + elements.length + " elements")
    //Log.debug("Model had","" + vertexData.length + " vertexData")

    // Loading mesh to memory
    val data = Buffer[Float]()
    for(i <- vertexData.indices){
      val vertIndex = vertexData(i).positionID
      val uvIndex = vertexData(i).uvID
      val normIndex = vertexData(i).normalID
      data ++= Vector[Float](
        vertices(vertIndex).x, vertices(vertIndex).y, vertices(vertIndex).z,
        texCoords(uvIndex).x, texCoords(uvIndex).y,
        normals(normIndex).x, normals(normIndex).y, normals(normIndex).z)
    }

    val dataBuffer = BufferUtils.createFloatBuffer(data.length)
    dataBuffer.put(data.toArray)
    dataBuffer.rewind()

    val dataID = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER,dataID)
    glBufferData(GL_ARRAY_BUFFER,dataBuffer,GL_STATIC_DRAW)
    glBindBuffer(GL_ARRAY_BUFFER, 0)

    val elemBuffer = BufferUtils.createIntBuffer(elements.length)
    elemBuffer.put(elements.toArray)
    elemBuffer.rewind()

    val vaoID = glGenVertexArrays()
    glBindVertexArray(vaoID)

    //data
    val vertsID = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER,vertsID)
    glBufferData(GL_ARRAY_BUFFER,dataBuffer,GL_STATIC_DRAW)
    glVertexAttribPointer(0, 3, GL_FLOAT, false, 8*FLOAT_SIZE, 0*FLOAT_SIZE) // verts
    glVertexAttribPointer(1, 2, GL_FLOAT, false, 8*FLOAT_SIZE, 3*FLOAT_SIZE) // uv
    glVertexAttribPointer(2, 3, GL_FLOAT, false, 8*FLOAT_SIZE, 5*FLOAT_SIZE) // normal
    //glBindBuffer(GL_ARRAY_BUFFER, 0)

    //elem
    val elemID = glGenBuffers()
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,elemID)
    glBufferData(GL_ELEMENT_ARRAY_BUFFER,elemBuffer,GL_STATIC_DRAW)
    //glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)

    glBindVertexArray(0)

    new Mesh(vaoID,dataID,elemID, elements.length, data.length)
  }

  def loadPly(path: String): Mesh = {
    def intColorToFloat(i: Float): Float = (i / 255)

    val lines = scala.io.Source.fromFile(new File(path)).getLines().toVector

    val vertCount = lines.find(a => { // Finds line "element count X", where X is vertex count
      val splits = a.split(" ")
      splits(0) == "element" && splits(1) == "vertex"
    }).get.split(" ")(2).toInt

    val faceCount = lines.find(a => {// Finds line "element face X", where X is face count
      val splits = a.split(" ")
      splits(0) == "element" && splits(1) == "face"
    }).get.split(" ")(2).toInt

    val vertexDataBufferLength = DATA_OFFSET * vertCount
    val elementDataBufferLength = faceCount * 3 // *3 because 3 vertices per face

    val vertexDataBuffer = BufferUtils.createFloatBuffer(vertexDataBufferLength)
    val elementDataBuffer = BufferUtils.createIntBuffer(elementDataBufferLength)

    val indexOfFirstVert = lines.indexOf("end_header") + 1
    val indexOfFirstElem = indexOfFirstVert+vertCount

    // Vertex
    for(i <- indexOfFirstVert until indexOfFirstVert + vertCount){
      val line = lines(i)
      val parts = line.split(" ")
      if(parts.length != 11){
        Log.error("Unsupported vertex data with vertex " + i, parts.length + " data segments found, should be " + DATA_OFFSET)
      }

     //parts.map(d => d.toFloat)//.foreach(d => vertexDataBuffer.put(d))
      for(f <- parts.indices){
        if(f > 7) {
          val a = intColorToFloat(parts(f).toFloat)
          //Log.debug("Color","" + parts(f) + " = " + a)
          vertexDataBuffer.put(a)
        } // For colors}
        else  vertexDataBuffer.put(parts(f).toFloat)
      }
    }
    // Faces
    for(i <- indexOfFirstElem until indexOfFirstElem + faceCount){
      val line = lines(i)
      val parts = line.split(" ")

      if(parts(0) != "3"){
        Log.error("Unsupported vertex count", "Faces needs to have 3 vertices")
      }

      parts.tail.foreach(d => elementDataBuffer.put(d.toInt)) // First section is just for telling the vert count
    }
    vertexDataBuffer.rewind()
    elementDataBuffer.rewind()
    val vaoID = glGenVertexArrays()
    glBindVertexArray(vaoID)

    val vertexDataID = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER,vertexDataID)
    glBufferData(GL_ARRAY_BUFFER,vertexDataBuffer,GL_STATIC_DRAW)
    glVertexAttribPointer(0, 3, GL_FLOAT, false, DATA_OFFSET * FLOAT_SIZE, VERTEX_DATA_STRIDE * FLOAT_SIZE) // verts
    glVertexAttribPointer(1, 3, GL_FLOAT, false, DATA_OFFSET * FLOAT_SIZE, NORMAL_DATA_STRIDE * FLOAT_SIZE) // normal
    glVertexAttribPointer(2, 2, GL_FLOAT, false, DATA_OFFSET * FLOAT_SIZE, UV_DATA_STRIDE * FLOAT_SIZE) // uv
    glVertexAttribPointer(3, 3, GL_FLOAT, false, DATA_OFFSET * FLOAT_SIZE, COLOR_DATA_STRIDE * FLOAT_SIZE) // color
    glBindBuffer(GL_ARRAY_BUFFER,0)

    val elementID = glGenBuffers()
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,elementID)
    glBufferData(GL_ELEMENT_ARRAY_BUFFER,elementDataBuffer,GL_STATIC_DRAW)
    //glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0)

    glBindVertexArray(0)

    new Mesh(vaoID,vertexDataID,elementID,elementDataBufferLength,vertexDataBufferLength)
  }
}

class VertexData(val positionID: Int, val uvID: Int, val normalID:Int) {

  def ==(other: VertexData): Boolean = this.positionID == other.positionID &&
    this.uvID == other.uvID && this.normalID == other.normalID
  def != (other: VertexData): Boolean = !(this == other)

  override def toString() = {
    (positionID+1) + "/" + (uvID+1) + "/" + (normalID+1)
  }
}