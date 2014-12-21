package tilde.util

import org.lwjgl.util.vector.Matrix4f


object MatrixUtil {

  def createPerspectiveProjection(zFar: Float, zNear: Float, aspect: Float, fov: Float) = {
    val matrix = new Matrix4f()
    val scaleY = 1f / Math.tan(Math.toRadians(fov / 2f).toFloat)
    val scaleX = scaleY / aspect
    val frusLength = zFar - zNear

    matrix.m00 = scaleX.toFloat
    matrix.m11 = scaleY.toFloat
    matrix.m22 = -((zFar + zNear) / frusLength)
    matrix.m23 = -1
    matrix.m32 = -((2 * zFar * zNear) / frusLength)
    matrix.m33 = 0

    matrix
  }
  def createOrthographicProjection(zFar: Float, zNear: Float, left:Float, right:Float,top:Float,bottom:Float) ={
    val matrix = new Matrix4f()

    matrix.m00 = 2 / (right - left)
    matrix.m11 = 2 / (top - bottom)
    matrix.m22 = -2 / (zFar - zNear)
    matrix.m30 = -(right + left) / (right - left)
    matrix.m31 = -(top + bottom) / (top - bottom)
    matrix.m32 = -(zFar + zNear) / (zFar - zNear)
    matrix.m33 = 1

    matrix
  }
}
