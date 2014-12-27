package tilde.game

import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.{GL11, Display}
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import org.lwjgl.util.vector.{Vector3f}
import tilde.entity.system.{PhysicsSystem, RenderSystem, CameraSystem}
import tilde.{Input, ResourceManager}
import tilde.entity.{World, Entity}
import tilde.entity.component.{PhysicsComponent, CameraComponent, ModelComponent, SpatialComponent}
import tilde.log.Log
import tilde.util.Direction

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

    world = new World(physicsSystem,cameraSystem,renderSystem)

    // Test entities
    val cube = world.createEntity()
    cube.addComponent(new SpatialComponent())
    cube.addComponent(new ModelComponent("cube","measure"))

    val cube2 = world.createEntity()
    val cube2Spatial = new SpatialComponent()
    cube2Spatial.move(Direction.UP,3)
    //cube2Spatial.rotate(45,cube2Spatial.forward())
    val phys = new PhysicsComponent()
    phys.angularSpeed.x = 1
    //phys.angularSpeed.y = 1
    phys.speed.x = 0.001f
    cube2.addComponent(phys)
    cube2.addComponent(cube2Spatial)
    cube2.addComponent(new ModelComponent("insetcube","measure"))

    val camera = world.createEntity()
    camera.addComponent(new CameraComponent(100f,0.1f, Display.getWidth.toFloat / Display.getHeight.toFloat,60))
    val cameraSpatial = new SpatialComponent()
    cameraSpatial.setPosition(0,0,2)
    camera.addComponent(cameraSpatial)

    world.addTag("camera",camera)
    glEnable(GL_DEPTH_TEST)
    glEnable(GL_CULL_FACE)
    glClearColor(0.031f, 0.663f, 1.0f, 0.0f)
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
