package tilde

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import tilde.entity.Entity
import tilde.entity.component.{ModelComponent, SpatialComponent}

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

  def readFromFile(path:String): String = Source.fromFile(path).mkString
  def readImegeFromFile(path:String): BufferedImage = ImageIO.read(new File(path))

  // debugging resources
  textures("grass") = Texture.load("data/textures/grass.png")
  textures("tree") = Texture.load("data/textures/tree.png")
  textures("wood") = Texture.load("data/textures/wood.png")
  textures("measure") = Texture.load("data/textures/measure_128.png")

  meshes("insetcube") = Mesh.load("data/meshes/insetcube.obj")
  meshes("cube") = Mesh.load("data/meshes/1x1_cube.obj")
  meshes("bunny") = Mesh.load("data/meshes/bunny.obj")
  //meshes("dragon") = Mesh.load("data/meshes/dragon.obj")
  meshes("teapot") = Mesh.load("data/meshes/teapot_test.obj")
  meshes("fence") = Mesh.load("data/meshes/fence.obj")
  meshes("treetop") = Mesh.load("data/meshes/treetop.obj")
  meshes("treetop2") = Mesh.load("data/meshes/treetop2.obj")
  meshes("treetrunk") = Mesh.load("data/meshes/treetrunk.obj")

  shaderPrograms("default") = ShaderProgram.load("data/shaders/default.vert","data/shaders/default.frag")

  val h = 8
  val testmapGrass = mutable.Buffer[(SpatialComponent,ModelComponent)]()
  for(x <- 0 until h;y <- 0 until h){
    val spat = new SpatialComponent()
    spat.setPosition(x,0,y)
    val model = new ModelComponent("cube","grass")
    testmapGrass += ((spat,model))
  }

  var fenceBuffer = mutable.Buffer[(SpatialComponent,ModelComponent)]()
  for(x <- 0 until h){
    val spat = new SpatialComponent()
    spat.setPosition(x,1,-1)
    spat.rotateY(90)
    val model = new ModelComponent("fence","wood")
    val spat2 = new SpatialComponent()
    spat2.setPosition(x,1,h-2)
    spat2.rotateY(90)
    fenceBuffer += ((spat,model))
    fenceBuffer += ((spat2,model))
  }
  for(y <- 0 until h){
    val spat = new SpatialComponent()
    spat.setPosition(0,1,y)
    val model = new ModelComponent("fence","wood")
    val spat2 = new SpatialComponent()
    spat2.setPosition(h-1,1,y)
    fenceBuffer += ((spat,model))
    fenceBuffer += ((spat2,model))
  }

  val treetrunkSpat = new SpatialComponent()
  treetrunkSpat.setPosition(h/2,1,h/2)
  val treetopSpat = new SpatialComponent()
  treetopSpat.setPosition(h/2,2.5f,h/2)

  var tree = Vector[(SpatialComponent,ModelComponent)](
    (treetrunkSpat, new ModelComponent("treetrunk","wood")),
    (treetopSpat, new ModelComponent("treetop","tree")))

  var testMap = testmapGrass ++ fenceBuffer ++ tree
}
