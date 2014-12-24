package tilde.game

import java.nio.FloatBuffer

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.{GL11, Display}
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import org.lwjgl.util.vector.{Matrix4f, Vector3f}
import tilde.entity.component.SpatialComponent
import tilde.graphics.{Mesh, Camera, ShaderProgram, Texture}
import tilde.log.Log
import tilde.util.{Direction, Transform}

/**
 * Created by Toni on 13.12.2014.
 */
class Game {
  lazy val shader: ShaderProgram = new ShaderProgram

  var vaoID = 0

  var texture: Texture = null

  var testSpatial = new SpatialComponent()
  var camera: Camera = null
  val testMesh = Mesh.load("data/meshes/teapot_test.obj")

  def create(): Unit = {
    //testSpatial.rotate(0,45,0)
    testSpatial.scale(new Vector3f(0.25f,0.25f,0.25f))
    //testSpatial.move(Direction.UP,1)
    val aspect = Display.getWidth.toFloat / Display.getHeight.toFloat
    camera = new Camera(100f,0.1f, aspect,60)

    val cameraSpatial = camera.getComponent(SpatialComponent.id).get
    cameraSpatial.setPosition(new Vector3f(0,2,2))
    cameraSpatial.rotateX(-45f)

    // Shader setup
    shader.attachVertexShader("data/shaders/default.vert")
    shader.attachFragShader("data/shaders/default.frag")
    shader.link()

    shader.setUniform("tex",0)

    // Vert Data
    val d = testMesh.getRawData
    val verts = BufferUtils.createFloatBuffer(d.length)
    verts.put(d)
    verts.rewind()

    // Elements
    val e = testMesh.getElements.toArray
    val elem = BufferUtils.createShortBuffer(e.length)
    elem.put(e)
    elem.rewind()

    // vao
    vaoID = glGenVertexArrays()
    glBindVertexArray(vaoID)

    //data
    val vertsID = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER,vertsID)
    glBufferData(GL_ARRAY_BUFFER,verts,GL_STATIC_DRAW)
    glVertexAttribPointer(0, 3, GL_FLOAT, false, 8*4, 0*4) // verts
    glVertexAttribPointer(1, 2, GL_FLOAT, false, 8*4, 3*4) // uv
    glVertexAttribPointer(2, 3, GL_FLOAT, false, 8*4, 5*4) // normal
    glBindBuffer(GL_ARRAY_BUFFER, 0)

    //elem
    val elemID = glGenBuffers()
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,elemID)
    glBufferData(GL_ELEMENT_ARRAY_BUFFER,elem,GL_STATIC_DRAW)
    glBindBuffer(GL_ARRAY_BUFFER, 0)

    glBindVertexArray(0)

    texture = Texture.load("data/textures/measure_128.png")
    texture.setActiveAsUnit(0)
    texture.bind()

    glEnable(GL_DEPTH_TEST)
    //glPolygonMode( GL_FRONT_AND_BACK, GL_LINE )
    //glEnable(GL_CULL_FACE)
    glClearColor(0.031f, 0.663f, 1.0f, 0.0f)

  }

  def render(): Unit = {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

    camera.update()
    shader.bind()

    //Log.debug("transform", testSpatial.toString )
    //Log.debug("view", camera.viewMatrix.toString)
    //Log.debug("proj", camera.projectionMatrix.toString)

    shader.setUniform("m_model", testSpatial.getFloatBuffer)
    shader.setUniform("m_view", camera.getViewBuffer)
    shader.setUniform("m_proj", camera.getProjectionBuffer)

    glBindVertexArray(vaoID)
    glEnableVertexAttribArray(0)
    glEnableVertexAttribArray(1)
    glEnableVertexAttribArray(2)
    glDrawElements(GL_TRIANGLES,testMesh.getElemCount,GL_UNSIGNED_SHORT,0)
    //GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST)

    glDisableVertexAttribArray(0)
    glDisableVertexAttribArray(1)
    glDisableVertexAttribArray(2)
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
    testSpatial.rotate(1, new Vector3f(0,1,0))
    //testSpatial.move(Direction.AXIS_Z,0.002f)
    //testSpatial.scale(new Vector3f(0.999f,0.999f,0.999f))
    //camera.move(Direction.FORWARD, 0.001f)
    //camera.getComponent(SpatialComponent.id).get.rotate(0.5f, camera.getComponent(SpatialComponent.id).get.forward)
  }

  def dispose() = {
    // Dispose the VAO
    shader.dispose()

    glBindVertexArray(0)
    glDeleteVertexArrays(vaoID)

    // Dispose the VBO
    glBindBuffer(GL_ARRAY_BUFFER, 0)
  }
}
