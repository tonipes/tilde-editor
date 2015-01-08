package tilde.entity.component

import org.lwjgl.util.vector.Vector4f
import tilde.entity.{ComponentObject, Component}

/**
 * Created by Toni on 1.1.15.
 */
object LightSourceComponent extends ComponentObject {
  val id = classOf[LightSourceComponent]
}

// TODO: Make support other lighning data
class LightSourceComponent( val ambient: Vector4f,
                            val diffuse: Vector4f,
                            val specular: Vector4f,
                            val constantAttenuation: Float,
                            val linearAttenuation: Float,
                            val quadraticAttenuation: Float ) extends Component {
  def this(color: Vector4f,attenuation: Float) = this(color,color,color,attenuation,attenuation,attenuation)
  val bitId: Int = LightSourceComponent.bitID
}
