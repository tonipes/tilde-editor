package tilde.graphics

import java.awt.image.BufferedImage

import org.lwjgl.BufferUtils
import org.lwjgl.util.vector.Matrix4f
import tilde.ResourceManager
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13._
import org.lwjgl.opengl.GL30._

/**
 * Created by Toni on 18.12.14.
 */

object Texture {

  def load(name: String): Texture = {

    val bufImage: BufferedImage = ResourceManager.readImegeFromFile(name)

    val pixels = Array.ofDim[Int](bufImage.getWidth() * bufImage.getHeight())

    bufImage.getRGB(0, 0, bufImage.getWidth(), bufImage.getHeight(), pixels, 0, bufImage.getWidth())

    val bytes = BufferUtils.createByteBuffer(pixels.length * 4)

    for (x <- 0 until bufImage.getWidth; y <- 0 until bufImage.getHeight) {
      //val pix = pixels(y * bufImage.getWidth() + x)
      val pix = pixels(y * bufImage.getWidth() + x)

      bytes.put(((pix >> 16) & 0xFF).toByte) // RED
      bytes.put(((pix >> 8) & 0xFF).toByte) // GREEN
      bytes.put((pix & 0xFF).toByte) // BLUE
      bytes.put(((pix >> 24) & 0xFF).toByte) // ALPHA
    }
    bytes.rewind()

    val texID: Int = glGenTextures()

    glBindTexture(GL_TEXTURE_2D, texID)

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, bufImage.getWidth(), bufImage.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, bytes)

    glGenerateMipmap(GL_TEXTURE_2D)

    new Texture(bufImage.getWidth, bufImage.getHeight, texID)
  }

}

class Texture(val width: Int, val height: Int, val texID: Int) {

  def bind() = {
    glBindTexture(GL_TEXTURE_2D, texID)
  }

  def unbind() = {
    glBindTexture(GL_TEXTURE_2D, 0)
  }

  def setActiveAsUnit(unit: Int) = {
    glActiveTexture(GL_TEXTURE0)
  }
}
