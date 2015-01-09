package tilde.entity

import tilde.entity.component._
/**
 * Created by Toni on 21.12.14.
 */

abstract class Component {
  val bitId: Int
}

trait ComponentObject{
  val id: Class[_ <: Component]
  val bitID: Int = getID(id)

  /* This holds bitIds for all components. Most used components should have lowest ids
  *  this id is used in bitmasking (see Aspect and entity componentStructure)
  * */
  def getID(id: Class[_ <: Component]): Int = id match{
    case SpatialComponent.id => 1
    case ModelComponent.id => 2
    case CameraComponent.id => 3
    case PhysicsComponent.id => 4
    case LightSourceComponent.id => 5
  }
}

