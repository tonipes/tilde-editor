package tilde.system

import tilde.systems.EntitySystem
import tilde.Entity

import scala.collection.immutable.BitSet

/**
 * Created by Toni on 5.2.2015.
 */

abstract class LogicSystem extends EntitySystem(){
  override var compStruct: BitSet = BitSet.empty

  override def processEntities(delta: Float) = {
    processSystem(delta: Float)
  }

  def processSystem(delta: Float): Unit

  override def checkInterest(e: Entity): Unit = {}

  override def process(e: Entity, delta: Float): Unit = {}

  override def removeEntity(e: Entity): Unit  = {}
}
