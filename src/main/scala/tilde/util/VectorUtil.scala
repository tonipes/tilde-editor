package tilde.util

import scala.math.pow
/**
 * Created by Toni on 17.1.2015.
 */
object Vec2{
  def apply(): Vec2                      = new Vec2(0,0)
  def apply(v: Vec2): Vec2                = new Vec2(v.x,v.y)
}

case class Vec2(var x :Float, var y: Float) extends Serializable{
  def +(other: Vec2): Vec2 =
    new Vec2(this.x + other.x, this.y + other.y)

  def += (other: Vec2): Unit = {
    val n = this + other
    x = n.x
    y = n.y
  }

  def -(other: Vec2): Vec2 =
    Vec2(this.x - other.x, this.y - other.y)

  def -= (other: Vec2): Unit = {
    val n = this - other
    x = n.x
    y = n.y
  }

  def neg(): Vec2 =
    Vec2(-this.x, -this.y)

  def *(other: Vec2): Vec2 =
    Vec2(this.x * other.x, this.y * other.y)

  def *= (other: Vec2): Unit = {
    val n = this * other
    x = n.x
    y = n.y
  }

  def *(value: Float): Vec2 =
    Vec2(this.x * value, this.y * value)

  def *= (other: Float): Unit = {
    val n = this * other
    x = n.x
    y = n.y
  }

  def /(other: Vec2): Vec2 =
    Vec2(this.x / other.x, this.y / other.y)

  def /= (other: Vec2): Unit = {
    val n = this / other
    x = n.x
    y = n.y
  }

  def /(value: Float): Vec2 =
    Vec2(this.x / value, this.y / value)

  def /= (value: Float): Unit = {
    val n = this / value
    x = n.x
    y = n.y
  }

  def dot(other: Vec2): Float =
    this.x * other.x + this.y * other.y


  def length(): Float = pow(this.dot(this), 0.5).floatValue()

  def normalise(): Vec2 = this / length

  override def toString() = {
    "Vec2(x: " + x + " y: " + y + ")"
  }
}

object Vec3{
  def apply(): Vec3                        = new Vec3(0,0,0)
  //def apply(x:Float,y:Float,z:Float): Vec3 = new Vec3(x,y,z)
  def apply(v: Vec3): Vec3                 = new Vec3(v.x,v.y,v.z)
}

case class Vec3(var x :Float, var y: Float, var z:Float) extends Serializable{
  def +(other: Vec3): Vec3 =
    new Vec3(this.x + other.x, this.y + other.y, this.z + other.z)

  def += (other: Vec3): Unit = {
    val n = this + other
    x = n.x
    y = n.y
    z = n.z
  }

  def -(other: Vec3): Vec3 =
    Vec3(this.x - other.x, this.y - other.y, this.z - other.z)

  def -= (other: Vec3): Unit = {
    val n = this - other
    x = n.x
    y = n.y
    z = n.z
  }

  def neg(): Vec3 =
    Vec3(-this.x, -this.y, -this.z)

  def *(other: Vec3): Vec3 =
    Vec3(this.x * other.x, this.y * other.y, this.z * other.z)

  def *= (other: Vec3): Unit = {
    val n = this * other
    x = n.x
    y = n.y
    z = n.z
  }

  def *(quat: Quaternion): Vec3 = {
    val num = quat.x * 2f
    val num2 = quat.y * 2f
    val num3 = quat.z * 2f
    val num4 = quat.x * num
    val num5 = quat.y * num2
    val num6 = quat.z * num3
    val num7 = quat.x * num2
    val num8 = quat.x * num3
    val num9 = quat.y * num3
    val num10 = quat.w * num
    val num11 = quat.w * num2
    val num12 = quat.w * num3
    val x = (1f - (num5 + num6)) * this.x + (num7 - num12) * this.y + (num8 + num11) * this.z
    val y = (num7 + num12) * this.x + (1f - (num4 + num6)) * this.y + (num9 - num10) * this.z
    val z = (num8 - num11) * this.x + (num9 + num10) * this.y + (1f - (num4 + num5)) * this.z
//    val x = 2*(other.x*this.z*other.z + other.y*this.z*other.w - other.x*this.y*other.w + other.y*this.y*other.z) + this.x*(other.x*other.x + other.y*other.y - other.z*other.z - other.w*other.w)
//    val y = 2*(other.x*this.x*other.w + other.y*this.x*other.z - other.x*this.z*other.y + other.z*this.z*other.w) + this.y*(other.x*other.x - other.y*other.y + other.z*other.z - other.w*other.w)
//    val z = 2*(other.x*this.y*other.y - other.x*this.x*other.z + other.y*this.x*other.w + other.z*this.y*other.w) + this.z*(other.x*other.x - other.y*other.y - other.z*other.z + other.w*other.w)
    Vec3(x,y,z)
  }

  def *= (other: Quaternion): Unit = {
    val n = this * other
    x = n.x
    y = n.y
    z = n.z
  }

  def *(value: Float): Vec3 =
    Vec3(this.x * value, this.y * value, this.z * value)

  def *= (other: Float): Unit = {
    val n = this * other
    x = n.x
    y = n.y
    z = n.z
  }

  def /(other: Vec3): Vec3 =
    Vec3(this.x / other.x, this.y / other.y, this.z / other.z)

  def /= (other: Vec3): Unit = {
    val n = this / other
    x = n.x
    y = n.y
    z = n.z
  }

  def /(value: Float): Vec3 =
    Vec3(this.x / value, this.y / value, this.z / value)

  def /= (value: Float): Unit = {
    val n = this / value
    x = n.x
    y = n.y
    z = n.z
  }

  def dot(other: Vec3): Float =
    this.x * other.x + this.y * other.y + this.z * other.z

  def cross(other: Vec3): Vec3 =
    Vec3(
      this.y * other.z - this.z * other.y,
      this.z * other.x - this.x * other.z,
      this.x * other.y - this.y * other.x
    )

  def length(): Float = pow(this.dot(this), 0.5).floatValue()

  def normalise(): Vec3 = this / length

  override def toString() = {
    "Vec3(x: " + x + " y: " + y + " z: " + z + ")"
  }
}

case object Vec4 {
  //def apply(x:Float,y:Float,z:Float,w:Float): Vec4  = new Vec4(x,y,z,w)
  def apply(): Vec4                                 = Vec4(0, 0, 0, 0)
  def apply(vector: Vec3, w: Float): Vec4           = Vec4(vector.x, vector.y, vector.z, w)
  def apply(v: Vec4): Vec4                          = Vec4(v.x,v.y,v.z,v.w)
}

case class Vec4(var x: Float, var y: Float, var z: Float, var w: Float) extends Serializable {
  def +(other: Vec4): Vec4 =
    Vec4(this.x + other.x, this.y + other.y, this.z + other.z, this.w + other.w)

  def += (other: Vec4): Unit = {
    val n = this + other
    x = n.x; y = n.y; z = n.z; w = n.w;
  }

  def -(other: Vec4): Vec4 =
    Vec4(this.x - other.x, this.y - other.y, this.z - other.z, this.w - other.w)

  def -= (other: Vec4): Unit = {
    val n = this - other
    x = n.x; y = n.y; z = n.z; w = n.w;
  }

  def neg(): Vec4 =
    Vec4(-this.x, -this.y, -this.z, -this.w)

  def *(other: Vec4): Vec4 =
    Vec4(this.x * other.x, this.y * other.y, this.z * other.z, this.w * other.w)

  def *= (other: Vec4): Unit = {
    val n = this * other
    x = n.x; y = n.y; z = n.z; w = n.w;
  }

  def *(value: Float): Vec4 =
    Vec4(this.x * value, this.y * value, this.z * value, this.w * value)

  def *= (value: Float): Unit = {
    val n = this * value
    x = n.x; y = n.y; z = n.z; w = n.w;
  }

  def /(other: Vec4): Vec4 =
    Vec4(this.x / other.x, this.y / other.y, this.z / other.z, this.w / other.w)

  def /= (value: Vec4): Unit = {
    val n = this / value
    x = n.x; y = n.y; z = n.z; w = n.w;
  }

  def /(value: Float): Vec4 =
    Vec4(this.x / value, this.y / value, this.z / value, this.w / value)

  def dot(other: Vec4): Float =
    this.x * other.x + this.y * other.y + this.z * other.z + this.w * other.w

  def length(): Float = pow(this.dot(this), 0.5).floatValue()

  def normalise(): Vec4 = this / length

  def xyz(): Vec3 = Vec3(this.x, this.y, this.z)

  override def clone(): Vec3 = Vec3(x,y,z)

  override def toString() = {
    "Vec4(x: " + x + " y: " + y + " z: " + z + " w: " + w + ")"
  }
}

object Matrix4{
//  def apply(m00: Float, m01: Float, m02: Float, m03: Float,
//            m10: Float, m11: Float, m12: Float, m13: Float,
//            m20: Float, m21: Float, m22: Float, m23: Float,
//            m30: Float, m31: Float, m32: Float, m33: Float): Matrix4 =
//    new Matrix4(m00, m01, m02, m03,
//      m10, m11, m12, m13,
//      m20, m21, m22, m23,
//      m30, m31, m32, m33)

  def apply(): Matrix4  =
    new Matrix4(1,0,0,0,
      0,1,0,0,
      0,0,1,0,
      0,0,0,1)

  def apply(m: Matrix4): Matrix4  =
    new Matrix4(m.m00, m.m01, m.m02, m.m03,
                m.m10, m.m11, m.m12, m.m13,
                m.m20, m.m21, m.m22, m.m23,
                m.m30, m.m31, m.m32, m.m33)

  def getScale(v: Vec3) = {
    new Matrix4(
      v.x, 0, 0, 0,
      0, v.y, 0, 0,
      0, 0, v.z, 0,
      0, 0, 0, 1)
  }

  def getTransformation(v: Vec3) = {
    new Matrix4(
      1, 0, 0, v.x,
      0, 1, 0, v.y,
      0, 0, 1, v.z,
      0, 0, 0, 1)
  }

}

case class Matrix4(var m00: Float, var m01: Float, var m02: Float, var m03: Float,
              var m10: Float, var m11: Float, var m12: Float, var m13: Float,
              var m20: Float, var m21: Float, var m22: Float, var m23: Float,
              var m30: Float, var m31: Float, var m32: Float, var m33: Float){
  /*
    m00 m01 m02 m03
    m10 m11 m12 m13
    m20 m21 m22 m23
    m30 m31 m32 m33
  */

  def setIdentity(): Matrix4 ={
    set(1,0,0,0,
        0,1,0,0,
        0,0,1,0,
        0,0,0,1)
    this
  }

  def setZero(): Matrix4 ={
    set(0,0,0,0,
        0,0,0,0,
        0,0,0,0,
        0,0,0,0)
    this
  }

  def set(f00: Float, f01: Float, f02: Float, f03: Float,
          f10: Float, f11: Float, f12: Float, f13: Float,
          f20: Float, f21: Float, f22: Float, f23: Float,
          f30: Float, f31: Float, f32: Float, f33: Float): Unit ={
    m00 = f00; m01 = f01; m02 = f02; m03 = f03
    m10 = f10; m11 = f11; m12 = f12; m13 = f13
    m20 = f20; m21 = f21; m22 = f22; m23 = f23
    m30 = f30; m31 = f31; m32 = f32; m33 = f33
  }

  def set(m: Matrix4): Unit = {
    set(m.m00,m.m01,m.m02,m.m03,
        m.m10,m.m11,m.m12,m.m13,
        m.m20,m.m21,m.m22,m.m23,
        m.m30,m.m31,m.m32,m.m33)
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
      m00 * f.m00	+ m01 * f.m10 + m02 * f.m20 + m03 * f.m30,
      m00 * f.m01	+ m01 * f.m11 + m02 * f.m21 + m03 * f.m31,
      m00 * f.m02	+ m01 * f.m12 + m02 * f.m22 + m03 * f.m32,
      m00 * f.m03	+ m01 * f.m13 + m02 * f.m23 + m03 * f.m33,

      m10 * f.m00	+ m11 * f.m10 + m12 * f.m20 + m13 * f.m30,
      m10 * f.m01	+ m11 * f.m11 + m12 * f.m21 + m13 * f.m31,
      m10 * f.m02	+ m11 * f.m12 + m12 * f.m22 + m13 * f.m32,
      m10 * f.m03	+ m11 * f.m13 + m12 * f.m23 + m13 * f.m33,

      m20 * f.m00	+ m21 * f.m10 + m22 * f.m20 + m23 * f.m30,
      m20 * f.m01	+ m21 * f.m11 + m22 * f.m21 + m23 * f.m31,
      m20 * f.m02	+ m21 * f.m12 + m22 * f.m22 + m23 * f.m32,
      m20 * f.m03	+ m21 * f.m13 + m22 * f.m23 + m23 * f.m33,

      m30 * f.m00	+ m31 * f.m10 + m32 * f.m20 + m33 * f.m30,
      m30 * f.m01	+ m31 * f.m11 + m32 * f.m21 + m33 * f.m31,
      m30 * f.m02	+ m31 * f.m12 + m32 * f.m22 + m33 * f.m32,
      m30 * f.m03	+ m31 * f.m13 + m32 * f.m23 + m33 * f.m33)
  }

  def rotate(angle: Float, axis: Vec3): Unit = {
    // TODO: Can be optimized
    val mat = this * Quaternion.fromAxisAngle(axis,angle).rotationMatrix()
  }

  def transpose(): Matrix4 = {
    val v00 = m00; val v01 = m10; val v02 = m20; val v03 = m30
    val v10 = m01; val v11 = m11; val v12 = m21; val v13 = m31
    val v20 = m02; val v21 = m12; val v22 = m22; val v23 = m32
    val v30 = m03; val v31 = m13; val v32 = m23; val v33 = m33

    m00 = v00; m01 = v01 ;m02 = v02; m03 = v03
    m10 = v10; m11 = v11 ;m12 = v12; m13 = v13
    m20 = v20; m21 = v21 ;m22 = v22; m23 = v23
    m30 = v30; m31 = v31 ;m32 = v32; m33 = v33

    this
  }

  def toArray(): Array[Float] =
    Array(m00, m01, m02, m03,
      m10, m11, m12, m13,
      m20, m21, m22, m23,
      m30, m31, m32, m33)

  override def clone(): Matrix4 =
    Matrix4(m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33)

  override def toString() =
    "Matrix4[" + "\n" +
      m00 + ", " + m01 + ", " + m02 + ", " + m03 + "\n" +
      m10 + ", " + m11 + ", " + m12 + ", " + m13 + "\n" +
      m20 + ", " + m21 + ", " + m22 + ", " + m23 + "\n" +
      m30 + ", " + m31 + ", " + m32 + ", " + m33 + "]"
}

object Quaternion{
//  def apply(x: Float, y: Float, z: Float, w: Float): Quaternion =
//    new Quaternion(x,y,z,w)

  def apply(): Quaternion =
    Quaternion(0,0,0,1)

  def apply(q: Quaternion): Quaternion =
    Quaternion(q.x,q.y,q.z,q.w)

  def fromAxisAngle(axis: Vec3, angle: Float) = {
    val normAxis = axis.normalise()
    val angleHalf = Math.toRadians((angle/2f)).toFloat
    val sin = Math.sin(angleHalf).toFloat
    Quaternion(
      axis.x * sin,
      axis.y * sin,
      axis.z * sin,
      Math.cos(angleHalf).toFloat
    ).normalise()
  }

  def fromVector(vec: Vec4): Quaternion = {
    val q = Quaternion()
    val l = Math.sqrt(vec.x * vec.x + vec.y * vec.y + vec.z * vec.z)
    val sin = Math.sin(0.5 * vec.w) / l
    q.w = Math.cos(0.5 * vec.w).toFloat

    q.x = (vec.x * sin).toFloat
    q.y = (vec.y * sin).toFloat
    q.z = (vec.z * sin).toFloat
    q
  }

//  def conjugate(quat: Quaternion) = {
//    val dest = Quaternion()
//    dest.x = -quat.x
//    dest.y = -quat.y
//    dest.z = -quat.z
//    dest.w =  quat.w
//
//    dest
//  }

//  def rotate(vec: Vec3, q:Quaternion) = {
//    val dest = Vec3()
//    val qC = conjugate(q)
//    val qVec = new Quaternion(vec.x,vec.y,vec.z,1)
//
////    Quaternion.mul(q, qVec, qVec)
////    Quaternion.mul(qVec, qC, qVec)
////
////    dest.x = qVec.x
////    dest.y = qVec.y
////    dest.z = qVec.z
//
//    dest
//  }
}

case class Quaternion(var x: Float, var y: Float, var z: Float, var w: Float) extends Serializable{

  def setIdentity() = set(0,0,0,1)

  def set(x:Float, y:Float, z:Float, w:Float): Unit = {
    this.x = x
    this.y = y
    this.z = z
    this.w = w
  }

  def *(other: Quaternion): Quaternion = {
//    Quaternion(
//      this.x * f.w + this.w * f.x + this.y * f.z - this.z * f.y,
//      this.y * f.w + this.w * f.y + this.z * f.x - this.x * f.z,
//      this.z * f.w + this.w * f.z + this.x * f.y - this.y * f.x,
//      this.w * f.w - this.x * f.x - this.y * f.y - this.z * f.z)
  Quaternion(
    this.w * other.x + this.x * other.w + this.y * other.z - this.z * other.y,
    this.w * other.y + this.y * other.w + this.z * other.x - this.x * other.z,
    this.w * other.z + this.z * other.w + this.x * other.y - this.y * other.x,
    this.w * other.w - this.x * other.x - this.y * other.y - this.z * other.z)
  }

  def *=(f: Quaternion) = {
    val a = this * f
    x = a.x
    y = a.y
    z = a.z
    w = a.w
  }

  def normalise(): Quaternion = {
    val len = this.length()
    Quaternion(this.x / len, this.y / len, this.z / len, this.w / len)
  }


  def length(): Float = {
    pow(this.dot(this), 0.5).floatValue()
  }

  def dot(f: Quaternion): Float =
    this.x * f.x + this.y * f.y + this.z * f.z + this.w * f.w

  def rotationMatrix(): Matrix4 ={
    val d = Matrix4()
    d.setIdentity()

    val q = this.normalise()
    //println(s"q = $this")
    //val mul: Float = 2f / q.length()
    val mul = 2f

    d.m00 = 1 - mul * ((q.y * q.y) + (q.z * q.z)) // +
    d.m01 =     mul * ((q.x * q.y) - (q.w * q.z)) // +
    d.m02 =     mul * ((q.x * q.z) + (q.w * q.y)) // -

    d.m10 =     mul * ((q.x * q.y) + (q.w * q.z)) // -
    d.m11 = 1 - mul * ((q.x * q.x) + (q.z * q.z)) // +
    d.m12 =     mul * ((q.y * q.z) - (q.w * q.x)) // +

    d.m20 =     mul * ((q.x * q.z) - (q.w * q.y)) // +
    d.m21 =     mul * ((q.y * q.z) + (q.w * q.x)) // -
    d.m22 = 1 - mul * ((q.x * q.x) + (q.y * q.y)) // +

    d.m33 = 1

    d
  }

  def getConjugate(): Quaternion = {
    val d = Quaternion()
    d.x = -this.x
    d.y = -this.y
    d.z = -this.z
    d.w =  this.w

    d
  }

  def getForward(): Vec3 = {
    Vec3(
           2f * (x * z + w * y),
           2f * (y * z - w * x),
      1f - 2f * (x * x + y * y))
  }

  def getUp(): Vec3 = {
    Vec3(
           2f * (x * y - w * z),
      1f - 2f * (x * x + z * z),
           2f * (y * z + w * x))
  }

  def getRight(): Vec3 = {
    Vec3( 1f - 2f * (y * y + z * z),
          2f * (x * y + w * z),
          2f * (x * z - w * y))
  }

  override def toString() = {
    "Quaternion(x: " + x + " y: " + y + " z: " + z + " w: " + w + ")"
  }
}
