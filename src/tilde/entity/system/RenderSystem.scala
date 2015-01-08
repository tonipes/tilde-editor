package tilde.entity.system

import java.nio.FloatBuffer

import org.lwjgl.BufferUtils
import tilde.ResourceManager
import tilde.log._
import tilde.entity.{World, Entity}
import tilde.entity.component.{LightSourceComponent, CameraComponent, ModelComponent, SpatialComponent}
import tilde.graphics.ShaderProgram
import tilde.util.{BufferCreatorUtil, Aspect, SystemUtil}
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._
import scala.collection.mutable._

/**
 * Created by Toni on 25.12.14.
 */
class RenderSystem() extends EntitySystem() {
  this.aspect = new Aspect(
    Array(ModelComponent,SpatialComponent),       // Renderable Entities
    Array(LightSourceComponent,SpatialComponent)  // Lights
  )

  lazy val shader: ShaderProgram = ResourceManager.shaderPrograms("default")

  var camera: Entity = null

  // Multimap for all different instances to render
  //val batches = new HashMap[ModelComponent, Set[FloatBuffer]] with MultiMap[ModelComponent, FloatBuffer]
  val ents = Buffer[Entity]()
  val lights = Buffer[Entity]()

  override def process(e: Entity, delta: Float): Unit = {
    if(e.getComponent(LightSourceComponent.id).isDefined){
      lights += e
    }
    if(e.getComponent(ModelComponent.id).isDefined){
      ents += e
    }
  }

  override def systemBegin(): Unit = {
    shader.bind()
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
    val cam = world.getTagged("camera")

    if(cam.isDefined){
      camera = cam.get
    } else {
      Log.error("Rendering error", "No camera found in world!")
    }
  }

  // TODO: Fix super bad code plz! Use proper batching and instansing
  override def systemEnd(): Unit = {
    // Render all batches
    //Log.debug("Rendering","" + ents.length + " entities")

    val cameraComp = camera.getComponent(CameraComponent.id).get
    val cameraPos = camera.getComponent(SpatialComponent.id).get.getPosition

    val light = lights(1)
    val lightSpat = light.getComponent(SpatialComponent.id).get
    val lightLight = light.getComponent(LightSourceComponent.id).get

    //val lightBuf = BufferUtil.createFloatBuffer(lightSpat.getPosition)
    //Log.debug("lightBuff","" + lightBuf.get(0) + ", " + lightBuf.get(1) + ", " + lightBuf.get(2) + ", ")

    shader.setUniform("c_position",cameraPos.x,cameraPos.y,cameraPos.z)
    shader.setUniform("l_position",lightSpat.getPosition.x,lightSpat.getPosition.y,lightSpat.getPosition.z)
    shader.setUniform("l_color",lightLight.ambient.x,lightLight.ambient.y,lightLight.ambient.z,lightLight.ambient.w)
    shader.setUniform("m_view", cameraComp.viewBuffer)
    shader.setUniform("m_proj", cameraComp.projectionBuffer)

    //Log.debug("LightCount","" + lights.length)
    for(ent <- ents){
      val model = ent.getComponent(ModelComponent.id).get
      val transformation = ent.getComponent(SpatialComponent.id).get.getFloatBuffer
      shader.setUniform("m_model", transformation)

      model.getTexture.setActiveAsUnit(0)
      model.getTexture.bind()

      model.getMesh.bindVAO()
      glEnableVertexAttribArray(0)
      glEnableVertexAttribArray(1)
      glEnableVertexAttribArray(2)
      glDrawElements(GL_TRIANGLES,model.getMesh.elemCount,GL_UNSIGNED_INT,0)

      glDisableVertexAttribArray(0)
      glDisableVertexAttribArray(1)
      glDisableVertexAttribArray(2)
      model.getMesh.unbindVAO()
    }
    shader.unbind()
    ents.clear()
    lights.clear()
  }

  private def loadLights(): Unit =  {
    def ambientString(i : Int) = "lights["+i+"].ambient"
    def diffuseString(i : Int) = "lights["+i+"].diffuse"
    def specularString(i : Int) = "lights["+i+"].specular"
    def constantAttenuationString(i : Int) = "lights["+i+"].constantAttenuation"
    def linearAttenuationString(i : Int) = "lights["+i+"].linearAttenuation"
    def quadraticAttenuationString(i : Int) = "lights["+i+"].quadraticAttenuation"

    var count = 0;
    for(l <- lights){
      val lightComponent = l.getComponent(LightSourceComponent.id).get
      val amb = lightComponent.ambient
      val dif = lightComponent.diffuse
      val spec = lightComponent.specular
      val linAt = lightComponent.linearAttenuation
      val quAt = lightComponent.quadraticAttenuation
      val conAt = lightComponent.constantAttenuation

      shader.setUniform(ambientString(count),amb.x,amb.y,amb.z,amb.w)
      shader.setUniform(diffuseString(count),dif.x,dif.y,dif.z,dif.w)
      shader.setUniform(specularString(count),spec.x,spec.y,spec.z,spec.w)
      shader.setUniform(constantAttenuationString(count),conAt)
      shader.setUniform(linearAttenuationString(count),linAt)
      shader.setUniform(quadraticAttenuationString(count),quAt)
    }
  }

  override def toString() ={
    "RenderSystem"
  }
}
