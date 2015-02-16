package sgEngine

/**
 * Created by Toni on 17.1.2015.
 */
import sgEngine.util._

object Component{
  val spatial = classOf[SpatialComponent]
  val model   = classOf[ModelComponent]
  val light   = classOf[LightSourceComponent]
  val input   = classOf[InputComponent]

  def getID[T <: Component](component: T): Int = getID(component.getClass)

  def getID[T <: Component](component: Class[T]): Int =
    component match {
      case q if q == classOf[SpatialComponent]     => 1
      case q if q == classOf[ModelComponent]       => 2
      case q if q == classOf[LightSourceComponent] => 3
      case q if q == classOf[InputComponent]       => 4
  }
}

abstract class Component extends Product{
  def getID = Component.getID(this)
}

case class SpatialComponent(
  position:    Vec3       = Vec3(0,0,0),
  orientation: Quaternion = Quaternion(0,0,0,1),
  scale:       Vec3       = Vec3(1,1,1) ) extends Component {

  def move(direction: Vec3, amount: Float): Unit = {
    val v = Vec3(direction).normalise()
    v *= amount
    position += v
  }

  def forward(): Vec3 = orientation.getForward()
  def up()     : Vec3 = orientation.getUp()
  def right()  : Vec3 = orientation.getRight()
}

case class ModelComponent(model: String = "") extends Component

case class LightSourceComponent(
  ambient:      Vec4  = Vec4(1,1,1,1),
  diffuse:      Vec4  = Vec4(1,1,1,1),
  specular:     Vec4  = Vec4(1,1,1,1),
  constAtten:   Float = 1.0f,
  linearAtten:  Float = 1.0f,
  quadratAtten: Float = 1.0f ) extends Component

case class InputComponent() extends Component