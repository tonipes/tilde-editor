package tilde

import scala.collection.mutable
import scala.collection.immutable.BitSet

class Entity(val world: World) {
  val components = mutable.Map[Class[_ <: Component], Component]()
  var compStruct: BitSet = BitSet.empty

  def addComponent[T <: Component](component: T): Unit = {
    components(component.getClass) = component
    compStruct = compStruct + Component.getID(component.getClass)
   //world.changed(this)
  }
  def removeComponent[T <: Component](component: Class[T]): Unit = {
    components.remove(component)
    compStruct = compStruct - Component.getID(component)
    //world.changed(this)
  }

  def dispose() = {

  }

}