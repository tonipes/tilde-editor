package tilde.entity

import java.nio.FloatBuffer

import org.lwjgl.BufferUtils
import org.lwjgl.util.vector.{Matrix4f, Quaternion, Vector3f, Vector4f}
import tilde.ResourceManager
import tilde.util.Direction.Direction
import tilde.util.{Direction, QuaternionUtil}

/**
 * Created by Toni on 21.12.14.
 */

abstract class Component extends Product {
  def bitId: Int
}

trait ComponentObject{
  val id: Class[_ <: Component]
  val bitID: Int = getID(id)

  /* This holds bitIds for all components. Most used components should have lowest ids
  *  this id is used in bitmasking (see Aspect and entity componentStructure)
  */
  def getID(id: Class[_ <: Component]): Int = id match{
    case SpatialComponent.id      => 1
    case ModelComponent.id        => 2
    case CameraComponent.id       => 3
    case PhysicsComponent.id      => 4
    case LightSourceComponent.id  => 5
  }
}

object TestComponent extends ComponentObject{
  val id = classOf[TestComponent]
}

case class TestComponent(position: Vector3f,orientation: Quaternion,scale: Vector3f) extends Component{
  def bitId = TestComponent.bitID
}

object PhysicsComponent extends ComponentObject{
  val id = classOf[PhysicsComponent]
}

case class PhysicsComponent(speed: Vector3f         = new Vector3f(0,0,0),
                            angularSpeed: Vector3f  = new Vector3f(0,0,0)) extends Component{
  def bitId = PhysicsComponent.bitID
}


object ModelComponent extends ComponentObject{
  val id = classOf[ModelComponent]
}
case class ModelComponent(model: String) extends Component{
  def bitId = ModelComponent.bitID

  def getMaterial() = ResourceManager.models(model).material
  def getMesh()     = ResourceManager.meshes(model)
}

object CameraComponent extends ComponentObject{
  val id = classOf[CameraComponent]
}
case class CameraComponent(projection: Matrix4f) extends Component{
  def bitId = CameraComponent.bitID

  val viewBuffer: FloatBuffer = BufferUtils.createFloatBuffer(16)
  val projectionBuffer: FloatBuffer = BufferUtils.createFloatBuffer(16)

  projection.store(projectionBuffer)
  projectionBuffer.rewind()

}


object SpatialComponent extends ComponentObject{
  val id = classOf[SpatialComponent]
}

case class SpatialComponent(position: Vector3f,
                            orientation: Quaternion,
                            scale: Vector3f) extends Component{
  def bitId = SpatialComponent.bitID

  val matrix: Matrix4f = new Matrix4f()

  val transFloatBuffer = BufferUtils.createFloatBuffer(16)

  def up(): Vector3f      = QuaternionUtil.getUp(orientation)
  def right(): Vector3f   = QuaternionUtil.getRight(orientation)
  def forward(): Vector3f = QuaternionUtil.getForward(orientation)

  def rotate(x: Float, y: Float, z: Float): Unit  = {
    rotateX(x)
    rotateY(y)
    rotateZ(z)
  }

  def rotateX(angle: Float) = rotate(angle,Direction.AXIS_X)
  def rotateY(angle: Float) = rotate(angle,Direction.AXIS_Y)
  def rotateZ(angle: Float) = rotate(angle,Direction.AXIS_Z)

  def move(dir: Direction,amount: Float): Unit =
    dir match {
      case Direction.FORWARD  => move(forward(),amount)
      case Direction.BACKWARD => move(forward(),-amount)
      case Direction.RIGHT    => move(right(),amount)
      case Direction.LEFT     => move(right(),-amount)
      case Direction.UP       => move(up(),amount)
      case Direction.DOWN     => move(up(),-amount)
    }

  def rotate(angle: Float, axis: Vector3f): Unit = {
    val xRot = QuaternionUtil.quatFromAxis(axis, angle)
    Quaternion.mul(xRot, orientation,orientation)
    //updateMatrix()
  }

  def scale(vec: Vector3f): Unit = {
    scale.x = scale.x * vec.x
    scale.y = scale.y * vec.y
    scale.z = scale.z * vec.z
    //updateMatrix()
  }
  def move(vec: Vector3f, amount: Float): Unit = {
    val v = new Vector3f(vec)
    v.normalise()
    v.scale(amount)
    Vector3f.add(position, v, position)
    //updateMatrix()
  }

  def getPosition = position
  def getOrientation = orientation
  def getScale = scale

  def setPosition(vec: Vector3f): Unit = setPosition(vec.x,vec.y,vec.z)

  def setPosition(x: Float, y: Float, z: Float): Unit  = {
    position.set(x,y,z)
    //updateMatrix()
  }

  def setScale(vec: Vector3f): Unit  = setScale(vec.x,vec.y,vec.z)
  def setScale(x: Float, y: Float, z: Float): Unit  = {
    scale.set(x,y,z)
    //updateMatrix()
  }

  def setOrientation(vec: Vector4f): Unit  = setOrientation(vec.x,vec.y,vec.z,vec.w)
  def setOrientation(x: Float, y: Float ,z: Float, w: Float): Unit  = {
    orientation.set(x,y,z,w)
    orientation.normalise()
    //updateMatrix()
  }

  private def updateMatrix(): Matrix4f = {
    matrix.setIdentity()
    matrix.scale(scale)
    matrix.translate(position)
    Matrix4f.mul(matrix, QuaternionUtil.rotationMatrix(orientation),matrix)
  }

  def getMatrix: Matrix4f = matrix

  def getFloatBuffer = {
    updateMatrix()
    matrix.store(transFloatBuffer)
    transFloatBuffer.rewind()
    transFloatBuffer
  }
}


object LightSourceComponent extends ComponentObject{
  val id = classOf[LightSourceComponent]
}
case class LightSourceComponent(ambient: Vector4f   = new Vector4f(1,1,1,1),
                                diffuse: Vector4f   = new Vector4f(1,1,1,1),
                                specular: Vector4f  = new Vector4f(1,1,1,1),
                                constAtten: Float   = 1.0f,
                                linearAtten: Float  = 1.0f,
                                quadratAtten: Float = 1.0f) extends Component {
  def bitId = LightSourceComponent.bitID
}
