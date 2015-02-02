import tilde.util._
import tilde._
import tilde.system._
import scala.collection.mutable.Buffer
import org.lwjgl.opengl.GL11._
import tilde.graphics._
import tilde.util.DataProtocol._
import spray.json._

/**
 * Created by Toni on 17.1.2015.
 */
class Game {

  lazy val world: World = new World(new RenderSystem())
  lazy val mapData = ResourceManager.loadMap("maps/default.map")

  def create(): Unit = {
    world.createEntities(mapData)

    val startTime = System.currentTimeMillis()
    for(i <- 1 until 700){
      val startTimeIn = System.currentTimeMillis()
      mapData.foreach(a => world.createEntity(a))
      println(s"Inside Time took ${System.currentTimeMillis() - startTimeIn} i= $i")
    }
    println(s"Time took ${System.currentTimeMillis() - startTime} world has ${world.getEntityCount()}")
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
