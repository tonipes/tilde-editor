package tilde.entity.system

import tilde.entity.Entity
import tilde.entity.aspect.Aspect

/**
 * Created by Toni on 23.12.14.
 */
abstract class EntitySystem(val aspect: Aspect) {

  def processEntities(entities: Vector[Entity]): Unit ={
    for(e <- entities){
      process(e)
    }
  }

  def process(e: Entity): Unit

  def begin(): Unit

  def end(): Unit
}
