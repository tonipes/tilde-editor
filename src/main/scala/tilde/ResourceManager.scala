package tilde

import org.lwjgl.opengl.Display
import org.lwjgl.util.vector.{Quaternion, Vector3f}
import tilde.entity._
import tilde.graphics.{Model, _}
import tilde.log.Log
import tilde.util._

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
    for(x <- 0 until 100;y <- 0 until 100) {
      val entity = world.createEntity()
      val spatial = new SpatialComponent(new Vector3f(x, 0, y), new Quaternion().setIdentity(), new Vector3f(1, 1, 1))
      val model = new ModelComponent("untitled")
      entity.addComponent(spatial)
      entity.addComponent(model)
    }

    ResourceUtil.worldToFile(world,"maps/testMap.map")

    // Camera
    val camera = world.createEntity()
    val a = 60f
    val height = Display.getHeight/a
    val width = Display.getWidth/a
    camera.addComponent(CameraComponent(MatrixUtil.createOrthographicProjection(100,0.1f,-width,width,height,-height)))
    camera.addComponent(LightSourceComponent())
    // 0.8f,-0.2,0.5f
    val q = new Quaternion()
    q.set(-0.16f,-0.8f,-0.2f,0.5f)
    q.normalise()
    camera.addComponent(SpatialComponent(new Vector3f(5,10,-4),q,new Vector3f(1,1,1)))
    //cameraSpatial.rotate(-25,Direction.AXIS_X)
    //cameraSpatial.rotate(45,Direction.AXIS_Y)

    world.addTag("camera",camera)
  }
}
