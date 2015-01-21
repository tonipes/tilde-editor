package tilde

import tilde.graphics._
import tilde.util.ResourceUtil.ResourceUtil
import scala.collection.mutable.Map

/**
 * Created by Toni on 17.1.2015.
 */
object ResourceManager {

  val textures = Map[String, Texture]()
  val meshes = Map[String,Mesh]()
  val shaderPrograms = Map[String, ShaderProgram]()
  val models = Map[String, Model]()
  val materials = Map[String, Material]()

  shaderPrograms("default") =
    ResourceUtil.loadShader("shaders/default.vert","shaders/default.frag")

  meshes("default") =
    ResourceUtil.loadMesh("meshes/box.ply")

  models("default") = ResourceUtil.loadModel("models/default.model")

  materials("default") = ResourceUtil.loadMaterial("materials/default.material")
}
