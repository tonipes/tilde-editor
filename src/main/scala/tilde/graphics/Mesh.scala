package tilde.graphics

import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL30._

/**
 * Created by Toni on 22.12.14.
 */

class Mesh( val vaoID: Int, val dataID: Int, val elemID: Int ,
            val elemCount: Int, val dataCount: Int) {

  def bindVAO(): Unit = 
    glBindVertexArray(vaoID)

  def unbindVAO(): Unit = 
    glBindVertexArray(0)

  def bindData(): Unit = 
    glBindBuffer(GL_ARRAY_BUFFER,dataID)

  def bindElem(): Unit = 
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,elemID)

  def bufferData(): Unit = 
    glBufferData(GL_ARRAY_BUFFER, dataID,GL_STATIC_DRAW)

  def bufferElem(): Unit = 
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, elemID,GL_STATIC_DRAW)

  def unbind(): Unit = 
    glBindBuffer(GL_ARRAY_BUFFER,0)

  def dispose(): Unit = {
    // TODO: Is unbinding good idea?
    unbind()
    glDeleteBuffers(dataID)
    glDeleteBuffers(elemID)
    glDeleteVertexArrays(vaoID)
  }
}
