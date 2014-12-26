package tilde.entity.component

import tilde.ResourceManager
import tilde.entity.ComponentIDs

/**
 * Created by Toni on 24.12.14.
 */
object ModelComponent extends ComponentObject{
  val id = classOf[ModelComponent]
}

class ModelComponent(val mesh: String, val texture: String) extends Component{
  val bitId = ModelComponent.bitID
  def getMesh = ResourceManager.meshes(mesh)
  def getTexture = ResourceManager.textures(texture)
  def == (other:ModelComponent) = this.mesh == other.mesh && this.texture == other.texture
}
