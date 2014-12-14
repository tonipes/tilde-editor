package tilde.game

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.{GL11, GL20, GL15, GL30}
import tilde.log.Log

/**
 * Created by Toni on 13.12.2014.
 */
class Game {
  var indicesCount: Int = 0
  var vaoId: Int = 0
  var vboId: Int = 0
  var vboiId: Int = 0

  def create(): Unit = {
    // Vertices, the order is not important.
    val vertices = Array[Float](
      -0.5f, 0.5f, 0f, 1f,
      -0.5f, -0.5f, 0f, 1f,
      0.5f, -0.5f, 0f, 1f,
      0.5f, 0.5f, 0f, 1f
    )
    // Sending data to OpenGL requires the usage of (flipped) byte buffers
    val verticesBuffer = BufferUtils.createFloatBuffer(vertices.length)
    verticesBuffer.put(vertices)
    verticesBuffer.flip()

    // OpenGL expects to draw vertices in counter clockwise order by default
    val indices = Array[Byte](
      // Left bottom triangle
      0, 1, 2,
      // Right top triangle
      2, 3, 0
    )
    indicesCount = indices.length
    val indicesBuffer = BufferUtils.createByteBuffer(indicesCount)
    indicesBuffer.put(indices)
    indicesBuffer.flip()

    // Create a new Vertex Array Object in memory and select it (bind)
    // A VAO can have up to 16 attributes (VBO's) assigned to it by default
    vaoId = GL30.glGenVertexArrays()
    GL30.glBindVertexArray(vaoId)

    // Create a new Vertex Buffer Object in memory and select it (bind)
    // A VBO is a collection of Vectors which in this case resemble the location of each vertex.
    vboId = GL15.glGenBuffers()
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId)
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW)
    // Put the VBO in the attributes list at index 0
    GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0)
    // Deselect (bind to 0) the VBO
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)

    // Deselect (bind to 0) the VAO
    GL30.glBindVertexArray(0)

    // Create a new VBO for the indices and select it (bind)
    vboiId = GL15.glGenBuffers()
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId)
    GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW)
    // Deselect (bind to 0) the VBO
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0)
  }

  def render(): Unit = {
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)

    // Bind to the VAO that has all the information about the vertices
    GL30.glBindVertexArray(vaoId)
    GL20.glEnableVertexAttribArray(0)

    // Bind to the index VBO that has all the information about the order of the vertices
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId)

    // Draw the vertices
    GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_BYTE, 0)

    // Put everything back to default (deselect)
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0)
    GL20.glDisableVertexAttribArray(0)
    GL30.glBindVertexArray(0)
  }

  var x = 0
  var y = 0
  def update(delta: Float): Unit = {
    GL11.glViewport(x,y,800,600)
    x += 1
    y += 1
    if(x > 100) x = 0
    if(y > 100) y = 0
  }
}
