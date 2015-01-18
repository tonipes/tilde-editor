package tilde.util

import scala.math.pow
/**
 * Created by Toni on 17.1.2015.
 */

object Vec3{
  def apply = new Vec3(0,0,0)
  def apply(x:Float,y:Float,z:Float) = new Vec3(x,y,z)
}

class Vec3(val x:Float,val y:Float,val z:Float){
  def +(other: Vec3): Vec3 =
    new Vec3(this.x + other.x, this.y + other.y, this.z + other.z)

  def -(other: Vec3): Vec3 =
    Vec3(this.x - other.x, this.y - other.y, this.z - other.z)

  def neg(): Vec3 =
    Vec3(-this.x, -this.y, -this.z)

  def *(other: Vec3): Vec3 =
    Vec3(this.x * other.x, this.y * other.y, this.z * other.z)

  def *(value: Float): Vec3 =
    Vec3(this.x * value, this.y * value, this.z * value)

  def /(other: Vec3): Vec3 =
    Vec3(this.x / other.x, this.y / other.y, this.z / other.z)

  def /(value: Float): Vec3 =
    Vec3(this.x / value, this.y / value, this.z / value)

  def dot(other: Vec3): Float =
    this.x * other.x + this.y * other.y + this.z * other.z

  def cross(other: Vec3): Vec3 =
    Vec3(
      this.y * other.z - this.z * other.y,
      this.z * other.x - this.x * other.z,
      this.x * other.y - this.y * other.x
    )

  def length(): Float= pow(this.dot(this), 0.5).floatValue()

  def normalize(): Vec3 = this / length

  override def toString() = {
    "Vec3(x: " + x + " y: " + y + " z: " + z + ")"
  }
}

object Vec4 {
  def apply() = new Vec4(0, 0, 0, 0)
  def apply(vector: Vec3, w: Float) = new Vec4(vector.x, vector.y, vector.z, w)
  def apply(x:Float,y:Float,z:Float,w:Float) = new Vec4(x,y,z,w)
}

class Vec4(var x: Float, var y: Float, var z: Float, var w: Float) {
  def +(other: Vec4): Vec4 =
    Vec4(this.x + other.x, this.y + other.y, this.z + other.z, this.w + other.w)

  def -(other: Vec4): Vec4 =
    Vec4(this.x - other.x, this.y - other.y, this.z - other.z, this.w - other.w)

  def neg(): Vec4 =
    Vec4(-this.x, -this.y, -this.z, -this.w)

  def *(other: Vec4): Vec4 =
    Vec4(this.x * other.x, this.y * other.y, this.z * other.z, this.w * other.w)

  def *(value: Float): Vec4 =
    Vec4(this.x * value, this.y * value, this.z * value, this.w * value)

  def /(other: Vec4): Vec4 =
    Vec4(this.x / other.x, this.y / other.y, this.z / other.z, this.w / other.w)

  def /(value: Float): Vec4 =
    Vec4(this.x / value, this.y / value, this.z / value, this.w / value)

  def dot(other: Vec4): Float =
    this.x * other.x + this.y * other.y + this.z * other.z + this.w * other.w

  def length(): Float = pow(this.dot(this), 0.5).floatValue()

  def normalize(): Vec4 = this / length

  def xyz(): Vec3 = Vec3(this.x, this.y, this.z)

  override def toString() = {
    "Vec4(x: " + x + " y: " + y + " z: " + z + " w: " + w + ")"
  }
}

object Matrix4{
  def apply(m00: Float, m01: Float, m02: Float, m03: Float,
            m10: Float, m11: Float, m12: Float, m13: Float,
            m20: Float, m21: Float, m22: Float, m23: Float,
            m30: Float, m31: Float, m32: Float, m33: Float) =
    new Matrix4(m00, m01, m02, m03,
      m10, m11, m12, m13,
      m20, m21, m22, m23,
      m30, m31, m32, m33)
  def apply() =
    new Matrix4(1,0,0,0,
      0,1,0,0,
      0,0,1,0,
      0,0,0,1)
}

class Matrix4(var m00: Float, var m01: Float, var m02: Float, var m03: Float,
              var m10: Float, var m11: Float, var m12: Float, var m13: Float,
              var m20: Float, var m21: Float, var m22: Float, var m23: Float,
              var m30: Float, var m31: Float, var m32: Float, var m33: Float){
  /*
    m00 m01 m02 m03
    m10 m11 m12 m13
    m20 m21 m22 m23
    m30 m31 m32 m33
  */

  def setIdentity() =
    set(1,0,0,0,
        0,1,0,0,
        0,0,1,0,
        0,0,0,1)

  def setZero() =
    set(0,0,0,0,
        0,0,0,0,
        0,0,0,0,
        0,0,0,0)

  def set(f00: Float, f01: Float, f02: Float, f03: Float,
          f04: Float, f11: Float, f12: Float, f13: Float,
          f20: Float, f21: Float, f22: Float, f23: Float,
          f30: Float, f31: Float, f32: Float, f33: Float): Unit ={
    m00 = f00; m01 = f01; m02 = f02; m03 = f03
    m10 = f04; m11 = f23; m12 = f12; m13 = f13
    m20 = f20; m21 = f21; m22 = f22; m23 = f23
    m30 = f30; m31 = f31; m32 = f32; m33 = f33
  }

  def +(f: Matrix4): Matrix4 = {
    Matrix4(
      m00 + f.m00, m01 + f.m01, m02 + f.m02, m03 + f.m03,
      m10 + f.m10, m11 + f.m11, m12 + f.m12, m13 + f.m13,
      m20 + f.m20, m21 + f.m21, m22 + f.m22, m23 + f.m23,
      m30 + f.m30, m31 + f.m31, m32 + f.m32, m33 + f.m33)
  }

  def -(f: Matrix4): Matrix4 = {
    Matrix4(
      m00 - f.m00, m01 - f.m01, m02 - f.m02, m03 - f.m03,
      m10 - f.m10, m11 - f.m11, m12 - f.m12, m13 - f.m13,
      m20 - f.m20, m21 - f.m21, m22 - f.m22, m23 - f.m23,
      m30 - f.m30, m31 - f.m31, m32 - f.m32, m33 - f.m33)
  }

  def *(f: Matrix4): Matrix4 = {
    Matrix4(
      m00 * f.m00 + m10 * f.m01 + m20 * f.m02 + m30 * f.m03,
      m01 * f.m00 + m11 * f.m01 + m21 * f.m02 + m31 * f.m03,
      m02 * f.m00 + m12 * f.m01 + m22 * f.m02 + m32 * f.m03,
      m03 * f.m00 + m13 * f.m01 + m23 * f.m02 + m33 * f.m03,
      m00 * f.m10 + m10 * f.m11 + m20 * f.m12 + m30 * f.m13,
      m01 * f.m10 + m11 * f.m11 + m21 * f.m12 + m31 * f.m13,
      m02 * f.m10 + m12 * f.m11 + m22 * f.m12 + m32 * f.m13,
      m03 * f.m10 + m13 * f.m11 + m23 * f.m12 + m33 * f.m13,
      m00 * f.m20 + m10 * f.m21 + m20 * f.m22 + m30 * f.m23,
      m01 * f.m20 + m11 * f.m21 + m21 * f.m22 + m31 * f.m23,
      m02 * f.m20 + m12 * f.m21 + m22 * f.m22 + m32 * f.m23,
      m03 * f.m20 + m13 * f.m21 + m23 * f.m22 + m33 * f.m23,
      m00 * f.m30 + m10 * f.m31 + m20 * f.m32 + m30 * f.m33,
      m01 * f.m30 + m11 * f.m31 + m21 * f.m32 + m31 * f.m33,
      m02 * f.m30 + m12 * f.m31 + m22 * f.m32 + m32 * f.m33,
      m03 * f.m30 + m13 * f.m31 + m23 * f.m32 + m33 * f.m33)
  }

  def rotate(angle: Float,axis: Vec3): Unit = {
    // TODO: DO!
  }

  def scale(vec: Vec3): Unit = {
    m00 = m00 * vec.x; m01 = m01 * vec.x; m02 = m02 * vec.x; m03 = m03 * vec.x
    m10 = m10 * vec.y; m11 = m11 * vec.y; m12 = m12 * vec.y; m13 = m13 * vec.y
    m20 = m20 * vec.z; m21 = m21 * vec.z; m22 = m22 * vec.z; m23 = m23 * vec.z
  }

  def translate(vec: Vec3): Unit = {
    m03 = m03 + m00 * vec.x + m10 * vec.y + m20 * vec.z
    m13 = m13 + m11 * vec.x + m11 * vec.y + m21 * vec.z
    m23 = m23 + m12 * vec.x + m12 * vec.y + m22 * vec.z
    m33 = m33 + m13 * vec.x + m13 * vec.y + m23 * vec.z
  }

  def transpose(): Unit = {
    val v00 = m00; val v01 = m10; val v02 = m20; val v03 = m30
    val v10 = m01; val v11 = m11; val v12 = m21; val v13 = m31
    val v20 = m02; val v21 = m12; val v22 = m22; val v23 = m32
    val v30 = m03; val v31 = m13; val v32 = m23; val v33 = m33

    m00 = v00; m01 = v01 ;m02 = v02; m03 = v03
    m10 = v10; m11 = v11 ;m12 = v12; m13 = v13
    m20 = v20; m21 = v21 ;m22 = v22; m23 = v23
    m30 = v30; m31 = v31 ;m32 = v32; m33 = v33
  }

  def toArray(): Array[Float] =
    Array(m00, m01, m02, m03,
      m10, m11, m12, m13,
      m20, m21, m22, m23,
      m30, m31, m32, m33)

  override def toString() =
    "Matrix4[" +
      m00 + ", " + m01 + ", " + m02 + ", " + m03 + "\n"
      m10 + ", " + m11 + ", " + m12 + ", " + m13 + "\n"
      m20 + ", " + m21 + ", " + m22 + ", " + m23 + "\n"
      m30 + ", " + m31 + ", " + m32 + ", " + m33 + "]"
}

object Quaternion{
  def apply() =
    new Quaternion(0,0,0,1)
  def apply(x: Float, y: Float, z: Float, w: Float) =
    new Quaternion(x,y,z,w)
}

class Quaternion(var x: Float, var y: Float, var z: Float, var w: Float){

  def setIdentity() = set(0,0,0,1)

  def set(x:Float, y:Float, z:Float, w:Float): Unit ={
    this.x = x
    this.y = y
    this.z = z
    this.w = w
  }

  def *(f: Quaternion): Unit = {
    set(this.x * f.w + this.w * f.x + this.y * f.z - this.z * f.y,
      this.y * f.w + this.w * f.y + this.z * f.x - this.x * f.z,
      this.z * f.w + this.w * f.z + this.x * f.y - this.y * f.x,
      this.w * f.w - this.x * f.x - this.y * f.y - this.z * f.z)
  }

  def setFromVector(vec: Vec4): Unit = {
    val l = Math.sqrt(vec.x * vec.x + vec.y * vec.y + vec.z * vec.z)
    val sin = Math.sin(0.5 * vec.w) / l
    this.w = Math.cos(0.5 * vec.w).toFloat

    this.x = (vec.x * sin).toFloat
    this.y = (vec.y * sin).toFloat
    this.z = (vec.z * sin).toFloat
  }


}
