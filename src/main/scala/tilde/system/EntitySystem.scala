package tilde.systems

import tilde._
import scala.collection.immutable.BitSet
import scala.collection.mutable._

abstract class EntitySystem() {
  var world: World = null
  var compStruct: BitSet
  protected var entities = Buffer[Entity]()
  private var started = false

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
   * Checks if system is interested in entity by comparing component structure bitsets
   * If system is interested, entity will be added to systems entity list
   * @param e Entity to check
   */
  def checkInterest(e: Entity): Unit = {
    // if compStruct is empty, system is not interested in any entities
    if(!compStruct.isEmpty) {
      val contains = entities.contains(e)
      val interest = this.isIntrestedIn(e)
      if (contains && !interest) removeEntity(e)
      if (!contains && interest) addEntity(e)
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

  /**
   * Adds entity to systems entity list
   * @param e Entity add
   */
  private def addEntity(e: Entity): Unit = {
    entities += e
  }

  /**
   * Checks if system is interested in entity
   * @param e Entity to check
   */
  private def isIntrestedIn(e: Entity): Boolean = 
    (this.compStruct & e.compStruct) == this.compStruct

  /**
   * Processes single entity
   * @param e Entity to process
   */
  protected def process(e: Entity, delta: Float): Unit

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
}