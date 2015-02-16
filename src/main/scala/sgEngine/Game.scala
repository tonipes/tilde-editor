package sgEngine

import sgEngine.system._

/**
 * Created by Toni on 17.1.2015.
 */
abstract class Game {
/*
  lazy val world: World = new World(new EditorInputSystem(),new RenderSystem())
  lazy val mapData = ResourceManager.loadMap("maps/default.map")
  lazy val editorSettings = ResourceManager.loadMap("editor_settings.map")
  */
  def create(): Unit/* = {
    world.createEntities(editorSettings)
    world.createEntities(mapData)
    val testing = new ObjectOutputStream(new FileOutputStream("test.obj"))
    testing.writeObject(world.getEntityData())
    testing.close()

  }*/

  def render(): Unit/* = {

  }*/

  def resize(width: Int, height: Int): Unit/* = {

  }*/

  def update(delta: Float): Unit/* = {
//    world.update(delta)
  }*/

  def dispose()/* = {
//    world.dispose()
  }*/
}
