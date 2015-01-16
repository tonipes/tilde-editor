package tilde.game

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl._
import tilde._
import tilde.entity._
import tilde.entity.system._
import tilde.log.Log

/**
 * Created by Toni on 13.12.2014.
 */
class Game {
  var world: World = null
  
  def create(): Unit = {
    // Test systems
    val cameraSystem = new CameraSystem()
    val renderSystem = new RenderSystem()
    val physicsSystem = new PhysicsSystem()
    val inputSystem = new InputSystem()
    
    world = new World(inputSystem, physicsSystem, cameraSystem, renderSystem)

    ResourceManager.createBallScene(world)

    glEnable(GL_DEPTH_TEST)
    glEnable(GL_CULL_FACE)
    //glClearColor(0.031f, 0.663f, 1.0f, 0.0f)
    glClearColor(0.2f, 0.2f, 0.2f, 0.0f)
  }

  def render(): Unit = {

  }

  def resize(width: Int, height: Int): Unit = {
    GL11.glViewport(0,0,width,height)
    Log.info("Game resize", ""+ width + ", " + height)
  }

  def update(delta: Float): Unit = {
    world.update(delta)
  }

  def dispose() = {

  }
}
