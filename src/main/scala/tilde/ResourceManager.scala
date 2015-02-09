package tilde

import tilde.graphics._
import tilde.util.{Matrix4, ProjectionFactory, ResourceUtil}
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

  shaderPrograms("passthrough") =
    ResourceUtil.loadShader("shaders/passthrough.vert", "shaders/passthrough.frag")

  shaderPrograms("grid") =
    ResourceUtil.loadShader("shaders/grid.vert", "shaders/grid.frag")

  //textures("measure") = ResourceUtil.loadTexture("textures/measure.png")
  //textures("measure_128") = ResourceUtil.loadTexture("textures/measure_128.png")

  val load_model:PartialFunction[Component, Unit] = {
    case q: ModelComponent => {
      // TODO: Make sure to check if already loaded before loading. What if using textures?
      val model   = ResourceUtil.loadModel(q.model)
      val mesh     = ResourceUtil.loadMesh(model.mesh)
      val material = ResourceUtil.loadMaterial(model.material)
      models(q.model)           = model
      meshes(model.mesh)        = mesh
      materials(model.material) = material
    }
  }

  val load_else: PartialFunction[Component, Unit] = { case _ =>  }

  val loadComponentData = load_model orElse load_else

  /**
   * Loads map and all resources
   * @param path Path to map file
   */
  def loadMap(path: String): Vector[EntityData] = {
    val map = ResourceUtil.loadMap(path)

    // For each component in each entity, load component data
    map.foreach(e => {
      e.components.foreach(c => {
        loadComponentData(c)
      })
    })
    map
  }

  def dispose() = {
    textures.foreach(a => a._2.dispose())
    textures.clear()

    meshes.foreach(a => a._2.dispose())
    meshes.clear()

    shaderPrograms.foreach(a => a._2.dispose())
    shaderPrograms.clear()
  }

  val getProjection = ProjectionFactory.createPerspectiveProjection(1000f,0.01f, 1/1f,75f)
}
