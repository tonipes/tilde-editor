package tilde.entity

import tilde.entity.component._
import scala.collection.mutable
import scala.collection.mutable.Map
/**
  * Created by Toni on 21.12.14.
 */
class Entity(val world: World) {
  val active: Boolean = true
  val components = Map[Class[_ <: Component], Component]()
  val componentStructure = new mutable.BitSet()

  def addComponent[T <: Component](component: T): Unit = {
    components(component.getClass()) = component
    componentStructure += component.bitId
    world.changed(this)
  }

  def removeComponent[T <: Component](component: Class[T]): Unit = {
    components.remove(component)
    componentStructure -= component.asInstanceOf[Component].bitId
    world.changed(this)
  }

  def getComponent[T <: Component](component: Class[T]): Option[T] = {
    if (components.contains(component))
      Some(components(component).asInstanceOf[T])
    else
      None
  }

  def dispose() {
    world.destroyEntity(this)
  }

  override def toString() = {
    "Entity" + this.hashCode() + " with " + components.keys.toString
  }

}
