package tilde.entity
import scala.collection.mutable._

/**
 * Created by Toni on 23.12.14.
 */
/*
  - Groups?
  - Tags ( to indentify eg. player or camera)
  - Managers? is needed !!
  - Signals !? Events?
 */
class World(entitySystems: EntitySystem*) {
  for(sys <- entitySystems){
    sys.world = this
  }

  // Tag is used to identify unique entities like "camera" and "player"
  val tags = Map[String, Entity]()  

  val entities = Buffer[Entity]()

  val created = Buffer[Entity]()
  val changed = Buffer[Entity]()
  val destroyed = Buffer[Entity]()

  // Iterates all systems and runs system with all entities that have the systems required aspect
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
      // This comment will be visible in test commit
  }

  def createEntity() = {
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
