package tilde.util

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import tilde.graphics.Mesh
import tilde.log.Log
import tilde.graphics.Texture
import scala.io.Source

/**
 * Created by Toni on 8.1.2015.
 */
object ResourceUtil {
  val RESOURCE_PATH = ""
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
    def intColorToFloat(i: Float): Float = (i / 255)

    val lines = readLinesFromFile(path)

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
  def loadTexture(path: String): Texture = {
    var bufImage: BufferedImage = null
    try {
      bufImage = readImageFromFile(path)
    } catch{
      case e: Exception => {
        bufImage = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB)
        bufImage.setRGB(0,0,0xFFFFFF)
      }
    }
    val pixels = Array.ofDim[Int](bufImage.getWidth() * bufImage.getHeight())

    bufImage.getRGB(0, 0, bufImage.getWidth(), bufImage.getHeight(), pixels, 0, bufImage.getWidth())

    val bytes = BufferUtils.createByteBuffer(pixels.length * 4)

    for (x <- 0 until bufImage.getWidth; y <- 0 until bufImage.getHeight) {
      //val pix = pixels(y * bufImage.getWidth() + x)
      val pix = pixels(y * bufImage.getWidth() + x)

      bytes.put(((pix >> 16) & 0xFF).toByte) // RED
      bytes.put(((pix >> 8) & 0xFF).toByte) // GREEN
      bytes.put((pix & 0xFF).toByte) // BLUE
      bytes.put(((pix >> 24) & 0xFF).toByte) // ALPHA
    }
    bytes.rewind()

    val texID: Int = glGenTextures()

    glBindTexture(GL_TEXTURE_2D, texID)

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, bufImage.getWidth(), bufImage.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, bytes)

    glGenerateMipmap(GL_TEXTURE_2D)

    new Texture(bufImage.getWidth, bufImage.getHeight, texID)
  }
  def loadObject(path: String) = {

  }

  def readFromFile(path:String): String = Source.fromFile(path).mkString
  def readImageFromFile(path:String): BufferedImage = ImageIO.read(new File(path))
  def readLinesFromFile(path:String) = scala.io.Source.fromFile(new File(path)).getLines().toVector
}