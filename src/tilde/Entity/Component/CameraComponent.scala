package tilde.entity.component

import org.lwjgl.BufferUtils
import tilde.util.MatrixUtil

/**
 * Created by Toni on 26.12.14.
 */
object CameraComponent extends ComponentObject{
  val id = classOf[CameraComponent]
}
class CameraComponent(zFar: Float, zNear: Float, aspect: Float, fov: Float) extends Component {
  val bitId: Int = CameraComponent.bitID

  var projectionMatrix = MatrixUtil.createPerspectiveProjection(zFar, zNear, aspect, fov)

  val viewBuffer = BufferUtils.createFloatBuffer(16)
  val projectionBuffer = BufferUtils.createFloatBuffer(16)

  projectionMatrix.store(projectionBuffer)
  projectionBuffer.rewind()


}
