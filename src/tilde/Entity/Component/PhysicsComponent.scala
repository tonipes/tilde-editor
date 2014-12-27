package tilde.entity.component

import org.lwjgl.util.vector.Vector3f

/**
 * Created by Toni on 26.12.14.
 */
object PhysicsComponent extends ComponentObject{
  val id = classOf[PhysicsComponent]
}

// Super simple physicsComponent for testing
class PhysicsComponent extends Component {
  val bitId = PhysicsComponent.bitID

  val speed = new Vector3f(0,0,0)
  val angularSpeed = new Vector3f(0,0,0)
}
