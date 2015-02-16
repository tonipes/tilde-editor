package sgEngine

import scala.collection.mutable
import scala.collection.immutable.BitSet
class Entity(val world: World, compData: Vector[Component]) {
  val id: Int = 0

  val components = mutable.Map[Class[_ <: Component], Component]()
  var compStruct: BitSet = BitSet.empty

  var changed = false

  compData.foreach(c =>{
    components(c.getClass) = c
    compStruct = compStruct + c.getID
  })
  world.changed(this)
  //println(s"entity created $compStruct. Comps $components ")

  /**
   * Adds component to entity
   * @param comp Component to add
   * @tparam T Type of the component
   */
  def addComponent[T <: Component](comp: T): Unit = {
    components(comp.getClass) = comp
    compStruct = compStruct + Component.getID(comp.getClass)
    world.changed(this)
  }

  /**
   * Removes component from entity
   * @param comp Class of component to remove
   * @tparam T
   */
  def removeComponent[T <: Component](comp: Class[T]): Unit = {
    components.remove(comp)
    compStruct = compStruct - Component.getID(comp)
    world.changed(this)
  }

  /**
   * Gets component from entity
   * @param comp Class of component
   * @tparam T
   * @return Component
   */
  def getComponent[T <: Component](comp: Class[T]): Option[T] = {
    if(components.contains(comp)){
      Some(components(comp).asInstanceOf[T])
    } 
    else None 
  }

  /**
   * Returns all tags attached to this entity
   * @return
   */
  def getTags = world.getTags(this)

  /**
   * Returns Entity's data
   * @return
   */
  def getEntityData = EntityData(this)

  /**
   * Dispose
   * @return
   */
  def dispose() = {
    ???
  }

}

object EntityData{
  def apply(e:Entity): EntityData ={
    val comps = e.components.values.toVector
    val tags = e.getTags
    EntityData(comps,tags)
  }
}

/**
 * EntityData holds all data for single entity.
 * EntityData is used for serialization.
 * @param components Entity's components
 * @param tags Entity's tags
 */
case class EntityData(components: Vector[Component], tags: Vector[String]) extends Serializable
