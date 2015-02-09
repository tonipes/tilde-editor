import java.io.{FileOutputStream, ObjectOutputStream}

import tilde.util._
import tilde._
import tilde.system._
import scala.collection.immutable.BitSet
import scala.collection.mutable
import scala.collection.mutable.Buffer
import org.lwjgl.opengl.GL11._
import tilde.graphics._
import tilde.util.DataProtocol._
import spray.json._

/**
 * Created by Toni on 17.1.2015.
 */
class Game {

  lazy val world: World = new World(new EditorInputSystem(),new RenderSystem())
  lazy val mapData = ResourceManager.loadMap("maps/default.map")

  def create(): Unit = {
    world.createEntities(mapData)
//    val testing = new ObjectOutputStream(new FileOutputStream("test.obj"))
//    testing.writeObject(world.getEntityData())
//    testing.close()

  }

  def render(): Unit = {

  }

  def resize(width: Int, height: Int): Unit = {

  }

  def update(delta: Float): Unit = {
    world.update(delta)
  }

  def dispose() = {
    world.dispose()
  }
}
