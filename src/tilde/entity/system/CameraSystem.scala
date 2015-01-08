package tilde.entity.system

import org.lwjgl.input.Keyboard
import org.lwjgl.util.vector.Matrix4f
import tilde.Input
import tilde.entity.{World, Entity}
import tilde.entity.component.{CameraComponent, SpatialComponent}
import tilde.log.Log
import tilde.util.{Aspect, QuaternionUtil, SystemUtil}

/**
 * Created by Toni on 26.12.14.
 */

class CameraSystem() extends EntitySystem(){
  this.aspect = new Aspect(Array(SpatialComponent,CameraComponent))//SystemUtil.createAspect(SpatialComponent,CameraComponent)

  override def process(e: Entity, delta: Float): Unit = {
    // update camera viewMatrix and projection matrix
    val spatial = e.getComponent(SpatialComponent.id).get
    val camera = e.getComponent(CameraComponent.id).get

    val viewMatrix = QuaternionUtil.rotationMatrix(spatial.getOrientation)
    Matrix4f.translate(spatial.getPosition.negate(null), viewMatrix, viewMatrix)
    viewMatrix.store(camera.viewBuffer)
    camera.viewBuffer.rewind()
  }

  override def systemBegin(): Unit = {
    //Log.debug("System stared", "CameraSystem")
  }

  override def systemEnd(): Unit = {
    //Log.debug("System ended", "CameraSystem")
  }

  override def toString() ={
    "CameraSystem"
  }
}
