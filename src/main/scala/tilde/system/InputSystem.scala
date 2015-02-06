package tilde.system

import tilde.Entity
import tilde.systems.EntitySystem
import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw._
import scala.collection.immutable.BitSet

/**
 * Created by Toni on 5.2.2015.
 */

class InputSystem extends LogicSystem{
  override def processSystem(delta: Float): Unit = {
    val camera = world.getTagged("camera")
  }

}
