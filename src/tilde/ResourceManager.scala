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
  val defaultTexture = Texture.load("null")
  val textures = Map[String, Texture]().withDefault(a => defaultTexture)
  val meshes = Map[String,Mesh]()
  val shaderPrograms = Map[String, ShaderProgram]()

  def readFromFile(path:String): String = Source.fromFile(path).mkString
  def readImageFromFile(path:String): BufferedImage = ImageIO.read(new File(path))

  // debugging resources
  /*textures("grass") = Texture.load("data/textures/grass.png")
  textures("tree") = Texture.load("data/textures/tree.png")
  textures("wood") = Texture.load("data/textures/wood.png")
  textures("measure") = Texture.load("data/textures/measure_128.png")

  meshes("insetcube") = ResourceUtil.loadMesh("data/meshes/insetcube.obj")
  meshes("plane") = ResourceUtil.loadMesh("data/meshes/bigPlane_smooth.obj")
  meshes("cube") = ResourceUtil.loadMesh("data/meshes/1x1_cube.obj")
  meshes("bunny") = ResourceUtil.loadMesh("data/meshes/bunny_smooth.obj")
  meshes("dragon") = ResourceUtil.loadMesh("data/meshes/dragon.obj")
  meshes("bud") = ResourceUtil.loadMesh("data/meshes/bud.obj")
  meshes("teapot") = ResourceUtil.loadMesh("data/meshes/teapot_test.obj")
  meshes("fence") = ResourceUtil.loadMesh("data/meshes/fence.obj")
  meshes("treetop") = ResourceUtil.loadMesh("data/meshes/treetop.obj")
  meshes("treetop2") = ResourceUtil.loadMesh("data/meshes/treetop2.obj")
  meshes("treetrunk") = ResourceUtil.loadMesh("data/meshes/treetrunk.obj")*/

  // ###### Night scene resources #####
  textures("measure") = Texture.load("data/textures/measure_128.png")
  textures("cliff") = Texture.load("data/textures/st_cliff.png")
  textures("fire") = Texture.load("data/textures/st_fire.png")
  textures("grass") = Texture.load("data/textures/st_grass.png")
  textures("tree") = Texture.load("data/textures/st_tree.png")
  textures("tree2") = Texture.load("data/textures/st_tree2.png")
  textures("wood") = Texture.load("data/textures/st_wood.png")
  textures("rock") = Texture.load("data/textures/st_rock.png")

  /*meshes("dragon") = ResourceUtil.loadMesh("data/meshes/bud.obj")
  meshes("cliff") = ResourceUtil.loadMesh("data/meshes/st_cliff.obj")
  meshes("rock") = ResourceUtil.loadMesh("data/meshes/st_rock.obj")
  meshes("fence") = ResourceUtil.loadMesh("data/meshes/st_fence.obj")
  meshes("fire_flame") = ResourceUtil.loadMesh("data/meshes/st_fire_flame.obj")
  meshes("fire_logs") = ResourceUtil.loadMesh("data/meshes/st_fire_logs.obj")
  meshes("grass") = ResourceUtil.loadMesh("data/meshes/st_grass.obj")
  meshes("tree_top_1") = ResourceUtil.loadMesh("data/meshes/st_tree_top_1.obj")
  meshes("tree_top_2") = ResourceUtil.loadMesh("data/meshes/st_tree_top_2.obj")
  meshes("tree_top_3") = ResourceUtil.loadMesh("data/meshes/st_tree_top_3.obj")
  meshes("tree_top_small") = ResourceUtil.loadMesh("data/meshes/st_tree_top_small.obj")
  meshes("tree_trunk_1") = ResourceUtil.loadMesh("data/meshes/st_tree_trunk_1.obj")
  meshes("tree_trunk_2") = ResourceUtil.loadMesh("data/meshes/st_tree_trunk_2.obj")
  meshes("tree_trunk_3") = ResourceUtil.loadMesh("data/meshes/st_tree_trunk_3.obj")
  meshes("tree_trunk_small") = ResourceUtil.loadMesh("data/meshes/st_tree_trunk_small.obj")*/
  meshes("cube") = ResourceUtil.loadPly("data/meshes/plyTest.ply")

  shaderPrograms("default") = ShaderProgram.load("data/shaders/default.vert","data/shaders/default.frag")

  // Night Scene map constructor
  // JUST FOR TESTING. DO NOT JUDGE!
  def createNightScene(world: World) = {
    var entity = world.createEntity()
    var spatial = new SpatialComponent()
    var model = new ModelComponent("grass","grass")
    //val phys = new PhysicsComponent()
    //phys.angularSpeed.x = 1

    //Grass
    entity.addComponent(spatial)
    entity.addComponent(model)

    // Cliff
    entity = world.createEntity()
    model = new ModelComponent("cliff","cliff")
    spatial = new SpatialComponent()
    entity.addComponent(spatial)
    entity.addComponent(model)

    // Fence
    entity = world.createEntity()
    model = new ModelComponent("fence","wood")
    spatial = new SpatialComponent()
    entity.addComponent(spatial)
    entity.addComponent(model)

    // Back Tree
    spatial = new SpatialComponent()
    spatial.setPosition(-6.5f,0.9f,-6.5f)

    entity = world.createEntity() // trunk
    model = new ModelComponent("tree_trunk_2","wood")
    entity.addComponent(spatial)
    entity.addComponent(model)

    entity = world.createEntity() // top
    model = new ModelComponent("tree_top_2","tree")
    entity.addComponent(spatial)
    entity.addComponent(model)

    // Left Tree
    spatial = new SpatialComponent()
    spatial.setPosition(-7f,0.9f,-2f)

    entity = world.createEntity() // trunk
    model = new ModelComponent("tree_trunk_1","wood")
    entity.addComponent(spatial)
    entity.addComponent(model)
    //entity.addComponent(phys)

    entity = world.createEntity() // top
    model = new ModelComponent("tree_top_1","tree")
    entity.addComponent(spatial)
    entity.addComponent(model)
    //entity.addComponent(phys)

    // Right Tree
    spatial = new SpatialComponent()
    spatial.setPosition(-2f,0.9f,-7f)

    entity = world.createEntity() // trunk
    model = new ModelComponent("tree_trunk_1","wood")
    entity.addComponent(spatial)
    entity.addComponent(model)
    //entity.addComponent(phys)

    entity = world.createEntity() // top
    model = new ModelComponent("tree_top_1","measure")
    entity.addComponent(spatial)
    entity.addComponent(model)
   // entity.addComponent(phys)


    // Small tree
    entity = world.createEntity()
    spatial = new SpatialComponent()
    spatial.setPosition(-6.5f,0.7f,-3.8f)
    //spatial.rotate(0,45,0)

    entity = world.createEntity() // trunk
    model = new ModelComponent("tree_trunk_small","wood")
    entity.addComponent(spatial)
    entity.addComponent(model)

    entity = world.createEntity() // top
    model = new ModelComponent("tree_top_small","tree")
    entity.addComponent(spatial)
    entity.addComponent(model)
    entity = world.createEntity()

    // rock
    spatial = new SpatialComponent()
    spatial.setPosition(-4f,0.7f,-6f)
    spatial.rotate(0,45,0)

    entity = world.createEntity() // trunk
    model = new ModelComponent("rock","rock")
    entity.addComponent(spatial)
    entity.addComponent(model)

    // Fire
    spatial = new SpatialComponent()
    spatial.setPosition(-3.5f,1f,-3.5f)

    entity = world.createEntity()
    model = new ModelComponent("fire_flame","fire")
    entity.addComponent(spatial)
    entity.addComponent(model)

    entity = world.createEntity() // top
    model = new ModelComponent("fire_logs","wood")
    entity.addComponent(spatial)
    entity.addComponent(model)
    entity = world.createEntity()

    // Light
    val lightSpatial = new SpatialComponent()
    lightSpatial.setPosition(-3.5f,2f,-3.5f)
    entity.addComponent(new LightSourceComponent(new Vector4f(1.0f,0.3f,0.7f,1f),1))
    entity.addComponent(lightSpatial)
    world.addTag("fireLight",entity)

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
