package tilde.system

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._
import tilde.util.Matrix4
import tilde.{ModelComponent, SpatialComponent, Component, Entity, ResourceManager}
import tilde.systems.EntitySystem
import tilde.graphics.ShaderProgram
import scala.collection.immutable.BitSet
import scala.collection.mutable

/**
 * Created by Toni on 17.1.2015.
 */
class RenderSystem extends EntitySystem{
  override var compStruct: BitSet =
    BitSet.empty +
      Component.getID(classOf[SpatialComponent]) +
      Component.getID(classOf[ModelComponent])

  // GL setup
  glEnable(GL_DEPTH_TEST)
  glEnable(GL_CULL_FACE)
  glClearColor(0.5f, 0.5f, 0.5f, 0.5f)

  lazy val shader: ShaderProgram = ResourceManager.shaderPrograms("default")

  override def systemBegin(): Unit = {
    shader.bind()
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
  }

  override protected def process(e: Entity, delta: Float): Unit = {
    // TODO: Batch entities
  }
  
  override def systemEnd(): Unit = { // Render
    val camera = world.getTagged("camera")

    if(!camera.isDefined) throw new IllegalStateException("No camera found!")

    val cameraPos = camera.get.getComponent(Component.spatial).get.position

    // set uniform
    shader.setUniform("c_position",cameraPos.x,cameraPos.y,cameraPos.z)
    shader.setUniform("l_position",cameraPos.x,cameraPos.y,cameraPos.z)
    shader.setUniform("l_color",0.5f,0.5f,0.5f,0.5f)
    //shader.setUniform("m_view", cameraComp.viewBuffer)
    //shader.setUniform("m_proj", cameraComp.projectionBuffer)

    // render entities
    for(ent <- this.entities){
      val model    = ResourceManager.models(ent.getComponent(Component.model).get.model)
      val mesh     = ResourceManager.meshes(model.mesh)
      val material = ResourceManager.materials(model.material)

      val transformation = ent.getComponent(Component.spatial).get // toFloatBuffer

      mesh.bindVAO()
      glEnableVertexAttribArray(0)
      glEnableVertexAttribArray(1)
      glEnableVertexAttribArray(2)
      glEnableVertexAttribArray(3)

      glDrawElements(GL_TRIANGLES,mesh.elemCount,GL_UNSIGNED_INT,0)

      glDisableVertexAttribArray(0)
      glDisableVertexAttribArray(1)
      glDisableVertexAttribArray(2)
      glDisableVertexAttribArray(3)
      mesh.unbindVAO()
    }
    shader.unbind()
  }

  private def getFloatBuffer(spat: SpatialComponent): Matrix4 = {
    val mat: Matrix4 = Matrix4()
    mat.scale(spat.scale)
    mat.translate(spat.position)
    mat * spat.orientation.rotationMatrix()
  }

}
