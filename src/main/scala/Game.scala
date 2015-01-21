import tilde.util.{Quaternion, Vec3}
import tilde.{ModelComponent, SpatialComponent, ResourceManager, World}

import org.lwjgl.opengl.GL11._
import tilde.graphics._
import tilde.util.DataProtocol._
import spray.json._
/**
 * Created by Toni on 17.1.2015.
 */
class Game {

  var world: World = null
  
  def create(): Unit = {
    world = new World()

    // Testing
    val ent = world.createEntity()
    ent.addComponent(new SpatialComponent(Vec3(1,1,1)))
    ent.addComponent(new ModelComponent("default"))
    //println(ent.toJson.prettyPrint)

    val quat = Quaternion(5f,634f,2321f,234f).normalise()
    println(quat)
    println(quat.rotationMatrix())

    glEnable(GL_DEPTH_TEST)
    glEnable(GL_CULL_FACE)
    glClearColor(0.2f, 0.2f, 0.2f, 0.0f)
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
