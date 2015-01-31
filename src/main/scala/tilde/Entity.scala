package tilde

import scala.collection.mutable
import scala.collection.immutable.BitSet

class Entity(val world: World) {
  val id: Int = 0
  val components = mutable.Map[Class[_ <: Component], Component]()
  var compStruct: BitSet = BitSet.empty

  def addComponent[T <: Component](comp: T): Unit = {
    components(comp.getClass) = comp
    compStruct = compStruct + Component.getID(comp.getClass)
    world.changed(this)
  }
  
  def removeComponent[T <: Component](comp: Class[T]): Unit = {
    components.remove(comp)
    compStruct = compStruct - Component.getID(comp)
    world.changed(this)
  }

  def getComponent[T <: Component](comp: Class[T]): Option[T] = {
    if(components.contains(comp)){
      Some(components(comp).asInstanceOf[T])
    } 
    else None 
  }

  def dispose() = {

  }

}