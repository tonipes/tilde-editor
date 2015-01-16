package app.main

import java.nio.FloatBuffer

import org.lwjgl.BufferUtils
import org.lwjgl.util.vector.{Vector3f, Matrix4f, Vector4f}
import tilde.util._
import spray.json._


object TestMain {

  def main(args: Array[String]): Unit = {
    val v = new Vector3f(2,7,4)
    val m = new Matrix4f()
    m.setIdentity()
    m.scale(new Vector3f(2,3,7))
    m.translate(new Vector3f(5,5,5))

    val f = new Matrix4f()
    f.setIdentity()
    f.scale(new Vector3f(5,1,3))
    f.translate(new Vector3f(9,1,3))

    println(Matrix4f.add(m,f,null))
    println(" - ")
    println(aaa(m,f))


  }
  def aaa(m:Matrix4f,f: Matrix4f) = {
    val a = BufferUtils.createFloatBuffer(16)

    a.put(Array(
      m.m00 + f.m00, m.m01 + f.m01, m.m02 + f.m02, m.m03 + f.m03,
      m.m10 + f.m10, m.m11 + f.m11, m.m12 + f.m12, m.m13 + f.m13,
      m.m20 + f.m20, m.m21 + f.m21, m.m22 + f.m22, m.m23 + f.m23,
      m.m30 + f.m30, m.m31 + f.m31, m.m32 + f.m32, m.m33 + f.m33))
    a.rewind()
    new Matrix4f().load(a)
  }
}
