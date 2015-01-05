package tilde

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import org.lwjgl.opengl.Display
import org.lwjgl.util.vector.Vector4f
import tilde.entity.{World, Entity}
import tilde.entity.component._
import tilde.log.Log
import tilde.util.{MatrixUtil, Direction}

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
  val defaultTexture = Texture.load("null")
  val textures = Map[String, Texture]().withDefault(a => defaultTexture)
  val meshes = Map[String,Mesh]()
  val shaderPrograms = Map[String, ShaderProgram]()

  def readFromFile(path:String): String = Source.fromFile(path).mkString
  def readImageFromFile(path:String): BufferedImage = ImageIO.read(new File(path))


  Log.debug("Size of short",Short.MinValue + " - " + Short.MaxValue)
  Log.debug("Size of int",Int.MinValue + " - " + Int.MaxValue)
  // debugging resources
  /*textures("grass") = Texture.load("data/textures/grass.png")
  textures("tree") = Texture.load("data/textures/tree.png")
  textures("wood") = Texture.load("data/textures/wood.png")
  textures("measure") = Texture.load("data/textures/measure_128.png")

  meshes("insetcube") = Mesh.load("data/meshes/insetcube.obj")
  meshes("plane") = Mesh.load("data/meshes/bigPlane_smooth.obj")
  meshes("cube") = Mesh.load("data/meshes/1x1_cube.obj")
  meshes("bunny") = Mesh.load("data/meshes/bunny_smooth.obj")
  meshes("dragon") = Mesh.load("data/meshes/dragon.obj")
  meshes("bud") = Mesh.load("data/meshes/bud.obj")
  meshes("teapot") = Mesh.load("data/meshes/teapot_test.obj")
  meshes("fence") = Mesh.load("data/meshes/fence.obj")
  meshes("treetop") = Mesh.load("data/meshes/treetop.obj")
  meshes("treetop2") = Mesh.load("data/meshes/treetop2.obj")
  meshes("treetrunk") = Mesh.load("data/meshes/treetrunk.obj")*/

  // ###### Night scene resources #####
  textures("measure") = Texture.load("data/textures/measure_128.png")
  textures("cliff") = Texture.load("data/textures/st_cliff.png")
  textures("fire") = Texture.load("data/textures/st_fire.png")
  textures("grass") = Texture.load("data/textures/st_grass.png")
  textures("tree") = Texture.load("data/textures/st_tree.png")
  textures("tree2") = Texture.load("data/textures/st_tree2.png")
  textures("wood") = Texture.load("data/textures/st_wood.png")
  textures("rock") = Texture.load("data/textures/st_rock.png")

  meshes("dragon") = Mesh.load("data/meshes/bud.obj")
  meshes("cliff") = Mesh.load("data/meshes/st_cliff.obj")
  meshes("rock") = Mesh.load("data/meshes/st_rock.obj")
  meshes("fence") = Mesh.load("data/meshes/st_fence.obj")
  meshes("fire_flame") = Mesh.load("data/meshes/st_fire_flame.obj")
  meshes("fire_logs") = Mesh.load("data/meshes/st_fire_logs.obj")
  meshes("grass") = Mesh.load("data/meshes/st_grass.obj")
  meshes("tree_top_1") = Mesh.load("data/meshes/st_tree_top_1.obj")
  meshes("tree_top_2") = Mesh.load("data/meshes/st_tree_top_2.obj")
  meshes("tree_top_3") = Mesh.load("data/meshes/st_tree_top_3.obj")
  meshes("tree_top_small") = Mesh.load("data/meshes/st_tree_top_small.obj")
  meshes("tree_trunk_1") = Mesh.load("data/meshes/st_tree_trunk_1.obj")
  meshes("tree_trunk_2") = Mesh.load("data/meshes/st_tree_trunk_2.obj")
  meshes("tree_trunk_3") = Mesh.load("data/meshes/st_tree_trunk_3.obj")
  meshes("tree_trunk_small") = Mesh.load("data/meshes/st_tree_trunk_small.obj")
  meshes("cube") = Mesh.load("data/meshes/cube.obj")
  // ###### Night scene resources end #####
  meshes("ball") = Mesh.load("data/meshes/bigball.obj")
  shaderPrograms("default") = ShaderProgram.load("data/shaders/default.vert","data/shaders/default.frag")

  // Night Scene map constructor
  // JUST FOR TESTING. DO NOT JUDGE!
  def createNightScene(world: World) = {
    var entitiy = world.createEntity()
    var spatial = new SpatialComponent()
    var model = new ModelComponent("grass","grass")
    val phys = new PhysicsComponent()
    phys.angularSpeed.x = 1

    //Grass
    entitiy.addComponent(spatial)
    entitiy.addComponent(model)

    // Cliff
    entitiy = world.createEntity()
    model = new ModelComponent("cliff","cliff")
    spatial = new SpatialComponent()
    entitiy.addComponent(spatial)
    entitiy.addComponent(model)

    // Fence
    entitiy = world.createEntity()
    model = new ModelComponent("fence","wood")
    spatial = new SpatialComponent()
    entitiy.addComponent(spatial)
    entitiy.addComponent(model)

    // Back Tree
    spatial = new SpatialComponent()
    spatial.setPosition(-6.5f,0.9f,-6.5f)


    entitiy = world.createEntity() // trunk
    model = new ModelComponent("tree_trunk_2","wood")
    entitiy.addComponent(spatial)
    entitiy.addComponent(model)
    entitiy.addComponent(phys)

    spatial = new SpatialComponent()
    spatial.setPosition(-15f,0.9f,-15f)


    entitiy = world.createEntity() // top
    model = new ModelComponent("tree_top_2","tree")
    entitiy.addComponent(spatial)
    entitiy.addComponent(model)
    entitiy.addComponent(phys)

    // Left Tree
    spatial = new SpatialComponent()
    spatial.setPosition(-7f,0.9f,-2f)

    entitiy = world.createEntity() // trunk
    model = new ModelComponent("tree_trunk_1","wood")
    entitiy.addComponent(spatial)
    entitiy.addComponent(model)
    entitiy.addComponent(phys)

    entitiy = world.createEntity() // top
    model = new ModelComponent("tree_top_1","tree")
    entitiy.addComponent(spatial)
    entitiy.addComponent(model)
    entitiy.addComponent(phys)

    // Right Tree
    spatial = new SpatialComponent()
    spatial.setPosition(-2f,0.9f,-7f)

    entitiy = world.createEntity() // trunk
    model = new ModelComponent("tree_trunk_1","wood")
    entitiy.addComponent(spatial)
    entitiy.addComponent(model)
    entitiy.addComponent(phys)

    entitiy = world.createEntity() // top
    model = new ModelComponent("cube","measure")
    entitiy.addComponent(spatial)
    entitiy.addComponent(model)
    entitiy.addComponent(phys)
    entitiy = world.createEntity()

    // Small tree
    spatial = new SpatialComponent()
    spatial.setPosition(-6.5f,0.7f,-3.8f)
    spatial.rotate(0,45,0)

    entitiy = world.createEntity() // trunk
    model = new ModelComponent("tree_trunk_small","wood")
    entitiy.addComponent(spatial)
    entitiy.addComponent(model)

    entitiy = world.createEntity() // top
    model = new ModelComponent("tree_top_small","tree")
    entitiy.addComponent(spatial)
    entitiy.addComponent(model)
    entitiy = world.createEntity()

    // rock
    spatial = new SpatialComponent()
    spatial.setPosition(-4f,0.7f,-6f)
    spatial.rotate(0,45,0)

    entitiy = world.createEntity() // trunk
    model = new ModelComponent("rock","rock")
    entitiy.addComponent(spatial)
    entitiy.addComponent(model)

    // Fire
    spatial = new SpatialComponent()
    spatial.setPosition(-3.5f,1f,-3.5f)

    entitiy = world.createEntity()
    model = new ModelComponent("fire_flame","fire")
    entitiy.addComponent(spatial)
    entitiy.addComponent(model)

    entitiy = world.createEntity() // top
    model = new ModelComponent("fire_logs","wood")
    entitiy.addComponent(spatial)
    entitiy.addComponent(model)
    entitiy = world.createEntity()

    // Light
    val lightSpatial = new SpatialComponent()
    lightSpatial.setPosition(-3.5f,2f,-3.5f)
    entitiy.addComponent(new LightSourceComponent(new Vector4f(1.0f,0.3f,0.7f,1f),1))
    entitiy.addComponent(lightSpatial)
    world.addTag("fireLight",entitiy)

    // Camera
    val camera = world.createEntity()
    val a = 60f
    val height = Display.getHeight/a
    val width = Display.getWidth/a
    camera.addComponent(new CameraComponent(MatrixUtil.createOrthographicProjection(100,0.1f,-width,width,height,-height)))
    val cameraSpatial = new SpatialComponent()
    val cameraLight = new LightSourceComponent(new Vector4f(1.0f,1.0f,1.0f,1.0f),1)
    cameraSpatial.setPosition(30f,20f,30f)
    cameraSpatial.rotate(-25,Direction.AXIS_X)
    cameraSpatial.rotate(45,Direction.AXIS_Y)
    camera.addComponent(cameraLight)
    camera.addComponent(cameraSpatial)
    world.addTag("camera",camera)

  }

  def createBallScene(world: World) = {
    var entitiy = world.createEntity()
    var spatial = new SpatialComponent()
    var model = new ModelComponent("ball",null)

    //ball
    entitiy.addComponent(spatial)
    entitiy.addComponent(model)

    entitiy = world.createEntity()
    val lightSpatial = new SpatialComponent()
    lightSpatial.setPosition(5f,2f,5f)
    entitiy.addComponent(new LightSourceComponent(new Vector4f(1.0f,1f,1f,1f),1))
    entitiy.addComponent(lightSpatial)
    world.addTag("fireLight",entitiy)

    // Camera
    val camera = world.createEntity()
    val a = 60f
    val height = Display.getHeight/a
    val width = Display.getWidth/a
    camera.addComponent(new CameraComponent(MatrixUtil.createOrthographicProjection(100,0.1f,-width,width,height,-height)))
    val cameraSpatial = new SpatialComponent()
    cameraSpatial.setPosition(30f,20f,30f)
    cameraSpatial.rotate(-25,Direction.AXIS_X)
    cameraSpatial.rotate(45,Direction.AXIS_Y)
    camera.addComponent(cameraSpatial)
    world.addTag("camera",camera)
  }
}
