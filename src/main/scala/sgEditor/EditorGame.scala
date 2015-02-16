package sgEditor

import sgEditor.systems.{EditorRenderSystem, EditorInputSystem}
import sgEngine.{ResourceManager, World, Game}

/**
 * Created by Toni on 15.2.15.
 */
class EditorGame extends Game {
  lazy val world: World = new World(new EditorInputSystem(),new EditorRenderSystem())
  lazy val mapData = ResourceManager.loadMap("maps/default.map")
  lazy val editorSettings = ResourceManager.loadMap("editor_settings.map")

  override def create(): Unit = {
    world.createEntities(editorSettings)
    world.createEntities(mapData)
  }

  override def resize(width: Int, height: Int): Unit = {

  }

  override def update(delta: Float): Unit = {
    world.update(delta)
  }

  override def dispose(): Unit = {
    world.dispose()
  }

  override def render(): Unit = {

  }
}
