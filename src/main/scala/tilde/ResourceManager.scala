package tilde

import org.lwjgl.opengl.Display

import tilde.entity._
import tilde.graphics.Model
import tilde.log.Log
import tilde.util._
import tilde.graphics._

import scala.collection.mutable.Map


/**
 * Created by Toni on 17.12.14.
 */
 
/*
  Needed file(data) formats:
  - Mesh
  - Texture
  - Model (textured mesh)
  - String (and all other values)

  Also needed:
  - Way to handle all different ids(names) in development
  - Way to handle resource releasing ( when resource is no longer needed)
  - Some kind of "loading request system"
 */
object ResourceManager {

  val textures = Map[String, Texture]()
  val meshes = Map[String,Mesh]()
  val shaderPrograms = Map[String, ShaderProgram]()
  val models = Map[String, Model]()

  textures("measure") = ResourceUtil.loadTexture("textures/measure_128.png")
  textures("grass") = ResourceUtil.loadTexture("textures/measure_128.png")
  
  meshes("untitled") = ResourceUtil.loadMesh("meshes/plyTest.ply")
  
  shaderPrograms("default") = ResourceUtil.loadShader( 
                              "shaders/default.vert",
                              "shaders/default.frag")

  models("untitled") = ResourceUtil.loadModel("objects/untitled.model")

  Log.debug(models("untitled").mesh + models("untitled").material)

  def createBallScene(world: World) = {
    val entity = world.createEntity()
    val spatial = new SpatialComponent()
    val model = new ModelComponent("untitled")
    entity.addComponent(spatial)
    entity.addComponent(model)
    // Camera
    val camera = world.createEntity()
    val a = 60f
    val height = Display.getHeight/a
    val width = Display.getWidth/a
    camera.addComponent(CameraComponent(MatrixUtil.createOrthographicProjection(100,0.1f,-width,width,height,-height)))
    camera.addComponent(LightSourceComponent())
    camera.addComponent(SpatialComponent())
    //cameraSpatial.rotate(-25,Direction.AXIS_X)
    //cameraSpatial.rotate(45,Direction.AXIS_Y)

    world.addTag("camera",camera)
  }
}
