package sgEditor.systems

import scala.collection.mutable.Buffer
import java.nio.FloatBuffer

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._
import sgEngine._
import sgEngine.graphics.ShaderProgram
import sgEngine.systems.EntitySystem
import sgEngine.util._

import scala.collection.immutable.BitSet
import scala.collection.mutable.Map

/**
 * Created by Toni on 17.1.2015.
 */

class EditorRenderSystem extends EntitySystem{
  override var compStruct = Vector(
      BitSet.empty +
      Component.getID(classOf[SpatialComponent]) +
      Component.getID(classOf[ModelComponent]),
      BitSet.empty +
      Component.getID(classOf[SpatialComponent]) +
      Component.getID(classOf[LightSourceComponent])
  )

  val batchedEntities = Map[ModelComponent, Vector[Entity]]()
  val lights = Buffer[(SpatialComponent, LightSourceComponent)]()
  val size = 2048

  // GL setup
  glEnable(GL_DEPTH_TEST)
  glEnable(GL_CULL_FACE)
  glEnable(GL_TEXTURE_2D)

  glLineWidth(1f)
  glEnable(GL_LINE_SMOOTH)
  glHint(GL_LINE_SMOOTH_HINT,  GL_NICEST)

  glViewport(0,0,size,size)

  lazy val defaultShader: ShaderProgram = ResourceManager.shaderPrograms("default")
  lazy val passthroughShader: ShaderProgram = ResourceManager.shaderPrograms("passthrough")
  lazy val gridShader: ShaderProgram = ResourceManager.shaderPrograms("grid")

  lazy val framebuffer = new sgEngine.graphics.Framebuffer(size,size,"diffuse","normal","material","id")
  lazy val screenPlane = MeshFactory.createScreenMesh()
  val grid = MeshFactory.createGridMesh(8, 8, 16, 16) // MeshFactory.createTransformManipulator(5)//

  override def addEntity(e: Entity) = {
    // TODO: Should get bitset that matches => match
    if(e.containsComponent(classOf[ModelComponent])){ // Add to entity list
      // TODO: Why is entity in batchedEntities. Isn't spatial enough
      val model = e.getComponent(Component.model).get
      if(batchedEntities.contains(model)){
        batchedEntities(model) ++= Vector[Entity](e)
      }
      else{
        batchedEntities(model) = Vector[Entity](e)
      }
    }
    if(e.containsComponent(classOf[LightSourceComponent])){ // Add to light list
      val light = e.getComponent(Component.light).get
      val spat = e.getComponent(Component.spatial).get
      lights += Tuple2(spat, light)
    }

  }

  override def systemBegin(): Unit = {

  }

  override protected def process(e: Entity, delta: Float): Unit = {

  }

  /*
   * Renders scene
   */
  override def systemEnd(): Unit = {
    val camera = world.getTagged("camera")

    // Camera is needed
    if(!camera.isDefined) throw new IllegalStateException("No camera found!")

    val cameraSpat = camera.get.getComponent(Component.spatial).get
    val cameraPos = cameraSpat.position

    val viewBuffer       = getViewBuffer(cameraSpat.position,cameraSpat.orientation)
    val projectionBuffer = BufferFactory.createFloatBuffer(ResourceManager.getProjection)

    // Get ready to draw scene to framebuffer
    framebuffer.bindFramebuffer()
      glDrawBuffers(BufferFactory.createIntBuffer(framebuffer.attachments))
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
      renderScene(viewBuffer, projectionBuffer)
      renderGrid(viewBuffer, projectionBuffer)
    framebuffer.unbind()

    // Render framebuffer to screen
    passthroughShader.bind()
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
      renderToScreen()

    passthroughShader.unbind()
  }

  /*
   * Renders grid
   */
  private def renderGrid(viewBuffer: FloatBuffer, projectionBuffer: FloatBuffer) = {
    val spat = SpatialComponent(Vec3(0,0,0),Quaternion(0,0,0,1),Vec3(1,1,1))

    gridShader.bind()
    gridShader.setUniform("m_model", getFloatBuffer(spat.position ,spat.orientation, spat.scale))
    gridShader.setUniform("m_view", viewBuffer)
    gridShader.setUniform("m_proj", projectionBuffer)

    grid.bindVAO()
    grid.bindElem()

    glEnableVertexAttribArray(0)
    glEnableVertexAttribArray(1)

    glDrawElements(GL_LINES, grid.elemCount, GL_UNSIGNED_INT, 0)

    glDisableVertexAttribArray(0)
    glDisableVertexAttribArray(1)

    grid.bindVAO()
    grid.bindElem()

    gridShader.unbind()
  }

  /*
   * Renders all entities
   */
  private def renderScene(viewBuffer: FloatBuffer, projectionBuffer: FloatBuffer) = {
    defaultShader.bind()
    defaultShader.setUniform("m_view", viewBuffer)
    defaultShader.setUniform("m_proj", projectionBuffer)

    for(i <- batchedEntities){
      val model = ResourceManager.models(i._1.model)
      val mesh = ResourceManager.meshes(model.mesh)

      mesh.bindVAO()
      mesh.bindElem()

      glEnableVertexAttribArray(0)
      glEnableVertexAttribArray(1)
      glEnableVertexAttribArray(2)
      glEnableVertexAttribArray(3)

      for(e <- i._2){
        val spat = e.getComponent(Component.spatial).get
        defaultShader.setUniform("m_model", getFloatBuffer(spat.position ,spat.orientation, spat.scale))


        glDrawElements(GL_TRIANGLES,mesh.elemCount,GL_UNSIGNED_INT,0)


      }
      glDisableVertexAttribArray(0)
      glDisableVertexAttribArray(1)
      glDisableVertexAttribArray(2)
      glDisableVertexAttribArray(3)

      mesh.unbindVAO()
      mesh.unbindElem()
    }
    defaultShader.unbind()
  }
  /*
   * Renders framebuffer to screen.
   */
  private def renderToScreen() ={
    framebuffer.activateTextures(passthroughShader)

    screenPlane.bindVAO()
    screenPlane.bindElem()
    glEnableVertexAttribArray(0)
    glEnableVertexAttribArray(1)

    glDrawElements(GL_TRIANGLES,screenPlane.elemCount,GL_UNSIGNED_INT,0)

    glDisableVertexAttribArray(0)
    glDisableVertexAttribArray(1)

    screenPlane.unbindVAO()
    screenPlane.unbindElem()

  }
  /*
   * Makes floatbuffer from spatial data to load it to shader
   */
  private def getFloatBuffer(position: Vec3, orientation: Quaternion, scaleVec: Vec3): FloatBuffer ={
    val scale  = Matrix4.getScale(scaleVec)
    val trans  = Matrix4.getTransformation(position)
    val rotate = orientation.rotationMatrix()
    val mul    = trans * rotate * scale

    BufferFactory.createFloatBuffer( mul.transpose )
  }

  private def getViewBuffer(position: Vec3, orientation: Quaternion): FloatBuffer = {
    val scale  = Matrix4.getScale(Vec3(1,1,1))
    val trans  = Matrix4.getTransformation(position.neg)
    val rotate = orientation.getConjugate().rotationMatrix()
    val mul    = scale * rotate * trans

    BufferFactory.createFloatBuffer( mul.transpose )
  }
}
