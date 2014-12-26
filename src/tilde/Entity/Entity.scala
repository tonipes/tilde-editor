package tilde.entity

import tilde.entity.aspect.Aspect
import tilde.entity.component._
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

  def getComponent[T <: Component](componentClass: Class[T]): Option[T] = {
    if (components.contains(componentClass))
      Some(components(componentClass).asInstanceOf[T])
    else
      None
  }

  def checkAspect(a: Aspect): Boolean = {
    a.components.forall(p => components.contains(p))
  }

  def dispose() {

  }
}
