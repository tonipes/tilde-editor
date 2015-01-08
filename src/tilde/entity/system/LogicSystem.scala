package tilde.entity.system

import tilde.entity.Entity

/**
 * Created by Toni on 27.12.14.
 */
abstract class LogicSystem extends EntitySystem(){
  override def processEntities(delta: Float) = {
    processSystem()
  }

  def processSystem(): Unit

  override def process(e: Entity, delta: Float): Unit = {}
}
