package tilde.entity.system

import tilde.entity.{World, Entity}
import tilde.log.Log
import tilde.util.Aspect

import scala.collection.mutable._

/**
 * Created by Toni on 23.12.14.
 */
abstract class EntitySystem() {
  var aspect: Aspect = null // If null, system is not interested in any entities, see logicSystem
  var world: World = null
  var entities = Buffer[Entity]()
  var started = false

  /**
   * Processes all entities in systems entity list
   */
  def processEntities(delta: Float): Unit = {
    if(!started) {
      Log.error("Illegal system call", "System must be started before processing entities")
      throw new IllegalStateException("System must be started before processing entities")
    }
    else{
      for(e <- entities){
        process(e, delta)
      }
    }
  }

  /**
   * Checks if system is interested in entity by comparing aspect bitsets
   * If system is interested, entity will be added to systems entity list
   * @param e Entity to check
   */
  def checkInterest(e: Entity): Unit = {
    if(aspect != null) { // if aspect is null, system is not interested in any entities
      val contains = entities.contains(e)
      val intrest = this.isIntrestedIn(e)
      if (contains && !intrest) removeEntity(e)
      if (!contains && intrest) addEntity(e)
    }
  }

  /**
   * Removes entity from systems entity list if found
   * @param e Entity to remove
   */
  def removeEntity(e: Entity): Unit  = {
    val index = entities.indexOf(e)
    if(index >= 0){
      entities.remove(index)
    }
  }


  private def addEntity(e: Entity) = {
    entities += e
  }

  private def isIntrestedIn (e: Entity) = this.aspect.machesWith(e.componentStructure)

  /**
   * Processes single entity
   * @param e Entity to process
   */
  def process(e: Entity, delta: Float): Unit

  final def begin(): Unit = {
    if (started) {
      Log.error("Illegal system call", "System must be ended before starting it again")
      throw new IllegalStateException("System must be ended before starting it again")
    } else {
      started = true
      systemBegin()
    }
  }

  final def end(): Unit = {
    if(!started){
      Log.error("Illegal system call", "System must be started before end")
      throw new IllegalStateException("System must be started before end")
    } else {
      started = false
      systemEnd()
    }
  }

  /**
   * Call this before processing entities
   */
  def systemBegin(): Unit = {}

  /**
   * Call this after processing entities
   */
  def systemEnd(): Unit = {}

  override def toString() ={
    "EntitySystem"
  }
}
