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
    val elements = Buffer[Int]()

    for(line <- lines){
      val data = line.split(" ")
      data(0) match{
        case "v" => vertices += parseVertexPosition(data(1),data(2),data(3))
        case "vt" => texCoords += parseTextureCoordinate(data(1),data(2))
        case "vn" => normals += parseVertexNormal(data(1),data(2),data(3))
        case "f" => {
          val verts = Vector.tabulate(3)[Array[String]](n => data(n+1).split("/"))
          val vert1_data = data(1).split("/")
          val vert2_data = data(2).split("/")
          val vert3_data = data(3).split("/")

          val vert1 = new VertexData(vert1_data(0).toInt - 1,vert1_data(1).toInt - 1,vert1_data(2).toInt - 1)
          val vert2 = new VertexData(vert2_data(0).toInt - 1,vert2_data(1).toInt - 1,vert2_data(2).toInt - 1)
          val vert3 = new VertexData(vert3_data(0).toInt - 1,vert3_data(1).toInt - 1,vert3_data(2).toInt - 1)

          var indexOfvert1 = vertedDataList.indexOf(vert1)
          if(indexOfvert1 < 0){
            vertedDataList += vert1
            indexOfvert1 = vertedDataList.length - 1
          }

          var indexOfvert2 = vertedDataList.indexOf(vert2)
          if(indexOfvert2 < 0){
            vertedDataList += vert2
            indexOfvert2 = vertedDataList.length - 1
          }

          var indexOfvert3 = vertedDataList.indexOf(vert3)
          if(indexOfvert3 < 0){
            vertedDataList += vert3
            indexOfvert3 = vertedDataList.length - 1
          }

          elements += indexOfvert1
          elements += indexOfvert2
          elements += indexOfvert3
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
           normals:Vector[Vector3f],    vertexData: Vector[VertexData], elements: Vector[Int]) {

  private val rawDataLenght = Mesh.VERTEX_DATA_LENGTH * vertexData.length +
                              Mesh.NORMAL_DATA_LENGTH * vertexData.length +
                              Mesh.TEX_DATA_LENGTH * vertexData.length

  private val rawData: Array[Float] = getRawData
  Log.debug("rawDataSize Check", "Should be " + rawDataLenght + ", is " + rawData.length)
  Log.debug("VertexData size", "" + vertexData.length)
  var s = ""
  vertexData.foreach(v => s += v.toString() + "\n")
  Log.debug("VData", s)

  private def getRawData(): Array[Float] = {
    val data = Buffer[Float]()
    for(i <- vertexData.indices){ data ++= getRawDataFromData(i) }
    data.toArray
  }

  private def getRawDataFromData(i: Int): Vector[Float] = {
    val vertIndex = vertexData(i).positionID
    val uvIndex = vertexData(i).uvID
    val normIndex = vertexData(i).normalID

    Vector[Float](
    vertices(vertIndex).x,vertices(vertIndex).y,vertices(vertIndex).z,
    texCoords(uvIndex).x,texCoords(uvIndex).y,
    normals(normIndex).x,normals(normIndex).y,normals(normIndex).z)
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
class Face(val vertices: Vector[Vertex])