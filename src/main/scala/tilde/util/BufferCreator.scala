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

}
