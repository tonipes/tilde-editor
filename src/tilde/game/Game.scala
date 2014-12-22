package tilde.game

import java.nio.FloatBuffer

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.{GL11, Display}
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import org.lwjgl.util.vector.Vector3f
import tilde.Entity.Component.SpatialComponent
import tilde.graphics.{Camera, ShaderProgram, Texture}
import tilde.log.Log
import tilde.util.{Direction, Transform}

/**
 * Created by Toni on 13.12.2014.
 */
class Game {
  lazy val shader: ShaderProgram = new ShaderProgram

  var vertsID = 0
  var uvID = 0
  var vaoID = 0
  var elemID = 0
  var texture: Texture = null
  //var transform: Transform = new Transform()
  var testSpatial = new SpatialComponent()
  var camera: Camera = null


  def create(): Unit = {
    testSpatial.rotate(0,45,0)
    Log.debug("Height" , "" + Display.getHeight)
    Log.debug("Width" , "" + Display.getWidth)
    val aspect = Display.getWidth.toFloat / Display.getHeight.toFloat
    Log.debug("Aspect ratio" , "" + aspect)
    camera = new Camera(100f,0.1f, aspect,60)
    camera.setPosition(new Vector3f(0,2,2))
    camera.rotateX(-45f)

    // Shader setup
    shader.attachVertexShader("data/shaders/default.vert")
    shader.attachFragShader("data/shaders/default.frag")
    shader.link()

    shader.setUniform("tex",0)

    // color
    val uv = Array[Float](
      0, 0,    // Top Left
      0, 1,    // Top Right
      1, 0,    // Bottom Left
      1, 1,    // Bottom Right
      1, 0,    // Top Left
      -1, -1,    // Top Right
      0, 1,    // Bottom Left
      -1, -1    // Bottom Right

    )

    val uvs = BufferUtils.createFloatBuffer(uv.length)
    uvs.put(uv)
    uvs.rewind()

    // setup vertex data buffer
    val v = Array[Float](
      -0.5f, +0.5f, +0.5f,  // Left,  Top,    Front
      +0.5f, +0.5f, +0.5f,  // Right, Top,    Front
      -0.5f, -0.5f, +0.5f,  // Left,  Bottom, Front
      +0.5f, -0.5f, +0.5f,  // Right, Bottom, Front

      -0.5f, +0.5f, -0.5f,  // Left,  Top,    Back
      +0.5f, +0.5f, -0.5f,  // Right  Top,    Back
      -0.5f, -0.5f, -0.5f,  // Left,  Bottom, Back
      +0.5f, -0.5f, -0.5f   // Right, Bottom, Back

    )

    val verts = BufferUtils.createFloatBuffer(v.length)
    verts.put(v)
    verts.rewind()

    // Elements
    val e = Array[Short](
      0,2,1,1,2,3,   // Front
      5,7,4,4,7,6,    // Back
      2,6,3,3,6,7,    // Bottom
      1,3,5,5,3,7,     // Right
      4,6,0,0,6,2,    //Left
      4,0,5,5,0,1
    )

    val elem = BufferUtils.createShortBuffer(e.length)
    elem.put(e)
    elem.rewind()

    // vao
    vaoID = glGenVertexArrays()
    glBindVertexArray(vaoID)

    //vbo vertex
    vertsID = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER,vertsID)
    glBufferData(GL_ARRAY_BUFFER,verts,GL_STATIC_DRAW)
    glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
    glBindBuffer(GL_ARRAY_BUFFER, 0)

    //uvs
    uvID = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER,uvID)
    glBufferData(GL_ARRAY_BUFFER,uvs,GL_STATIC_DRAW)
    glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0)
    glBindBuffer(GL_ARRAY_BUFFER, 0)

    //elem
    elemID = glGenBuffers()
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,elemID)
    glBufferData(GL_ELEMENT_ARRAY_BUFFER,elem,GL_STATIC_DRAW)
    //glBindBuffer(GL_ARRAY_BUFFER, 0)

    glBindVertexArray(0)

    texture = Texture.load("data/textures/measure_128.png")
    texture.setActiveAsUnit(0)
    texture.bind()

    glEnable(GL_DEPTH_TEST)
    //glPolygonMode( GL_FRONT_AND_BACK, GL_LINE )
    glEnable(GL_CULL_FACE)

  }

  def render(): Unit = {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

    camera.update()
    shader.bind()
    //Log.debug("transform", testSpatial.toString )
    //Log.debug("view", camera.viewMatrix.toString)
    //Log.debug("proj", camera.projectionMatrix.toString)

    shader.setUniform("m_model", testSpatial.getFloatBuffer())
    shader.setUniform("m_view", camera.getViewBuffer)
    shader.setUniform("m_proj", camera.getProjectionBuffer)

    glBindVertexArray(vaoID)
    glEnableVertexAttribArray(0)
    glEnableVertexAttribArray(1)

    glDrawElements(GL_TRIANGLES,36,GL_UNSIGNED_SHORT,0)

    glDisableVertexAttribArray(0)
    glDisableVertexAttribArray(1)
    glBindVertexArray(0)
    shader.unbind()
  }

  def resize(width: Int, height: Int): Unit = {
    GL11.glViewport(0,0,width,height)
    Log.info("Game resize", ""+ width + ", " + height)
  }

  def update(delta: Float): Unit = {
    val d = delta*100
    //testSpatial.rotate(d,d/2,d/3)
    //camera.move(Direction.FORWARD, 0.001f)
    camera.rotate(0.5f, camera.right)
  }

  def dispose() = {
    // Dispose the VAO
    shader.dispose()

    glBindVertexArray(0)
    glDeleteVertexArrays(vaoID)

    // Dispose the VBO
    glBindBuffer(GL_ARRAY_BUFFER, 0)
    glDeleteBuffers(vertsID)
  }
}
