package tilde.entity.system

import tilde.entity.{World, Entity}

import scala.collection.mutable._

/**
 * Created by Toni on 23.12.14.
 */
abstract class EntitySystem(val aspect: BitSet) {
  var entities = Buffer[Entity]()

  def processEntities(): Unit ={
    for(e <- entities){
      process(e)
    }
  }

  def checkIntrest(e: Entity) = {
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

  def begin(): Unit

  def end(): Unit
}
