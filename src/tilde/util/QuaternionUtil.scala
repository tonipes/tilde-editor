package tilde.util

import org.lwjgl.util.vector.{Vector3f, Matrix4f, Quaternion}

/*
Functions from
https://github.com/sriharshachilakapati/LWJGL-Tutorial-Series/blob/ffef3d66a212d3ad55859ffe4043cb9778978787/src/com/shc/tutorials/lwjgl/util/QuaternionUtil.java
*/

object QuaternionUtil {

  def rotationMatrix(q: Quaternion, dest: Matrix4f = new Matrix4f) = {
    //val dest = new Matrix4f()
    dest.setIdentity()

    q.normalise()
    val s: Float = 2f / q.length()

    dest.m00 = 1 - s * (q.y * q.y + q.z * q.z)
    dest.m10 = s * (q.x * q.y + q.w * q.z)
    dest.m20 = s * (q.x * q.z - q.w * q.y)

    dest.m01 = s * (q.x * q.y - q.w * q.z)
    dest.m11 = 1 - s * (q.x * q.x + q.z * q.z)
    dest.m21 = s * (q.y * q.z + q.w * q.x)

    dest.m02 = s * (q.x * q.z + q.w * q.y)
    dest.m12 = s * (q.y * q.z - q.w * q.x)
    dest.m22 = 1 - s * (q.x * q.x + q.y * q.y)

    dest
  }

  def conjugate(q: Quaternion, dest: Quaternion = new Quaternion()) = {
    //val dest = new Quaternion
    dest.x = -q.x
    dest.y = -q.y
    dest.z = -q.z
    dest.w = q.w

    dest
  }

  def rotate(vec: Vector3f, q:Quaternion, dest: Vector3f = new Vector3f()) = {

    val qC = conjugate(q)
    val qVec = new Quaternion(vec.x,vec.y,vec.z,1)

    Quaternion.mul(q, qVec, qVec)
    Quaternion.mul(qVec, qC, qVec)

    dest.x = qVec.x
    dest.y = qVec.y
    dest.z = qVec.z

    dest
  }

  def quatFromAxis(axis: Vector3f, angle: Float, dest: Quaternion = new Quaternion()) = {
    // TODO: Does destination matter???

    axis.normalise()

    val angleHalf = Math.toRadians((angle/2f)).toFloat

    dest.x = axis.x * Math.sin(angleHalf).toFloat
    dest.y = axis.y * Math.sin(angleHalf).toFloat
    dest.z = axis.z * Math.sin(angleHalf).toFloat

    dest.w = Math.cos(angleHalf).toFloat
    dest.normalise()

    dest
  }

  def getForward(q: Quaternion) = {
    new Vector3f( 2f * (q.x * q.z + q.w * q.y),
                  2f * (q.y * q.z - q.w * q.x),
              1f - 2f * (q.x * q.x + q.y * q.y))
  }

  def getUp(q: Quaternion) = {
    new Vector3f( 2f * (q.x * q.y - q.w * q.z),
              1f - 2f * (q.x * q.x + q.z * q.z),
                  2f * (q.y * q.z + q.w * q.x))
  }

  def getRight(q: Quaternion) = {
    new Vector3f( 1f - 2f * (q.y * q.y + q.z * q.z),
                      2f * (q.x * q.y + q.w * q.z),
                      2f * (q.x * q.z - q.w * q.y))
  }
}
