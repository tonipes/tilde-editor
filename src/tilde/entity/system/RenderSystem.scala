package tilde.entity.system

import java.nio.FloatBuffer

import tilde.ResourceManager
import tilde.log._
import tilde.entity.{World, Entity}
import tilde.entity.component.{CameraComponent, ModelComponent, SpatialComponent}
import tilde.graphics.ShaderProgram
import tilde.util.SystemUtil
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import scala.collection.mutable._

/**
 * Created by Toni on 25.12.14.
 */
class RenderSystem() extends EntitySystem() {
  this.aspect = SystemUtil.createAspect(ModelComponent,SpatialComponent)

  lazy val shader: ShaderProgram = ResourceManager.shaderPrograms("default")

  var camera: Entity = null

  // Multimap for all different instances to render
  //val batches = new HashMap[ModelComponent, Set[FloatBuffer]] with MultiMap[ModelComponent, FloatBuffer]
  val ents = Buffer[Entity]()

  override def process(e: Entity): Unit = {
    ents += e
  }

  override def systemBegin(): Unit = {
    shader.bind()
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
    camera = world.getTagged("camera")
  }

  // TODO: Fix super bad implementation plz!
  override def systemEnd(): Unit = {
    // Render all batches
    //Log.debug("Rendering","" + ents.length + " entities")
    val cameraComp = camera.getComponent(CameraComponent.id).get
    for(ent <- ents){
      val model = ent.getComponent(ModelComponent.id).get
      val transformation = ent.getComponent(SpatialComponent.id).get.getFloatBuffer

      model.getTexture.setActiveAsUnit(0)
      model.getTexture.bind()

      shader.setUniform("m_model", transformation)
      shader.setUniform("m_view", cameraComp.viewBuffer)
      shader.setUniform("m_proj", cameraComp.projectionBuffer)


      model.getMesh.bindVAO()
      glEnableVertexAttribArray(0)
      glEnableVertexAttribArray(1)
      glEnableVertexAttribArray(2)
      glDrawElements(GL_TRIANGLES,model.getMesh.elemCount,GL_UNSIGNED_SHORT,0)

      glDisableVertexAttribArray(0)
      glDisableVertexAttribArray(1)
      glDisableVertexAttribArray(2)
      model.getMesh.unbindVAO()
    }
    shader.unbind()
    ents.clear()
  }

  override def toString() ={
    "RenderSystem"
  }
}
