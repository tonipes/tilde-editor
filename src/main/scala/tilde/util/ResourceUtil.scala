package tilde.util

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import java.nio.FloatBuffer

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import tilde.graphics.Mesh
import tilde.log.Log
import tilde.graphics._
import scala.io.Source
import argonaut._, Argonaut._
import scalaz._, Scalaz._

/**
 * Created by Toni on 8.1.2015.
 */

object ResourceUtil {
  private val RESOURCE_ROOT_PATH = "src/main/resources/"
  private val FLOAT_SIZE = 4

  private val VERTEX_DATA_LENGTH = 3
  private val NORMAL_DATA_LENGTH = 3
  private val UV_DATA_LENGTH     = 2
  private val COLOR_DATA_LENGTH  = 3

  private val DATA_OFFSET = VERTEX_DATA_LENGTH + NORMAL_DATA_LENGTH + 
                            UV_DATA_LENGTH     + COLOR_DATA_LENGTH

  private val VERTEX_DATA_STRIDE =  0
  private val NORMAL_DATA_STRIDE =  VERTEX_DATA_LENGTH
  private val UV_DATA_STRIDE =      VERTEX_DATA_LENGTH + NORMAL_DATA_LENGTH
  private val COLOR_DATA_STRIDE =   VERTEX_DATA_LENGTH + NORMAL_DATA_LENGTH + 
                                    UV_DATA_LENGTH

  implicit def ModelCodec: CodecJson[Model] = 
    codec2(Model.parse,Model.decode)("mesh", "material")

  def loadModel(path: String): Model = {
    val a = Parse.decodeOption[Model](readFromFile(RESOURCE_ROOT_PATH + path))
    Log.debug("Model _1", "" + a.get)
    a.get
  }

  def loadMesh(path: String): Mesh = {
    // Simple function to convert int rgb to float rgb
    def intColorToFloat(i: Float): Float = (i / 255)

    val lines = readLinesFromFile(RESOURCE_ROOT_PATH + path)

     // Finds line "element count X", where X is vertex count
    val vertCount = lines.find(a => {
      val splits = a.split(" ")
      splits(0) == "element" && splits(1) == "vertex"
    }).get.split(" ")(2).toInt

    // Finds line "element face X", where X is face count
    val faceCount = lines.find(a => {
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
      val parts = lines(i).split(" ")

      if(parts.length != 11){
        Log.error("Unsupported vertex data with vertex " + i, 
          parts.length + " data segments found, should be " + DATA_OFFSET)
      }

      for(f <- parts.indices){
        if(f > 7) vertexDataBuffer.put(intColorToFloat(parts(f).toFloat))
        else vertexDataBuffer.put(parts(f).toFloat)
      }
    }
    // Faces
    for(i <- indexOfFirstElem until indexOfFirstElem + faceCount){
      val parts = lines(i).split(" ")

      if(parts(0) != "3") { // First part is the vertex count
        Log.error("Unsupported vertex count", "Faces needs to have 3 vertices")
      }

      // Add rest of parts to element buffer
      parts.tail.foreach(d => elementDataBuffer.put(d.toInt)) 
    }

    vertexDataBuffer.rewind()
    elementDataBuffer.rewind()
    val vaoID = glGenVertexArrays()
    glBindVertexArray(vaoID)

    val vertexDataID = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER,vertexDataID)
    glBufferData(GL_ARRAY_BUFFER,vertexDataBuffer,GL_STATIC_DRAW)

    glVertexAttribPointer(0, 3, GL_FLOAT, false, 
                          DATA_OFFSET * FLOAT_SIZE, 
                          VERTEX_DATA_STRIDE * FLOAT_SIZE) // verts

    glVertexAttribPointer(1, 3, GL_FLOAT, false, 
                          DATA_OFFSET * FLOAT_SIZE, 
                          NORMAL_DATA_STRIDE * FLOAT_SIZE) // normal

    glVertexAttribPointer(2, 2, GL_FLOAT, false,
                          DATA_OFFSET * FLOAT_SIZE, 
                          UV_DATA_STRIDE * FLOAT_SIZE)     // uv

    glVertexAttribPointer(3, 3, GL_FLOAT, false, 
                          DATA_OFFSET * FLOAT_SIZE, 
                          COLOR_DATA_STRIDE * FLOAT_SIZE)  // color

    glBindBuffer(GL_ARRAY_BUFFER,0)

    val elementID = glGenBuffers()
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,elementID)
    glBufferData(GL_ELEMENT_ARRAY_BUFFER,elementDataBuffer,GL_STATIC_DRAW)
    //glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0)

    glBindVertexArray(0)

    new Mesh( vaoID,vertexDataID,elementID,
              elementDataBufferLength,vertexDataBufferLength)
  }

  def loadTexture(path: String): Texture = {
    var bufImage: BufferedImage = null
    try {

      bufImage = readImageFromFile(RESOURCE_ROOT_PATH +path)

    } catch{
      case e: Exception => {
        bufImage = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB)
        bufImage.setRGB(0,0,0xFFFFFF)
      }
    }
    val pixels = Array.ofDim[Int](bufImage.getWidth() * bufImage.getHeight())

    bufImage.getRGB(0, 0, bufImage.getWidth(),
                    bufImage.getHeight(), pixels, 0, bufImage.getWidth())

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

    glTexImage2D( GL_TEXTURE_2D, 0, GL_RGBA8, bufImage.getWidth(), bufImage.getHeight(), 
                  0, GL_RGBA, GL_UNSIGNED_BYTE, bytes)

    glGenerateMipmap(GL_TEXTURE_2D)

    new Texture(bufImage.getWidth, bufImage.getHeight, texID)
  }

  def loadObject(path: String) = {
    // TODO: "object" needs better name...
    ???
  }

  def loadShader(VertPath: String,FragPath: String): ShaderProgram = {
    val programID = glCreateProgram()

    val vertShaderID = loadVertexShader(RESOURCE_ROOT_PATH + VertPath)
    glAttachShader(programID,vertShaderID)

    val fragShaderID = loadFragmentShader(RESOURCE_ROOT_PATH + FragPath)
    glAttachShader(programID,fragShaderID)
    Log.debug("Shader IDs",programID + ", " + vertShaderID + ", " + fragShaderID ) 
    val shaderP = new ShaderProgram(programID,vertShaderID,fragShaderID)

    glLinkProgram(programID)
    if(glGetShaderi(programID,GL_LINK_STATUS) == GL_FALSE) {
      // TODO: What to do if cant link? Exception or default shader?
      Log.error("Couldn't link shader")
    }
    shaderP
  }

  private def loadVertexShader(path: String): Int = {
    // load shader source
    val shaderSource = ResourceUtil.readFromFile(path)

    // create shader
    val shaderID = glCreateShader(GL_VERTEX_SHADER)
    glShaderSource(shaderID,shaderSource)

    glCompileShader(shaderID)

    // Check for errors
    if(glGetShaderi(shaderID,GL_COMPILE_STATUS) == GL_FALSE){
      Log.error("Couldn't create vertex shader: ",
      glGetShaderInfoLog(shaderID, glGetShaderi(shaderID, GL_INFO_LOG_LENGTH)))
    }
    shaderID
  }

  private def loadFragmentShader(path: String): Int = {
    // load shader source
    val shaderSource = ResourceUtil.readFromFile(path)
    // create shader
    val shaderID = glCreateShader(GL_FRAGMENT_SHADER)
    glShaderSource(shaderID,shaderSource)

    glCompileShader(shaderID)

    // Check for errors
    if(glGetShaderi(shaderID,GL_COMPILE_STATUS) == GL_FALSE){
      Log.error("Couldn't create fragment shader: ",
      glGetShaderInfoLog(shaderID, glGetShaderi(shaderID, GL_INFO_LOG_LENGTH)))
    }
    shaderID
  } 

  def readFromFile(path:String): String = 
    Source.fromFile(path).mkString

  def readImageFromFile(path:String): BufferedImage = 
    ImageIO.read(new File(path))

  def readLinesFromFile(path:String) = 
    scala.io.Source.fromFile(new File(path)).getLines().toVector

}