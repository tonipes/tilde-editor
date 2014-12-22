package tilde.util

import org.lwjgl.util.vector.Vector3f

/**
 * Created by Toni on 22.12.14.
 */
object Direction extends Enumeration {
  val AXIS_X = new Vector3f(1,0,0)
  val AXIS_Y = new Vector3f(0,1,0)
  val AXIS_Z = new Vector3f(0,0,1)
  type Direction = Value
  val FORWARD,
      BACKWARD,
      RIGHT,
      LEFT,
      UP,
      DOWN = Value
}
