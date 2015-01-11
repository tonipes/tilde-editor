package tilde

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import org.lwjgl.opengl.Display
import org.lwjgl.util.vector.Vector4f

import tilde.entity._
import tilde.entity.component._
import tilde.log.Log
import tilde.util._
import tilde.graphics._

import scala.collection.mutable
import scala.collection.mutable.Map


import scala.io.Source

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
  -
 */
object ResourceManager {

  val textures = Map[String, Texture]()
  val meshes = Map[String,Mesh]()
  val shaderPrograms = Map[String, ShaderProgram]()

  textures("measure") = ResourceUtil.loadTexture("src/main/resources/textures/measure_128.png")
  textures("grass") = ResourceUtil.loadTexture("src/main/resources/textures/measure_128.png")
  
  meshes("map") = ResourceUtil.loadMesh("src/main/resources/meshes/plyTest.ply")
  
  shaderPrograms("default") = ShaderProgram.load( "src/main/resources/shaders/default.vert",
                                                  "src/main/resources/shaders/default.frag")

  def createBallScene(world: World) = {
    val entity = world.createEntity()
    val spatial = new SpatialComponent()
    val model = new ModelComponent("map","measure")
    entity.addComponent(spatial)
    entity.addComponent(model)
    // Camera
    val camera = world.createEntity()
    val a = 60f
    val height = Display.getHeight/a
    val width = Display.getWidth/a
    camera.addComponent(new CameraComponent(MatrixUtil.createOrthographicProjection(100,0.1f,-width,width,height,-height)))
    val cameraSpatial = new SpatialComponent()
    val cameraLight = new LightSourceComponent(new Vector4f(1.0f,1.0f,1.0f,1.0f),1)
    cameraSpatial.setPosition(6f,4f,6f)
    //cameraSpatial.rotate(-25,Direction.AXIS_X)
    //cameraSpatial.rotate(45,Direction.AXIS_Y)
    camera.addComponent(cameraLight)
    camera.addComponent(cameraSpatial)
    world.addTag("camera",camera)
  }
}
