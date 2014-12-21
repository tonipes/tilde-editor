package tilde.Entity

import tilde.Entity.Component._
import scala.collection.mutable.Map
/**
 * Created by Toni on 21.12.14.
 */
class Entity {
  val components = Map[Class[_ <: Component], Component]()

  def addComponent[T <: Component](component: T) = {
    components(component.getClass()) = component
  }

  def removeComponent[T <: Component](component: Class[T]) = {
    components.remove(component)
  }

  def getComponent[T <: Component](componentClass: Class[T]) = {
    if (components.contains(componentClass))
      Some(components(componentClass).asInstanceOf[T])
    else
      None
  }
}
