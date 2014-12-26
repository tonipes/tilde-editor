package tilde.entity.system

import tilde.entity.{World, Entity}
import tilde.log.Log

import scala.collection.mutable._

/**
 * Created by Toni on 23.12.14.
 */
abstract class EntitySystem(val aspect: BitSet) {
  var world: World = null
  var entities = Buffer[Entity]()
  var started = false

  def processEntities(): Unit ={

    for(e <- entities){
      process(e)
    }
  }

  def checkIntrest(e: Entity) = {
    //Log.debug("Checking system's intrest","System: " + this.toString + ", Entity: " + e.toString)
    val contains = entities.contains(e)
    val intrest = this.isIntrestedIn(e)
    if(contains && !intrest) removeEntity(e)
    if(!contains && intrest) addEntity(e)
  }

  def removeEntity(e: Entity) = {
    val index = entities.indexOf(e)
    if(index >= 0){
      entities.remove(index)
    }
  }

  def addEntity(e: Entity) = {
    entities += e
  }

  def isIntrestedIn (e: Entity) = (this.aspect & e.aspect) == this.aspect

  def process(e: Entity): Unit

  def begin(): Unit = {
    if(started)
      Log.error("Illegal system start", "System must be ended before starting it again")
    started = true
  }

  def end(): Unit = {
    if(!started)
      Log.error("Illegal system end", "System must be started before end")
    started = false
  }

  override def toString() ={
    "EntitySystem"
  }
}
