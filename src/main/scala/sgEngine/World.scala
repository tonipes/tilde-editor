package sgEngine

import sgEngine.systems.EntitySystem
import scala.collection.mutable.{ArrayBuffer, Buffer, Map}

class World(entitySystems: EntitySystem*) {

  for(sys <- entitySystems){
    sys.world = this
  }

  private val entities = ArrayBuffer[Entity]()
  private val changed = ArrayBuffer[Entity]()
  private val destroyed = ArrayBuffer[Entity]()

  private val tags = Map[String, Entity]()
  private val groups = Map[String,Vector[Entity]]()

  /**
   * Updates world. Runs all systems
   */
  def update(delta: Float): Unit = {
    handleChanges()
    for(sys <- entitySystems){
      sys.begin()
      sys.processEntities(delta: Float)
      sys.end()
    }
  }

  /**
   * Checks if any systems are interested in changed entities
   */
  def handleChanges() = {
    for(system <- entitySystems){
      for(entity <- changed){  // Changed
        system.checkInterest(entity)
        entity.changed = false
      }
      for(entity <- destroyed){ // Destroyed
        system.removeEntity(entity)
      }
    }
    changed.clear()
    destroyed.clear()
  }

  /**
   * Adds entity to changed list. See handleChanges
   * @param entity entity to add to changed list
   */
  def changed(entity: Entity): Unit = {
    if(entity.changed != true){
      entity.changed = true
      changed += entity
    }
  }

  /**
   * Creates an entity from EntityData
   * @param data data
   * @return created entity
   */
  def createEntity(data: EntityData) = {
    val entity = new Entity(this, data.components)
    data.tags.foreach(t => addTag(t,entity))
    entities += entity
    entity
  }

  def createEntities(data: Vector[EntityData]) =
    data.foreach(d => createEntity(d))

  /**
   * Adds tag to entity
   * @param tag tag to add
   * @param entity entity to add tag to
   */
  def addTag(tag: String, entity: Entity): Unit = tags(tag) = entity

  /**
   * Returns entity with tag
   * @param tag tag to find
   * @return entity with tag or None
   */
  def getTagged(tag: String): Option[Entity] = {
    if(tags.contains(tag))
      Some(tags(tag))
    else
      None
  }

  /**
   * Returns all tags that an entity has
   * @param e entity
   * @return all tags that given entity has
   */
  def getTags(e: Entity): Vector[String] =
    tags.filter(p => p._2 == e).map(p => p._1).toVector

  /**
   * Destroys an entity
   * @param entity entity to destroy
   */
  def destroyEntity(entity: Entity) = {
    val index = entities.indexOf(entity)
    if (index >= 0) entities.remove(index)
    entity.dispose()
  }

  /**
   * Returns length of the entities list
   * @return length of entities list
   */
  def getEntityCount() = entities.length

  /**
   * Returns all EntityData from world.
   * @return all entityData
   */
  def getEntityData(): Vector[EntityData] = {
    entities.map(f => f.getEntityData).toVector
  }

  def dispose(): Unit = {
    // TODO: Write method
  }

}