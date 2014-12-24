package tilde

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
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
  textures("measure") = Texture.load("data/textures/measure_128.png")
  //meshes("dragon") = Mesh.load("data/meshes/dragon.obj")
  meshes("cube") = Mesh.load("data/meshes/cube.obj")
  meshes("teapot") = Mesh.load("data/meshes/teapot_test.obj")
  shaderPrograms("default") = ShaderProgram.load("data/shaders/default.vert","data/shaders/default.frag")

}
