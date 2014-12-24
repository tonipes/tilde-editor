package tilde.graphics

import java.io.File
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

    val vertedDataList = Buffer[VertexData]()
    val elements = Buffer[Short]()

    for(line <- lines){
      val data = line.split(" ")
        data(0) match {
          case "v" => vertices += parseVertexPosition(data(1), data(2), data(3))
          case "vt" => texCoords += parseTextureCoordinate(data(1), data(2))
          case "vn" => normals += parseVertexNormal(data(1), data(2), data(3))
          case "f" => {
            val verts = Vector.tabulate(3)(n => data(n + 1).split("/"))
            for (v <- verts) {
              val vertData = new VertexData(v(0).toInt - 1, v(1).toInt - 1, v(2).toInt - 1)

              //var indexOfvertData = vertedDataList.indexWhere(v => v == vertData)
              var indexOfvertData = 0
              //if (indexOfvertData < 0) {
                vertedDataList += vertData
                indexOfvertData = vertedDataList.length - 1
              //}

              elements += indexOfvertData.toShort
            }
          }

          case _ => {}
        }
    }
    new Mesh(vertices.toVector,texCoords.toVector,normals.toVector,vertedDataList.toVector,elements.toVector)
  }

  private def parseVertexPosition(str: String*): Vector3f = new Vector3f(str(0).toFloat,str(1).toFloat,str(2).toFloat)
  private def parseTextureCoordinate(str: String*): Vector2f = {new Vector2f(str(0).toFloat,str(1).toFloat)}
  private def parseVertexNormal(str: String*): Vector3f = {new Vector3f(str(0).toFloat,str(1).toFloat,str(2).toFloat)}
}

class Mesh(vertices: Vector[Vector3f],  texCoords: Vector[Vector2f],
           normals:Vector[Vector3f],    vertexData: Vector[VertexData], private val elements: Vector[Short]) {

  private val rawDataLenght = Mesh.VERTEX_DATA_LENGTH * vertexData.length +
                              Mesh.NORMAL_DATA_LENGTH * vertexData.length +
                              Mesh.TEX_DATA_LENGTH * vertexData.length

  private val rawData: Array[Float] = createRawData
  def getTriangleCount = elements.length / 3
  def getElemCount = elements.length
  def getRawData = rawData
  def getElements = elements
  //Log.debug("rawDataSize Check", "Should be " + rawDataLenght + ", is " + rawData.length)
  //Log.debug("VertexData size", "" + vertexData.length)

  //var s = ""
  //vertexData.foreach(v => s += v.toString() + "\n")
  //Log.debug("VData", s)

  private def createRawData(): Array[Float] = {
    val data = Buffer[Float]()
    for(i <- vertexData.indices){ data ++= getRawDataFromData(i) }
    data.toArray
  }

  private def getRawDataFromData(i: Int): Vector[Float] = {
    val vertIndex = vertexData(i).positionID
    val uvIndex = vertexData(i).uvID
    val normIndex = vertexData(i).normalID

    Vector[Float](
    vertices(vertIndex).x, vertices(vertIndex).y, vertices(vertIndex).z,
    texCoords(uvIndex).x, texCoords(uvIndex).y,
    normals(normIndex).x, normals(normIndex).y, normals(normIndex).z)
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

// Holds all of vertex data
class Vertex(position: Vector3f, uv: Vector2f,normal: Vector3f){
  override def toString() = {
    "" + position.x + ", " + position.y + ", " + position.z + " : " +
      uv.x + ", " + uv.y + " : " + normal.x + ", " + normal.y + ", " + normal.z
  }
}