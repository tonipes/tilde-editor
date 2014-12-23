package tilde.graphics

import java.util.spi.TimeZoneNameProvider

import org.lwjgl.BufferUtils
import org.lwjgl.util.vector.{Matrix4f, Quaternion, Vector3f}
import tilde.entity.Entity
import tilde.entity.component.SpatialComponent
import tilde.util.Direction._
import tilde.util.{Direction, QuaternionUtil, MatrixUtil}

object Camera{
  object Projection extends Enumeration{
    type Projection = Value
    val PERSPECTIVE, ORTHOGRAPHIC = Value
  }
}

class Camera(zFar: Float, zNear: Float, aspect: Float, fov: Float) extends Entity {

  this.addComponent(new SpatialComponent())

  private val spatial = this.getComponent(SpatialComponent.id).get

  private def orientation =  spatial.getOrientation
  private def position = spatial.getPosition

  var projectionMatrix = MatrixUtil.createPerspectiveProjection(zFar, zNear, aspect, fov)
  var viewMatrix = new Matrix4f()

  private val viewBuffer = BufferUtils.createFloatBuffer(16)
  private val projectionBuffer = BufferUtils.createFloatBuffer(16)

  projectionMatrix.store(projectionBuffer)
  projectionBuffer.rewind()

  def update() = {
    viewMatrix = QuaternionUtil.rotationMatrix(orientation)
    Matrix4f.translate(position.negate(null), viewMatrix, viewMatrix)

    // Store the view matrix in the buffer
    viewMatrix.store(viewBuffer)
    viewBuffer.rewind()
  }

  def getViewBuffer = {
    val view = QuaternionUtil.rotationMatrix(orientation)
    Matrix4f.translate(position.negate(null), view, view)
    viewBuffer
  }

  def getProjectionBuffer = projectionBuffer

}
