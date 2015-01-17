package tilde

import tilde.graphics.{Model, ShaderProgram, Mesh, Texture}

/**
 * Created by Toni on 17.1.2015.
 */
object ResourceManager {
  val textures = Map[String, Texture]()
  val meshes = Map[String,Mesh]()
  val shaderPrograms = Map[String, ShaderProgram]()
  val models = Map[String, Model]()
}
