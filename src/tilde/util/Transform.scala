package tilde.util

import org.lwjgl.BufferUtils
import org.lwjgl.util.vector.{Vector3f, Matrix4f}

class Transform {
  val mat: Matrix4f = new Matrix4f()
  mat.setIdentity()

  def translate(x:Float,y:Float,z:Float) = {
    Matrix4f.translate(new Vector3f(x,y,z),mat,mat)
    this
  }

  def scale(x:Float,y:Float,z:Float) = {
    Matrix4f.scale(new Vector3f(x,y,z),mat,mat)
    this
  }

  def rotate(x:Float,y:Float,z:Float) = {
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
