package tilde.entity.system

import java.nio.FloatBuffer

import tilde.ResourceManager
import tilde.log._
import tilde.entity.{World, Entity}
import tilde.entity.component.{CameraComponent, ModelComponent, SpatialComponent}
import tilde.graphics.ShaderProgram
import tilde.util.SystemUtil
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import scala.collection.mutable._

/**
 * Created by Toni on 25.12.14.
 */
class RenderSystem() extends EntitySystem(SystemUtil.createAspect(ModelComponent,SpatialComponent)) {
  lazy val shader: ShaderProgram = ResourceManager.shaderPrograms("default")

  var camera: Entity = null

  // Multimap for all different instances to render
  //val batches = new HashMap[ModelComponent, Set[FloatBuffer]] with MultiMap[ModelComponent, FloatBuffer]
  val ents = Buffer[Entity]()

  override def process(e: Entity): Unit = {
    // Adding entity to batch
    //Log.debug("RenderSystem processing entity", "" + e)
    val spatial = e.getComponent(SpatialComponent.id).get
    val model = e.getComponent(ModelComponent.id).get
    //println("spatial = " + spatial)
    //println("model = " + model)
    ents += e
    //batches.addBinding(model,spatial.getFloatBuffer)
  }

  override def begin(): Unit = {
    super.begin()
    shader.bind()
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
    //Log.debug("RendererSystem begin","worlds taglist: " + world.tags.toString)
    camera = world.getTagged("camera")
  }

  // TODO: Fix super bad implementation plz!
  override def end(): Unit = {
    super.end()
    // Render all batches
    val cameraComp = camera.getComponent(CameraComponent.id).get

    for(ent <- ents){
      val model = ent.getComponent(ModelComponent.id).get
      val transformation = ent.getComponent(SpatialComponent.id).get.getFloatBuffer

      val vaoID = glGenVertexArrays()
      glBindVertexArray(vaoID)

      model.getMesh.bindData()
      model.getMesh.bufferData()
      glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * 4, 0 * 4) // verts
      glVertexAttribPointer(1, 2, GL_FLOAT, false, 8 * 4, 3 * 4) // uv
      glVertexAttribPointer(2, 3, GL_FLOAT, false, 8 * 4, 5 * 4) // normal
      model.getMesh.unbind()

      model.getMesh.bindElem()
      model.getMesh.bufferElem()
      model.getMesh.unbind()
      glBindVertexArray(0)

      model.getTexture.setActiveAsUnit(0)
      model.getTexture.bind()

      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)


      shader.setUniform("m_model", transformation)
      shader.setUniform("m_view", cameraComp.viewBuffer)
      shader.setUniform("m_proj", cameraComp.projectionBuffer)

      glBindVertexArray(vaoID)
      glEnableVertexAttribArray(0)
      glEnableVertexAttribArray(1)
      glEnableVertexAttribArray(2)
      glDrawElements(GL_TRIANGLES,model.getMesh.elemCount,GL_UNSIGNED_SHORT,0)
      //GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST)

      glDisableVertexAttribArray(0)
      glDisableVertexAttribArray(1)
      glDisableVertexAttribArray(2)
      glBindVertexArray(0)
    }
    shader.unbind()
    ents.clear()
    /*for (batch <- batches) {
      val model = batch._1
      val transformations = batch._2

      val vaoID = glGenVertexArrays()
      glBindVertexArray(vaoID)

      model.getMesh.bindData()
      model.getMesh.bufferData()
      glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * 4, 0 * 4) // verts
      glVertexAttribPointer(1, 2, GL_FLOAT, false, 8 * 4, 3 * 4) // uv
      glVertexAttribPointer(2, 3, GL_FLOAT, false, 8 * 4, 5 * 4) // normal
      model.getMesh.unbind()

      model.getMesh.bindElem()
      model.getMesh.bufferElem()
      model.getMesh.unbind()

      glBindVertexArray(0)

      model.getTexture.setActiveAsUnit(0)
      model.getTexture.bind()

      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
      shader.bind()

      for(trans <- transformations){
        shader.setUniform("m_model", trans)
        shader.setUniform("m_view", cameraComp.viewBuffer)
        shader.setUniform("m_proj", cameraComp.projectionBuffer)

        glBindVertexArray(vaoID)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)
        glEnableVertexAttribArray(2)
        glDrawElements(GL_TRIANGLES,model.getMesh.elemCount,GL_UNSIGNED_SHORT,0)
        //GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST)

        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
        glDisableVertexAttribArray(2)
        glBindVertexArray(0)
        shader.unbind()
      }

    }*/
  }

  override def toString() ={
    "RenderSystem"
  }
}
