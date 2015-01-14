/*
package tilde.entity.component

import java.nio.FloatBuffer

import org.lwjgl.BufferUtils
import org.lwjgl.util.vector.Matrix4f
import tilde.entity.{ComponentObject, Component}
import tilde.util.MatrixUtil

/**
 * Created by Toni on 26.12.14.
 */
object CameraComponent extends ComponentObject{
  val id = classOf[CameraComponent]
}
class CameraComponent(projection: Matrix4f) extends Component {
  val bitId: Int = CameraComponent.bitID

  var projectionMatrix: Matrix4f = projection

  val viewBuffer: FloatBuffer = BufferUtils.createFloatBuffer(16)
  val projectionBuffer: FloatBuffer = BufferUtils.createFloatBuffer(16)

  projectionMatrix.store(projectionBuffer)
  projectionBuffer.rewind()


}
*/
