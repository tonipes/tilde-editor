package tilde.entity.component

import org.lwjgl.BufferUtils
import org.lwjgl.util.vector.{Vector4f, Matrix4f, Quaternion, Vector3f}
import tilde.entity.{ComponentObject, Component}
import tilde.util.Direction._
import tilde.util.{Direction, QuaternionUtil}

/**
 * Created by Toni on 23.12.14.
 */
object SpatialComponent extends ComponentObject{
  val id = classOf[SpatialComponent]
}

class SpatialComponent extends Component {
  val bitId = SpatialComponent.bitID

  private val position: Vector3f = new Vector3f(0,0,0)
  private val orientation: Quaternion = new Quaternion().setIdentity()
  private val scale: Vector3f = new Vector3f(1,1,1)

  val matrix: Matrix4f = new Matrix4f()

  val transFloatBuffer = BufferUtils.createFloatBuffer(16)

  def up(): Vector3f = QuaternionUtil.getUp(orientation)
  def right(): Vector3f = QuaternionUtil.getRight(orientation)
  def forward(): Vector3f = QuaternionUtil.getForward(orientation)

  def rotate(x: Float, y: Float, z: Float): Unit  = {
    rotateX(x)
    rotateY(y)
    rotateZ(z)
  }

  def rotateX(angle: Float) = rotate(angle,AXIS_X)
  def rotateY(angle: Float) = rotate(angle,AXIS_Y)
  def rotateZ(angle: Float) = rotate(angle,AXIS_Z)

  def move(dir: Direction,amount: Float): Unit =
    dir match {
    case Direction.FORWARD => move(forward(),amount)
    case Direction.BACKWARD => move(forward(),-amount)
    case Direction.RIGHT => move(right(),amount)
    case Direction.LEFT => move(right(),-amount)
    case Direction.UP => move(QuaternionUtil.getUp(orientation),amount)
    case Direction.DOWN => move(QuaternionUtil.getUp(orientation),-amount)
  }

  def rotate(angle: Float, axis: Vector3f): Unit = {
    val xRot = QuaternionUtil.quatFromAxis(axis, angle)
    Quaternion.mul(xRot, orientation,orientation)
    updateMatrix()
  }

  def scale(vec: Vector3f): Unit = {
    scale.x = scale.x * vec.x
    scale.y = scale.y * vec.y
    scale.z = scale.z * vec.z
    updateMatrix()
  }
  def move(vec: Vector3f, amount: Float): Unit = {
    val v = new Vector3f(vec)
    v.normalise()
    v.scale(amount)
    Vector3f.add(position, v, position)
    updateMatrix()
  }

  def getPosition = position
  def getOrientation = orientation
  def getScale = scale

  def setPosition(vec: Vector3f): Unit = setPosition(vec.x,vec.y,vec.z)

  def setPosition(x: Float, y: Float, z: Float): Unit  = {
    position.set(x,y,z)
    updateMatrix()
  }

  def setScale(vec: Vector3f): Unit  = setScale(vec.x,vec.y,vec.z)
  def setScale(x: Float, y: Float, z: Float): Unit  = {
    scale.set(x,y,z)
    updateMatrix()
  }

  def setOrientation(vec: Vector4f): Unit  = setOrientation(vec.x,vec.y,vec.z,vec.w)
  def setOrientation(x: Float, y: Float ,z: Float, w: Float): Unit  = {
    orientation.set(x,y,z,w)
    orientation.normalise()
    updateMatrix()
  }

  private def updateMatrix(): Matrix4f = {
    matrix.setIdentity()
    matrix.scale(scale)
    matrix.translate(position)
    Matrix4f.mul(matrix, QuaternionUtil.rotationMatrix(orientation),matrix)
  }

  def getMatrix: Matrix4f = matrix

  def getFloatBuffer = {
    matrix.store(transFloatBuffer)
    transFloatBuffer.rewind()
    transFloatBuffer
  }

}
