package tilde.util

import java.nio.FloatBuffer

import org.lwjgl.BufferUtils
import org.lwjgl.util.vector._

/**
 * Created by Toni on 1.1.15.
 */
object BufferUtil {

 def createFloatBuffer(m:Matrix4f):FloatBuffer = {
   val buff = BufferUtils.createFloatBuffer(16)
   m.store(buff)
   buff.rewind()
   buff
 }

  def createFloatBuffer(m:Matrix3f):FloatBuffer = {
    val buff = BufferUtils.createFloatBuffer(9)
    m.store(buff)
    buff.rewind()
    buff
  }

  def createFloatBuffer(m:Vector2f):FloatBuffer = {
    val buff = BufferUtils.createFloatBuffer(2)
    m.store(buff)
    buff.rewind()
    buff
  }
  def createFloatBuffer(m:Vector3f):FloatBuffer = {
    val buff = BufferUtils.createFloatBuffer(3)
    m.store(buff)
    buff.rewind()
    buff
  }
  def createFloatBuffer(m:Vector4f):FloatBuffer = {
    val buff = BufferUtils.createFloatBuffer(4)
    m.store(buff)
    buff.rewind()
    buff
  }
}
