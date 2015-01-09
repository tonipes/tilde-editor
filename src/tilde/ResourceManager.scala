package tilde

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import org.lwjgl.opengl.Display
import org.lwjgl.util.vector.Vector4f
import tilde.entity.{World, Entity}
import tilde.entity.component._
import tilde.log.Log
import tilde.util.{ResourceUtil, MatrixUtil, Direction}

import scala.collection.mutable
import scala.collection.mutable.Map
import tilde.graphics.{ShaderProgram, Mesh, Texture}

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
  - Way to handle all different ids(names) in development ( Resource map document?)
  - Way to handle resource releasing ( when resource is no longer needed)
  - Some kind of "loading request system"
  -
 */
object ResourceManager {
  val textures = Map[String, Texture]()
  val meshes = Map[String,Mesh]()
  val shaderPrograms = Map[String, ShaderProgram]()
  //textures("measure") = Texture.load("data/textures/measure_128.png")

  meshes("cube") = ResourceUtil.loadMesh("data/meshes/plyTest.ply")

  shaderPrograms("default") = ShaderProgram.load("data/shaders/default.vert","data/shaders/default.frag")

  def createBallScene(world: World) = {
    var entity = world.createEntity()
    var spatial = new SpatialComponent()
    var model = new ModelComponent("cube","measure")

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
    cameraSpatial.rotate(-25,Direction.AXIS_X)
    cameraSpatial.rotate(45,Direction.AXIS_Y)
    camera.addComponent(cameraLight)
    camera.addComponent(cameraSpatial)
    world.addTag("camera",camera)
  }
}
