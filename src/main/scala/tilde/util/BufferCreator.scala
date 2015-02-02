package tilde.util

import java.nio.FloatBuffer

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
}
