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

  val tags = HashMap[String, Entity]()  // Tag is used to identify unique entities like "camera" and "player"
  val systems = HashMap[Class[_ <: EntitySystem], EntitySystem]()  // Systems alter world's state.
  val entities = Buffer[Entity]()

  val created = Buffer[Entity]()
  val changed = Buffer[Entity]()
  val destroyed = Buffer[Entity]()

  // Iterates all systems and runs system with all entities that have the systems required aspect
  def update(delta: Float): Unit = {
    handleChanges()
  }

  def handleChanges() = {
    for(system <- systems){
      val sys = system._2
      val aspect = sys.aspect

      for(e <- created){ // Created
        sys.checkIntrest(e)
      }
      for(e <- changed){
        sys.checkIntrest(e)
      }
      for(e <- destroyed){
        sys.removeEntity(e)
      }
    }
    created.clear()
    changed.clear()
    destroyed.clear()
  }

  def changed(e: Entity): Unit = {
    if(!changed.contains(e))
      changed += e
  }

  def createEntity() = {
    val e = new Entity(this)
    created += e
    e
  }

  def addTag(tag: String, e: Entity): Unit ={
    tags(tag) = e
  }


  def destroyEntity(e: Entity) = {
    val i = entities.indexOf(e)
    if (i >= 0) entities.remove(i)
    e.dispose()
  }
}
