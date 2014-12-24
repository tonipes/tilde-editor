package tilde.entity.component

import org.lwjgl.BufferUtils
import org.lwjgl.util.vector.{Matrix4f, Quaternion, Vector3f}
import tilde.util.Direction._
import tilde.util.{Direction, QuaternionUtil}

/**
 * Created by Toni on 23.12.14.
 */
object SpatialComponent{
  val id = classOf[SpatialComponent]
}
class SpatialComponent extends Component {
  private val position: Vector3f = new Vector3f(0,0,0)
  private val orientation: Quaternion = new Quaternion().setIdentity()
  private val scale: Vector3f = new Vector3f(1,1,1)

  private val matrix: Matrix4f = new Matrix4f()

  private val transFloatBuffer = BufferUtils.createFloatBuffer(16)

  def up: Vector3f = QuaternionUtil.getUp(orientation)
  def right: Vector3f = QuaternionUtil.getRight(orientation)
  def forward: Vector3f = QuaternionUtil.getForward(orientation)

  def rotate(x:Float,y:Float,z:Float): Unit  = {
    rotateX(x)
    rotateY(y)
    rotateZ(z)
  }

  def rotateX(angle: Float) = rotate(angle,AXIS_X)
  def rotateY(angle: Float) = rotate(angle,AXIS_Y)
  def rotateZ(angle: Float) = rotate(angle,AXIS_Z)

  def move(dir: Direction,amount: Float): Unit = dir match {
    case Direction.FORWARD => move(forward,amount)
    case Direction.BACKWARD => move(forward,-amount)
    case Direction.RIGHT => move(right,amount)
    case Direction.LEFT => move(right,-amount)
    case Direction.UP => move(up,amount)
    case Direction.DOWN => move(up,-amount)
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

  def setPosition(vec: Vector3f) = position.set(vec)
  def setPosition(x:Float,y:Float,z:Float) = position.set(x,y,z)

  def setScale(vec: Vector3f) = scale.set(vec)
  def setScale(x:Float,y:Float,z:Float) = scale.set(x,y,z)

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
