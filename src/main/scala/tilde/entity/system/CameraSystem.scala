package tilde.entity.system

import org.lwjgl.util.vector.Matrix4f
import tilde.entity.{Entity, EntitySystem, SpatialComponent, _}
import tilde.util.{Aspect, QuaternionUtil}

/**
 * Created by Toni on 26.12.14.
 */

class CameraSystem() extends EntitySystem(){
  this.aspect = new Aspect(Vector(SpatialComponent,CameraComponent))

  override def process(e: Entity, delta: Float): Unit = {
    // update camera viewMatrix and projection matrix
    val spatial = e.getComponent(SpatialComponent.id).get
    val camera = e.getComponent(CameraComponent.id).get

    val viewMatrix = QuaternionUtil.rotationMatrix(spatial.orientation)
    Matrix4f.translate(spatial.position.negate(null), viewMatrix, viewMatrix)
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
