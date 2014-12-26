package tilde.entity.component

import org.lwjgl.BufferUtils
import org.lwjgl.util.vector.Matrix4f
import tilde.util.{QuaternionUtil, MatrixUtil}

/**
 * Created by Toni on 26.12.14.
 */
object CameraComponent extends ComponentObject{
  val id = classOf[CameraComponent]
}
class CameraComponent(zFar: Float, zNear: Float, aspect: Float, fov: Float) extends Component {
  override val bitId: Int = CameraComponent.bitID

  var projectionMatrix = MatrixUtil.createPerspectiveProjection(zFar, zNear, aspect, fov)

  val viewBuffer = BufferUtils.createFloatBuffer(16)
  val projectionBuffer = BufferUtils.createFloatBuffer(16)

  projectionMatrix.store(projectionBuffer)
  projectionBuffer.rewind()

  /*
  var projectionMatrix = MatrixUtil.createPerspectiveProjection(zFar, zNear, aspect, fov)
  var viewMatrix = new Matrix4f()

  val viewBuffer = BufferUtils.createFloatBuffer(16)
  val projectionBuffer = BufferUtils.createFloatBuffer(16)

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
*/

}
