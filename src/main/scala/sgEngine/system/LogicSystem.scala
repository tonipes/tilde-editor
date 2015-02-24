package sgEngine.system

import sgEngine.systems.EntitySystem
import sgEngine.Entity

import scala.collection.immutable.BitSet

/**
 * Created by Toni on 5.2.2015.
 */

abstract class LogicSystem extends EntitySystem(){
  override var compStruct: Vector[BitSet] = Vector()

  override def processEntities(delta: Float) = {
    processSystem(delta: Float)
  }

  def processSystem(delta: Float): Unit

  override def checkInterest(e: Entity): Unit = {}

  override def process(e: Entity, delta: Float): Unit = {}

  override def removeEntity(e: Entity): Unit  = {}
}
