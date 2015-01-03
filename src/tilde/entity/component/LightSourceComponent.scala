package tilde.entity.component

import org.lwjgl.util.vector.Vector4f

/**
 * Created by Toni on 1.1.15.
 */
object LightSourceComponent extends ComponentObject {
  val id = classOf[LightSourceComponent]
}

// TODO: Make support other lighning data
class LightSourceComponent(val color: Vector4f) extends Component {
  val bitId: Int = LightSourceComponent.bitID
}
