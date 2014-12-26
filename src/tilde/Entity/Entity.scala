package tilde.entity

import tilde.entity.aspect.Aspect
import tilde.entity.component._
import scala.collection.mutable
import scala.collection.mutable.Map
/**
 * Created by Toni on 21.12.14.
 */
class Entity(val world: World) {
  val active: Boolean = true
  val components = Map[Class[_ <: Component], Component]()
  val aspect = new mutable.BitSet()

  def addComponent[T <: Component](component: T) = {
    components(component.getClass()) = component
    aspect += component.bitId
    world.changed(this)
  }

  def removeComponent[T <: Component](component: Class[T]) = {
    components.remove(component)
    aspect -= component.asInstanceOf[Component].bitId
    world.changed(this)
  }

  def getComponent[T <: Component](componentClass: Class[T]): Option[T] = {
    if (components.contains(componentClass))
      Some(components(componentClass).asInstanceOf[T])
    else
      None
  }

  def dispose() {
    world.destroyEntity(this)
  }
}
