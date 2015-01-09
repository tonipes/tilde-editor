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

  val tags = Map[String, Entity]()  // Tag is used to identify unique entities like "camera" and "player"

  //val systems = HashMap[Class[_ <: EntitySystem], EntitySystem]()  // Systems alter world's state.

  val entities = Buffer[Entity]()

  val created = Buffer[Entity]()
  val changed = Buffer[Entity]()
  val destroyed = Buffer[Entity]()

  // Iterates all systems and runs system with all entities that have the systems required aspect
  def update(delta: Float): Unit = {
    //Log.debug("Starting world update", "systems: " + entitySystems.toString)
    handleChanges()
    for(sys <- entitySystems){
      //Log.debug("Starting system", sys.toString)
      sys.begin()
      sys.processEntities(delta: Float)
      sys.end()
    }
  }

  def handleChanges() = {
    for(system <- entitySystems){
      for(e <- created){ // Created
        system.checkInterest(e)
      }
      for(e <- changed){  // Changed
        system.checkInterest(e)
      }
      for(e <- destroyed){ // Destryed
        system.removeEntity(e)
      }
    }
    created.clear()
    changed.clear()
    destroyed.clear()
  }

  def changed(e: Entity): Unit = {
    if(!changed.contains(e) && !created.contains(e))
      changed += e
  }

  def createEntity() = {
    val e = new Entity(this)
    created += e
    e
  }

  def addTag(tag: String, e: Entity): Unit ={
    //Log.debug("Tagging entity",tag + ", " + e.toString)
    tags(tag) = e
    //Log.debug("Tagged now", tags.toString)
  }

  def getTagged(tag: String): Option[Entity] = Some(tags(tag))

  def destroyEntity(e: Entity) = {
    val i = entities.indexOf(e)
    if (i >= 0) entities.remove(i)
    e.dispose()
  }
}
