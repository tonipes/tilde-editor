package tilde.util

import java.nio.{ByteBuffer, IntBuffer, ShortBuffer, FloatBuffer}

import org.lwjgl.BufferUtils

/**
 * Created by Toni on 1.2.2015.
 */
object BufferCreator {

  def createFloatBuffer(m:Matrix4):FloatBuffer = {
    val buff = BufferUtils.createFloatBuffer(16)
    m.toArray().foreach(a => buff.put(a))
    buff.rewind()
    buff
  }

  def createFloatBuffer(m:Vec3):FloatBuffer = {
    val buff = BufferUtils.createFloatBuffer(3)
    buff.put(m.x)
    buff.put(m.y)
    buff.put(m.z)
    buff.rewind()
    buff
  }

  def createFloatBuffer(m:Vec4):FloatBuffer = {
    val buff = BufferUtils.createFloatBuffer(4)
    buff.put(m.x)
    buff.put(m.y)
    buff.put(m.z)
    buff.put(m.w)
    buff.rewind()
    buff
  }

  def createFloatBuffer(f: Float*):FloatBuffer = {
    createFloatBuffer(f.toArray)
  }

  def createFloatBuffer(f: Array[Float]):FloatBuffer = {
    val buff = BufferUtils.createFloatBuffer(f.length)
    f.foreach(a => buff.put(a))
    buff.rewind()
    buff
  }

  def createShortBuffer(s: Short*): ShortBuffer = {
    val buff = BufferUtils.createShortBuffer(s.length)
    s.foreach(a => buff.put(a))
    buff.rewind()
    buff
  }

  def createIntBuffer(s: Int*): IntBuffer =
    createIntBuffer(s.toArray)

  def createIntBuffer(s: Array[Int]): IntBuffer = {
    val buff = BufferUtils.createIntBuffer(s.length)
    s.foreach(a => buff.put(a))
    buff.rewind()
    buff
  }
}
