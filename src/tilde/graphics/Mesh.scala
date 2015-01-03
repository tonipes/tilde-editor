package tilde.graphics

import java.io.File
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import org.lwjgl.util.vector.{Vector2f, Vector3f}
import tilde.log.Log

import scala.collection.mutable.Buffer

/**
 * Created by Toni on 22.12.14.
 */
object Mesh{
  val VERTEX_DATA_LENGTH = 3
  val TEX_DATA_LENGTH = 2
  val NORMAL_DATA_LENGTH = 3

  def load(path: String) = {
    Log.debug("Loading mesh", path)
    val lines = scala.io.Source.fromFile(new File(path)).getLines()

    val vertices = Buffer[Vector3f]()
    val texCoords = Buffer[Vector2f]()
    val normals = Buffer[Vector3f]()

    val vertexData = Buffer[VertexData]()
    val elements = Buffer[Short]()

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

              elements += (vertexData.length - 1).toShort
            }
          }

          case _ => {}
        }
    }
    Log.debug("Model had","" + vertices.length + " unique verts")
    Log.debug("Model had","" + elements.length + " elements")
    Log.debug("Model had","" + vertexData.length + " vertexData")
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

    val elemBuffer = BufferUtils.createShortBuffer(elements.length)
    elemBuffer.put(elements.toArray)
    elemBuffer.rewind()

    val vaoID = glGenVertexArrays()
    glBindVertexArray(vaoID)

    //data
    val vertsID = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER,vertsID)
    glBufferData(GL_ARRAY_BUFFER,dataBuffer,GL_STATIC_DRAW)
    glVertexAttribPointer(0, 3, GL_FLOAT, false, 8*4, 0*4) // verts
    glVertexAttribPointer(1, 2, GL_FLOAT, false, 8*4, 3*4) // uv
    glVertexAttribPointer(2, 3, GL_FLOAT, false, 8*4, 5*4) // normal
    //glBindBuffer(GL_ARRAY_BUFFER, 0)

    //elem
    val elemID = glGenBuffers()
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,elemID)
    glBufferData(GL_ELEMENT_ARRAY_BUFFER,elemBuffer,GL_STATIC_DRAW)
    //glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)

    glBindVertexArray(0)

    new Mesh(vaoID,dataID,elemID, data.length, elements.length)
  }

  private def parseVertexPosition(str: String*): Vector3f = new Vector3f(str(0).toFloat,str(1).toFloat,str(2).toFloat)
  private def parseTextureCoordinate(str: String*): Vector2f = {new Vector2f(str(0).toFloat,1 - str(1).toFloat)} // Why 1 - str(1)?
  private def parseVertexNormal(str: String*): Vector3f = {new Vector3f(str(0).toFloat,str(1).toFloat,str(2).toFloat)}
}

class Mesh(val vaoID:Int, val dataId: Int, val elemId: Int , val elemCount: Int, val dataCount: Int){

  def bindVAO(): Unit = glBindVertexArray(vaoID)

  def unbindVAO(): Unit = glBindVertexArray(0)

  def bindData(): Unit = glBindBuffer(GL_ARRAY_BUFFER,dataId)

  def bindElem(): Unit = glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,elemId)

  def bufferData(): Unit = glBufferData(GL_ARRAY_BUFFER,dataId,GL_STATIC_DRAW)

  def bufferElem(): Unit = glBufferData(GL_ELEMENT_ARRAY_BUFFER,elemId,GL_STATIC_DRAW)

  def unbind(): Unit = glBindBuffer(GL_ARRAY_BUFFER,0)

}

class VertexData(val positionID: Int, val uvID: Int, val normalID:Int) {

  def ==(other: VertexData): Boolean = this.positionID == other.positionID &&
    this.uvID == other.uvID && this.normalID == other.normalID
  def != (other: VertexData): Boolean = !(this == other)

  override def toString() = {
    (positionID+1) + "/" + (uvID+1) + "/" + (normalID+1)
  }
}