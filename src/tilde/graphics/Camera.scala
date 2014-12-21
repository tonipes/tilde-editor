package tilde.graphics

import org.lwjgl.util.vector.{Matrix4f, Quaternion, Vector3f}
import tilde.Entity.Component.SpatialComponent
import tilde.Entity.Entity
import tilde.util.MatrixUtil

object Camera{

}

class Camera(zFar: Float, zNear: Float, aspect: Float, fov: Float) extends Entity {

  private val projectionMatrix = MatrixUtil.createPerspectiveProjection(zFar, zNear, aspect, fov)
  Quaternion
}
