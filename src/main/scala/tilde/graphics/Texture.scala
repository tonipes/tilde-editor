package tilde.graphics

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13._

/**
 * Created by Toni on 18.12.14.
 */
class Texture(val width: Int, val height: Int, val id: Int) {

  def bind(): Unit = 
    glBindTexture(GL_TEXTURE_2D, id)

  def unbind(): Unit = 
    glBindTexture(GL_TEXTURE_2D, 0)

  def setActiveAsUnit(unit: Int): Unit = 
    glActiveTexture(GL_TEXTURE0 + unit)

  def dispose(): Unit = {
    glDeleteTextures(id)
  }
}
