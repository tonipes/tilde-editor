package sgEngine.graphics

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13._
import org.lwjgl.opengl.GL30._

/**
 * Created by Toni on 8.2.2015.
 */
class Framebuffer(val width: Int, val height: Int, textures: String*) {
  val textureMap = textures.map(f => (f,glGenTextures())).toMap
  val framebufferID = glGenFramebuffers()
  val depthBufferID = glGenRenderbuffers()

  val attachments = Array.tabulate[Int](textures.length)(i => GL_COLOR_ATTACHMENT0 + i)

  glBindFramebuffer(GL_FRAMEBUFFER, framebufferID)
  textureMap.foreach(f => {
    val id = f._2
    val index = textures.indexOf(f._1)
    glBindTexture(GL_TEXTURE_2D, id)
      glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR) // GL_NEAREST
      glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
      glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP)
      glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP)
      glGenerateMipmap(GL_TEXTURE_2D)
      glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, null.asInstanceOf[java.nio.ByteBuffer])
    glBindTexture(GL_TEXTURE_2D, 0)
    glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0 + index,GL_TEXTURE_2D, id, 0)
  })

  glBindRenderbuffer(GL_RENDERBUFFER, depthBufferID)
  glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height)
  glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBufferID)
  glBindRenderbuffer(GL_RENDERBUFFER, 0)

  glBindFramebuffer(GL_FRAMEBUFFER, 0)

  if( glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE){
    throw new RuntimeException("Framebuffer setup failed!")
  }

  def bindFramebuffer() = {
    glBindFramebuffer(GL_FRAMEBUFFER, framebufferID)
  }

  def unbind() = {
    glBindFramebuffer(GL_FRAMEBUFFER, 0)
  }

  def activateTextures(shader: ShaderProgram) = {
    for(i <- textures.indices){
      glActiveTexture(GL_TEXTURE0 + i)
      glBindTexture(GL_TEXTURE_2D, textureMap(textures(i)))
      shader.setUniform(textures(i),i)
    }
  }
}
