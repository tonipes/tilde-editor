package tilde.Entity.Component

import org.lwjgl.BufferUtils
import org.lwjgl.util.vector.{Matrix4f, Vector3f}

/**
 * Created by Toni on 21.12.14.
 */
object Component{
  val id = getClass()
}
abstract class Component {}

case class SpatialComponent(
  val location: Vector3f = new Vector3f(0.0f, 0.0f, 0.0f),
  val rotation: Vector3f = new Vector3f(0.0f, 0.0f, 0.0f),
  val scale: Vector3f = new Vector3f(1.0f, 1.0f, 1.0f)) extends Component {

  val mat: Matrix4f = new Matrix4f()
  mat.setIdentity()

  def translate(vec: Vector3f): SpatialComponent = translate(vec.x,vec.y,vec.z)
  def translate(x:Float,y:Float,z:Float): SpatialComponent  = {
    Matrix4f.translate(new Vector3f(x,y,z),mat,mat)
    this
  }

  def scale(vec: Vector3f): SpatialComponent  = translate(vec.x,vec.y,vec.z)
  def scale(x:Float,y:Float,z:Float): SpatialComponent  = {
    Matrix4f.scale(new Vector3f(x,y,z),mat,mat)
    this
  }

  def rotate(vec: Vector3f): SpatialComponent  = translate(vec.x,vec.y,vec.z)
  def rotate(x:Float,y:Float,z:Float): SpatialComponent  = {
    Matrix4f.rotate(Math.toRadians(x).toFloat, new Vector3f(1,0,0),mat,mat)
    Matrix4f.rotate(Math.toRadians(y).toFloat, new Vector3f(0,1,0),mat,mat)
    Matrix4f.rotate(Math.toRadians(z).toFloat, new Vector3f(0,0,1),mat,mat)
    this
  }

  def getFloatBuffer() = {
    val buffer = BufferUtils.createFloatBuffer(16)
    mat.store(buffer)
    buffer.rewind()
    buffer
  }

  override def toString() = {
    mat.toString
  }

}