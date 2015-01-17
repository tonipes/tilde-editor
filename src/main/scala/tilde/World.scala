package tilde

import tilde.systems.EntitySystem
import scala.collection.mutable.Buffer
import scala.collection.mutable.Map

class World(entitySystems: EntitySystem*) {

  for(sys <- entitySystems){
    sys.world = this
  }
  val tags = Map[String, Entity]()

  val entities = Buffer[Entity]()

  val created = Buffer[Entity]()
  val changed = Buffer[Entity]()
  val destroyed = Buffer[Entity]()

  def update(delta: Float): Unit = {
    handleChanges()
    for(sys <- entitySystems){
      sys.begin()
      sys.processEntities(delta: Float)
      sys.end()
    }
  }

  def handleChanges() = {
    for(system <- entitySystems){
      for(entity <- created){ // Created
        system.checkInterest(entity)
      }
      for(entity <- changed){  // Changed
        system.checkInterest(entity)
      }
      for(entity <- destroyed){ // Destryed
        system.removeEntity(entity)
      }
    }
    created.clear()
    changed.clear()
    destroyed.clear()
  }

  def changed(entity: Entity): Unit = {
    if(!changed.contains(entity) && !created.contains(entity))
      changed += entity
  }

  def createEntity(): Entity = {
    val entity = new Entity(this)
    created += entity
    entity
  }

  def addTag(tag: String, entity: Entity): Unit = tags(tag) = entity

  def getTagged(tag: String): Option[Entity] = Some(tags(tag))

  def destroyEntity(entity: Entity) = {
    val index = entities.indexOf(entity)
    if (index >= 0) entities.remove(index)
     entity.dispose()
  }

}