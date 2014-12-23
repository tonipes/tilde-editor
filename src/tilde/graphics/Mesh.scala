package tilde.graphics

import java.io.File
import org.lwjgl.util.vector.{Vector2f, Vector3f}
import tilde.log.Log

import scala.collection.mutable.Buffer

/**
 * Created by Toni on 22.12.14.
 */
object Mesh{
  def load(path: String) = {
    val lines = scala.io.Source.fromFile(new File(path)).getLines()

    val vertices = Buffer[Vector3f]()
    val faces = Buffer[Vector3f]()
    val uvCoords = Buffer[Vector2f]()
    val vertexNormals = Buffer[Vector3f]()

    for(line <- lines){
      val data = line.split(" ")
      data(0) match{
        case "v" => vertices += parseVertexLine(data(1),data(2),data(3))

        case "vt" => uvCoords += parseTextureCoordinateLine(data(1),data(2))
        case "vn" => vertexNormals += parseVertexNormalLine(data(1),data(2),data(3))
        case "f" => faces += parseFaceLine(data(1),data(2),data(3))
        case _ => {}
      }
    }
    vertices.foreach(f => println("vert: " + f))
  }

  private def parseVertexLine(str: String*): Vector3f = new Vector3f(str(0).toFloat,str(1).toFloat,str(2).toFloat)
  private def parseFaceLine(str: String*): Vector3f = {
    val vert0 = str(0).split("/")
    val vert1 = str(0).split("/")
    val vert2 = str(0).split("/")
    new Vector3f(vert0(0).toFloat,vert1(1).toFloat,vert2(2).toFloat)
  }
  private def parseTextureCoordinateLine(str: String*): Vector2f = {new Vector2f(str(0).toFloat,str(1).toFloat)}
  private def parseVertexNormalLine(str: String*): Vector3f = {new Vector3f(str(0).toFloat,str(1).toFloat,str(2).toFloat)}
}

class Mesh {

}

class Vertex(val x:Float,val y:Float,val z:Float){
  override def toString() = {
    "" + x + ", " + y + ", " + z
  }
}
class Face(val vertices: Vector[Vertex])