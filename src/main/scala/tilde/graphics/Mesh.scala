package tilde.graphics

import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL30._

/**
 * Created by Toni on 22.12.14.
 */

class Mesh( val vaoID: Int, val dataId: Int, val elemId: Int , 
			val elemCount: Int, val dataCount: Int) {

  def bindVAO(): Unit = glBindVertexArray(vaoID)

  def unbindVAO(): Unit = glBindVertexArray(0)

  def bindData(): Unit = glBindBuffer(GL_ARRAY_BUFFER,dataId)

  def bindElem(): Unit = glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,elemId)

  def bufferData(): Unit = glBufferData(GL_ARRAY_BUFFER, dataId,GL_STATIC_DRAW)

  def bufferElem(): Unit = glBufferData(GL_ELEMENT_ARRAY_BUFFER, elemId,GL_STATIC_DRAW)

  def unbind(): Unit = glBindBuffer(GL_ARRAY_BUFFER,0)

}
