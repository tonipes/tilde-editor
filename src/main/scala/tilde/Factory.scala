package tilde

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import tilde.graphics.Mesh
import tilde.util.{Vec3, BufferCreator}

/**
 * Created by Toni on 8.2.2015.
 */
object Factory {

  def createScreenMesh(): Mesh = {
    val a = 1f
    val vertCount = 4
    val elemCount = 6
    val vertData = BufferCreator.createFloatBuffer(
      -a,  a, 0f, 0f, 1f, // top left
      -a, -a, 0f, 0f, 0f, // bottom left
       a, -a, 0f, 1f, 0f, // bottom right
       a,  a, 0f, 1f, 1f) // top right

    val elemData = BufferCreator.createIntBuffer(0,1,2,0,2,3)

    val vaoID = glGenVertexArrays()
    val vertexDataID = glGenBuffers()
    val elementID = glGenBuffers()

    glBindVertexArray(vaoID)

    glBindBuffer(GL_ARRAY_BUFFER, vertexDataID)
    glBufferData(GL_ARRAY_BUFFER, vertData, GL_STATIC_DRAW)

    glVertexAttribPointer(0, 3, GL_FLOAT, false, 5*4, 0)
    glVertexAttribPointer(1, 2, GL_FLOAT, false, 5*4, 3*4)

    glBindBuffer(GL_ARRAY_BUFFER, 0)

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,elementID)
    glBufferData(GL_ELEMENT_ARRAY_BUFFER,elemData,GL_STATIC_DRAW)
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0)

    glBindVertexArray(0)

    new Mesh(vaoID,vertexDataID,elementID,elemCount,vertCount)
  }

  def createGridMesh(width:Float,height:Float,divX:Int,divY:Int)= {
    val col = Vec3(0.5f,0.5f,0.5f)
    val halfWidth = width/2f
    val halfHeight = width/2f

    val xDivSize = width/divX
    val yDivSize = height/divY

    val xValues = Vector.tabulate(divX+1)(i => -halfWidth + i*xDivSize)
    val yValues = Vector.tabulate(divY+1)(i => -halfHeight + i*yDivSize)

    val elemCount =  (divX+1 + divY+1) * 2
    //val elemCount =  2
    val vertCount = (xValues.length*yValues.length) * 6

    val vertData = BufferUtils.createFloatBuffer(vertCount)
    val elemData = BufferUtils.createIntBuffer(elemCount)

    // Vertices
    for(x <- xValues; y <- yValues){
      // Location
      vertData.put(x)
      vertData.put(0)
      vertData.put(y)

      vertData.put(col.x)
      vertData.put(col.y)
      vertData.put(col.y)
    }
    vertData.rewind()

    // Elements

    for(x <- xValues.indices){ // Vertical lines
      val first = x*(divY+1)
      elemData.put(first)
      elemData.put(first + (divY))
    }
    for(y <- yValues.indices){ // Vertical lines
    val first = y
      elemData.put(first)
      elemData.put(first + (divY+1)*divX)
    }
    elemData.rewind()

    val vaoID = glGenVertexArrays()
    val vertexDataID = glGenBuffers()
    val elementID = glGenBuffers()

    glBindVertexArray(vaoID)

    glBindBuffer(GL_ARRAY_BUFFER, vertexDataID)
    glBufferData(GL_ARRAY_BUFFER, vertData, GL_STATIC_DRAW)

    glVertexAttribPointer(0, 3, GL_FLOAT, false, 6*4, 0)
    glVertexAttribPointer(1, 3, GL_FLOAT, false, 6*4, 3*4)

    glBindBuffer(GL_ARRAY_BUFFER, 0)

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,elementID)
    glBufferData(GL_ELEMENT_ARRAY_BUFFER,elemData,GL_STATIC_DRAW)
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0)

    glBindVertexArray(0)

    new Mesh(vaoID,vertexDataID,elementID,elemCount,vertCount)

  }

}
