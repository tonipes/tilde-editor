package tilde.entity

import tilde.entity.aspect.Aspect
import tilde.entity.component.Component
import tilde.entity.system.EntitySystem

import scala.collection.mutable._
/**
 * Created by Toni on 23.12.14.
 */
/*
  - Groups?
  - Tags ( to indentify eg. player or camera)
  - Managers? is needed
  - Systems ! ! !
  - Signals !?
 */
class World {

  val tag = HashMap[String, Entity]()  // Tag is used to identify unique entities like "camera" and "player"
  val systems = HashMap[Class[_ <: EntitySystem], EntitySystem]()  // Systems alter world's state.
  val entities = Buffer[Entity]()

  val created = Buffer[Entity]()
  val changed = Buffer[Entity]()
  val destroyed = Buffer[Entity]()

  // Iterates all systems and runs system with all entities that have the systems required aspect
  // TODO: Way too slow way to update. Plz fix!
  def update(delta: Float): Unit = {
    for(system <- systems){
      val sys = system._2
      val aspect = sys.aspect
      val entitiesWithAspect = Buffer[Entity]()
      for(e <- entities){
        if(e.checkAspect(aspect))
          entitiesWithAspect += e
      }
      sys.processEntities(entitiesWithAspect.toVector)
    }
  }

  def addEntity(e: Entity) = {
    entities += e
  }

  def removeEntity(e: Entity) = {
    val i = entities.indexOf(e)
    if (i >= 0) entities.remove(i)
    e.dispose()
  }
}
