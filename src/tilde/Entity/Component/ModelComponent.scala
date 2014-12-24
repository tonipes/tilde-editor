package tilde.entity.component

import tilde.ResourceManager

/**
 * Created by Toni on 24.12.14.
 */
object ModelComponent{
  val id = classOf[ModelComponent]
}

class ModelComponent(val mesh: String, val texture: String) extends Component{
  def getMesh = ResourceManager.meshes(mesh)
  def getTexture = ResourceManager.textures(texture)
}
