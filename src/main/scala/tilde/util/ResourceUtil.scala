package tilde.util

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import org.lwjgl.util.vector.{Quaternion, Vector3f, Vector4f}
import spray.json._
import tilde.entity._
import tilde.graphics._
import tilde.log.Log

import scala.io.Source



/**
 * Created by Toni on 8.1.2015.
 */

object ResourceUtil {
  private val RESOURCE_ROOT_PATH = "src/main/resources/"
  private val FLOAT_SIZE         = 4

  private val VERTEX_DATA_LENGTH = 3
  private val NORMAL_DATA_LENGTH = 3
  private val UV_DATA_LENGTH     = 2
  private val COLOR_DATA_LENGTH  = 3

  private val DATA_OFFSET        = VERTEX_DATA_LENGTH + NORMAL_DATA_LENGTH + 
                                   UV_DATA_LENGTH     + COLOR_DATA_LENGTH

  private val VERTEX_DATA_STRIDE = 0
  private val NORMAL_DATA_STRIDE = VERTEX_DATA_LENGTH
  private val UV_DATA_STRIDE     = VERTEX_DATA_LENGTH + NORMAL_DATA_LENGTH
  private val COLOR_DATA_STRIDE  = VERTEX_DATA_LENGTH + NORMAL_DATA_LENGTH + 
                                   UV_DATA_LENGTH
  import spray.json.DefaultJsonProtocol._





  implicit val vector3Format = new RootJsonFormat[Vector3f] {
    def write(obj: Vector3f): JsValue =
      JsArray(JsNumber(obj.x), JsNumber(obj.y), JsNumber(obj.z))
    def read(json: JsValue) = json match {
      case JsArray(Vector(JsNumber(x), JsNumber(y), JsNumber(z))) =>
        new Vector3f(x.toFloat,y.toFloat,z.toFloat)
      case _ => deserializationError("Parse error" + "Can't parse" + json.prettyPrint + " as Vector3")
    }
  }

  implicit val vector4Format = new RootJsonFormat[Vector4f] {
    def write(obj: Vector4f): JsValue =
      JsArray(JsNumber(obj.x), JsNumber(obj.y), JsNumber(obj.z),JsNumber(obj.w))
    def read(json: JsValue) = json match {
      case JsArray(Vector(JsNumber(x), JsNumber(y), JsNumber(z),JsNumber(w))) =>
        new Vector4f(x.toFloat,y.toFloat,z.toFloat,w.toFloat)
      case _ => deserializationError("Parse error" + "Can't parse" + json.prettyPrint + " as Vector4")
    }
  }

  implicit val quaternionFormat = new RootJsonFormat[Quaternion] {
    def write(obj: Quaternion): JsValue =
      JsArray(JsNumber(obj.x), JsNumber(obj.y), JsNumber(obj.z),JsNumber(obj.w))
    def read(json: JsValue) = json match {
      case JsArray(Vector(JsNumber(x), JsNumber(y), JsNumber(z),JsNumber(w))) =>
        new Quaternion(x.toFloat,y.toFloat,z.toFloat,w.toFloat)
      case _ => deserializationError("Parse error" + "Can't parse" + json.prettyPrint + " as Quaternion")
    }
  }

  implicit val ModelFormat                = jsonFormat2(Model.apply)
  //implicit val SpatialComponentFormat     = jsonFormat3(SpatialComponent.apply)
  implicit val ModelComponentFormat       = jsonFormat1(ModelComponent.apply)
  //implicit val CameraComponentFormat      = jsonFormat1(CameraComponent.apply)
  implicit val PhysicsComponentFormat     = jsonFormat2(PhysicsComponent.apply)
  implicit val LightSourceComponentFormat = jsonFormat6(LightSourceComponent.apply)

  implicit val componentFormat = new RootJsonFormat[Component] {
    def write(obj: Component): JsValue =
      JsObject((obj match {
        //case c: SpatialComponent     => c.toJson
        case m: ModelComponent       => m.toJson
        //case c: CameraComponent      => c.toJson
        case p: PhysicsComponent     => p.toJson
        case l: LightSourceComponent => l.toJson
      }).asJsObject.fields + ("type" -> JsString(obj.productPrefix)))

    def read(json: JsValue): Component =
      json.asJsObject.getFields("type") match {
        //case Seq(JsString("SpatialComponent"))     => json.convertTo(SpatialComponentFormat)
        case Seq(JsString("ModelComponent"))       => json.convertTo[ModelComponent]
        //case Seq(JsString("CameraComponent"))      => json.convertTo(CameraComponentFormat)
        case Seq(JsString("PhysicsComponent"))     => json.convertTo[PhysicsComponent]
        case Seq(JsString("LightSourceComponent")) => json.convertTo[LightSourceComponent]
      }
  }

  implicit val entityFormat = new RootJsonFormat[Entity] {
    def write(obj: Entity): JsValue = obj.components.values.toJson

    def read(json: JsValue) = {deserializationError("Not implemented")}
  }

  implicit val worldFormat = new RootJsonFormat[World] {
    def write(obj: World): JsValue = {
      obj.entities.toList.toJson
    }

    def read(json: JsValue) = {deserializationError("Not implemented")}
  }
  def loadModel(path: String): Model =
    ResourceUtil.readFromFile(RESOURCE_ROOT_PATH + path).parseJson.convertTo[Model]

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
    glShaderSource(shaderID,shaderSource.toCharArray())

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
    glShaderSource(shaderID,shaderSource.toCharArray())

    glCompileShader(shaderID)

    // Check for errors
    if(glGetShaderi(shaderID,GL_COMPILE_STATUS) == GL_FALSE){
      Log.error("Couldn't create fragment shader: ",
      glGetShaderInfoLog(shaderID, glGetShaderi(shaderID, GL_INFO_LOG_LENGTH)))
    }
    shaderID
  }

  def worldToFile(world: World,path: String): Unit = {
    Log.debug("World to file. ents length","" + world.entities.length)
    writeToFile(path,world.toJson.prettyPrint)
  }

  def writeToFile(path: String, s: String): Unit = {
    val file = new File(RESOURCE_ROOT_PATH + path)
    file.createNewFile()
    val pw = new java.io.PrintWriter(file)
    try pw.write(s) finally pw.close()
  }

  def readFromFile(path:String): String = Source.fromFile(path).mkString

  def readImageFromFile(path:String): BufferedImage = ImageIO.read(new File(path))

  def readLinesFromFile(path:String) = scala.io.Source.fromFile(new File(path)).getLines().toVector

}