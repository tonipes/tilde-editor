package tilde.graphics

import org.lwjgl.BufferUtils
import org.lwjgl.util.vector.{Matrix4f, Quaternion, Vector3f}
import tilde.Entity.Entity
import tilde.util.Direction.Direction
import tilde.util.{QuaternionUtil, MatrixUtil}

object Camera{

}

class Camera(zFar: Float, zNear: Float, aspect: Float, fov: Float) extends Entity {

  var projectionMatrix = MatrixUtil.createPerspectiveProjection(zFar, zNear, aspect, fov)
  //var projectionMatrix = MatrixUtil.createOrthographicProjection(zFar,zNear,1,-1,1,-1)
  var viewMatrix = new Matrix4f()

  private var position: Vector3f = new Vector3f()
  private val orientation: Quaternion = new Quaternion()

  private val viewBuffer = BufferUtils.createFloatBuffer(16)
  private val projectionBuffer = BufferUtils.createFloatBuffer(16)

  projectionMatrix.store(projectionBuffer)
  projectionBuffer.rewind()

  //def up: Vector3f = QuaternionUtil.getUpFromQuat(orientation)//new Vector3f(0, 1, 0)
  //def right: Vector3f = QuaternionUtil.getRightFromQuat(orientation)//new Vector3f(1, 0, 0)
  //def forward: Vector3f = QuaternionUtil.getForwardFromQuat(orientation)// new Vector3f(new Vector3f(0, 0, 1).negate(null))

  var up: Vector3f = new Vector3f(0, 1, 0)
  var right: Vector3f = new Vector3f(1, 0, 0)
  var forward: Vector3f = new Vector3f(new Vector3f(0, 0, 1).negate(null))

  def rotateX(angle: Float) = {   //rotate(angle,new Vector3f(1,0,0))
    val xRot = QuaternionUtil.quatFromAxis(new Vector3f(1,0,0), angle);
    Quaternion.mul(xRot, orientation, orientation);

    up = QuaternionUtil.rotateByQuat(up, xRot)
    forward = QuaternionUtil.rotateByQuat(forward, xRot)

    up.normalise()
    forward.normalise()
  }

  def rotateY(angle: Float) = {   //rotate(angle,new Vector3f(0,1,0))
    val yRot = QuaternionUtil.quatFromAxis(new Vector3f(0,1,0), angle);
    Quaternion.mul(yRot, orientation, orientation);

    right = QuaternionUtil.rotateByQuat(up, yRot)
    forward = QuaternionUtil.rotateByQuat(forward, yRot)

    right.normalise()
    forward.normalise()
  }

  def rotateZ(angle: Float) = {   //rotate(angle,new Vector3f(0,0,1))
    val zRot = QuaternionUtil.quatFromAxis(new Vector3f(0,0,1), angle);
    Quaternion.mul(zRot, orientation, orientation);

    up = QuaternionUtil.rotateByQuat(up, zRot)
    right = QuaternionUtil.rotateByQuat(forward, zRot)

    up.normalise()
    right.normalise()
  }

  def rotate(angle: Float, axis: Vector3f) = {
    val xRot = QuaternionUtil.quatFromAxis(axis, angle)
    Quaternion.mul(xRot, orientation,orientation)
  }

  def move(dir: Direction,amount: Float) = {
    ???
  }

  def move(vec: Vector3f, amount: Float) = {
    val v = new Vector3f(vec)
    v.normalise()
    v.scale(amount)
    Vector3f.add(position, v, position)
  }

  def update() = {
    viewMatrix = QuaternionUtil.quatToRotationMatrix(orientation)
    Matrix4f.translate(position.negate(null), viewMatrix, viewMatrix)

    // Store the view matrix in the buffer
    viewMatrix.store(viewBuffer)
    viewBuffer.rewind()
  }

  def getViewBuffer = {
    val view = QuaternionUtil.quatToRotationMatrix(orientation)
    Matrix4f.translate(position.negate(null), view, view)
    viewBuffer
  }

  def getProjectionBuffer = projectionBuffer

  def setPosition(pos: Vector3f) = {
    this.position = pos
  }
}
