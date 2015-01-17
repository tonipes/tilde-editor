package tilde

/**
 * Created by Toni on 17.1.2015.
 */
import tilde.util._

object Component{
  def getID[T <: Component](component: T): Int = getID(component.getClass)

  def getID[T <: Component](component: Class[T])=
    component match {
      case q if q == classOf[SpatialComponent]     => 1
      case q if q == classOf[ModelComponent]       => 2
      case q if q == classOf[LightSourceComponent] => 3

  }
}

abstract class Component extends Product{
  def getID = Component.getID(this)
}

case class SpatialComponent(position:    Vec3       = Vec3(0,0,0),
                            orientation: Quaternion = Quaternion(0,0,0,1),
                            scale:       Vec3       = Vec3(1,1,1) ) extends Component

case class ModelComponent(model: String = "") extends Component

case class CameraComponent(projection: Matrix4) extends Component

case class LightSourceComponent(ambient:      Vec4  = Vec4(1,1,1,1),
                                diffuse:      Vec4  = Vec4(1,1,1,1),
                                specular:     Vec4  = Vec4(1,1,1,1),
                                constAtten:   Float = 1.0f,
                                linearAtten:  Float = 1.0f,
                                quadratAtten: Float = 1.0f ) extends Component