package tilde.game

import java.nio.FloatBuffer

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.{GL11, Display}
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import org.lwjgl.util.vector.{Matrix4f, Vector3f}
import tilde.ResourceManager
import tilde.entity.Entity
import tilde.entity.component.{ModelComponent, SpatialComponent}
import tilde.graphics.{Mesh, Camera, ShaderProgram, Texture}
import tilde.log.Log
import tilde.util.{Direction, Transform}

/**
 * Created by Toni on 13.12.2014.
 */
class Game {
  var shader: ShaderProgram = null
  var camera: Camera = null

  val entity = new Entity()
      entity.addComponent(new SpatialComponent())
      entity.addComponent(new ModelComponent("cube","measure"))

  var vaoID = 0



  def create(): Unit = {
    val aspect = Display.getWidth.toFloat / Display.getHeight.toFloat
    camera = new Camera(100f,0.1f, aspect,60)

    shader = ResourceManager.shaderPrograms("default")

    val cameraSpatial = camera.getComponent(SpatialComponent.id).get
    cameraSpatial.setPosition(new Vector3f(0,0,2))
    //cameraSpatial.rotateX(-45f)

    val entitySpatial = entity.getComponent(SpatialComponent.id).get

    //Log.debug("EntitySpatial up before", "" + entitySpatial.up.toString)
    //Log.debug("EntitySpatial forward before", "" + entitySpatial.forward.toString)
    //Log.debug("EntitySpatial right before", "" + entitySpatial.right.toString)

    entitySpatial.scale(new Vector3f(0.5f,0.5f,0.5f))
    entitySpatial.rotate(-45,entitySpatial.up)
    //entitySpatial.rotate(180,entitySpatial.up)

    //Log.debug("EntitySpatial up after", "" + entitySpatial.up.toString)
    //Log.debug("EntitySpatial forward after", "" + entitySpatial.forward.toString)
    //Log.debug("EntitySpatial right after", "" + entitySpatial.right.toString)

    // Vert Data
    val d = entity.getComponent(ModelComponent.id).get.getMesh.getRawData
    val verts = BufferUtils.createFloatBuffer(d.length)
    verts.put(d)
    verts.rewind()

    // Elements
    val e = entity.getComponent(ModelComponent.id).get.getMesh.getElements.toArray
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

    entity.getComponent(ModelComponent.id).get.getTexture.setActiveAsUnit(0)
    entity.getComponent(ModelComponent.id).get.getTexture.bind()

    glEnable(GL_DEPTH_TEST)
    //glPolygonMode( GL_FRONT_AND_BACK, GL_LINE )
    //glEnable(GL_CULL_FACE)
    glClearColor(0.031f, 0.663f, 1.0f, 0.0f)

  }

  def render(): Unit = {
    val entSpatial = entity.getComponent(SpatialComponent.id).get
    val entModel = entity.getComponent(ModelComponent.id).get
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

    camera.update()
    shader.bind()

    shader.setUniform("m_model", entSpatial.getFloatBuffer)
    shader.setUniform("m_view", camera.getViewBuffer)
    shader.setUniform("m_proj", camera.getProjectionBuffer)

    glBindVertexArray(vaoID)
    glEnableVertexAttribArray(0)
    glEnableVertexAttribArray(1)
    glEnableVertexAttribArray(2)
    glDrawElements(GL_TRIANGLES,entModel.getMesh.getElemCount,GL_UNSIGNED_SHORT,0)
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
    val entitySpatial = entity.getComponent(SpatialComponent.id).get
    //entitySpatial.rotate(1,entitySpatial.forward)
    entitySpatial.move(entitySpatial.right, 0.01f)
  }

  def dispose() = {
    // Dispose the VAO
    shader.dispose()

    glBindVertexArray(0)
    glDeleteVertexArrays(vaoID)

    // Dispose the VBO
    glBindBuffer(GL_ARRAY_BUFFER, 0)
    shader.dispose()
    entity.dispose()
  }
}
