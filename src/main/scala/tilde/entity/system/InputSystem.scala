package tilde.entity.system

import java.nio.{FloatBuffer, DoubleBuffer}

import org.lwjgl.BufferUtils
import org.lwjgl.input.{Mouse, Keyboard}
import org.lwjgl.opengl.Display
import org.lwjgl.util.glu.GLU
import tilde.Input
import org.lwjgl.opengl.GL11._
import tilde.entity._
import tilde.log.Log
import tilde.util.{Direction, MatrixUtil}

/**
 * Input system. At the time mostly for debugging stuff.
 */
class InputSystem extends LogicSystem {
  var movementSpeed = 0.01f
  var rotateSpeed = 1f
  var globalAxis = false

  override def processSystem(): Unit = {
    val camEntity = world.getTagged("camera").get
    val camSpatial = camEntity.getComponent(SpatialComponent.id).get
    val camCamera = camEntity.getComponent(CameraComponent.id).get
    handleMovement(camSpatial)

    if(Input.isJustPressed(Keyboard.KEY_O)){
      val height = Display.getHeight/60f
      val width = Display.getWidth/60f
      camEntity.addComponent(CameraComponent(MatrixUtil.createOrthographicProjection(100,0.1f,-width,width,height,-height)))
    }

    if(Input.isJustPressed(Keyboard.KEY_P)){
      val height = Display.getHeight.toFloat
      val width = Display.getWidth.toFloat
      camEntity.addComponent(CameraComponent(MatrixUtil.createPerspectiveProjection(100,0.1f,width/height,45f)))
    }

    if(Input.isJustPressed(Keyboard.KEY_B)) {
      val cam = world.getTagged("camera")
      //val light = world.getTagged("fireLight")
      Log.debug("Camera position","" + cam.get.getComponent(SpatialComponent.id).get.getPosition)
      Log.debug("Camera Rot","" + cam.get.getComponent(SpatialComponent.id).get.getOrientation)
      //light.get.addComponent(cam.get.getComponent(SpatialComponent.id).get)
      //light.get.getComponent(SpatialComponent.id).get.setPosition(cam.get.getComponent(SpatialComponent.id).get.getPosition)
    }
    //val cursorLocation = getWorldCoordinates(camCamera.viewBuffer,camCamera.projectionBuffer)

    //val cursor = world.getTagged("cursor").get
    //val cursorSpatial = cursor.getComponent(SpatialComponent.id).get
    //cursorSpatial.setPosition((cursorLocation.get(0) + 0.5).toInt,(cursorLocation.get(1) + 0.5).toInt,(cursorLocation.get(2) + 0.5).toInt)
    //cursorSpatial.setPosition(cursorLocation.get(0),cursorLocation.get(1),cursorLocation.get(2))
  }

  private def getWorldCoordinates(view: FloatBuffer,proj: FloatBuffer):FloatBuffer = {
    // TODO: Make nicer. MAKE
    val viewportBuf = BufferUtils.createIntBuffer(16)
    glGetInteger(GL_VIEWPORT,viewportBuf)

    var s = ""
    for(i <- 0 until 16){
      s += viewportBuf.get(i) + ", "
    }

    val winX = Mouse.getX
    val winY = Mouse.getY
    //Log.debug("Modelview",s)
    //Log.debug("Mouse",winX + ", " + winY)
    val winZ = BufferUtils.createFloatBuffer(1)
    glReadPixels(winX,winY.toInt,1,1,GL_DEPTH_COMPONENT,GL_FLOAT,winZ)

    var resBuffer = BufferUtils.createFloatBuffer(3)
    GLU.gluUnProject(winX,winY,winZ.get(0),view,proj,viewportBuf,resBuffer)
    //Log.debug("Mouse world coordinates","" + resBuffer.get(0) + ", " + resBuffer.get(1)+ ", " + resBuffer.get(2))
    resBuffer
  }

  private def dToF(d: DoubleBuffer) = {
    val a = BufferUtils.createFloatBuffer(16)
    for(i <- 0 until 16){
      val f = d.get(i)
      a.put(i, f.toFloat)
    }
    a
  }

  private def handleMovement(spatial: SpatialComponent) = {
    if(Input.isPressed(Keyboard.KEY_LSHIFT)) {
      movementSpeed = 0.1f
      rotateSpeed = 2f
    } else{
      movementSpeed = 0.01f
      rotateSpeed = 1f
    }
    if(Input.isJustPressed(Keyboard.KEY_M)) {
      globalAxis = !globalAxis
      Log.debug("Changed axis","now " + (if(globalAxis) "global" else "local"))
    }

    if(Input.isJustPressed(Keyboard.KEY_Z)) {
      spatial.setPosition(0,1,0)
    }
    if(Input.isJustPressed(Keyboard.KEY_X)) {
      spatial.setOrientation(0,-1,0,0)
    }
    val forward = if(globalAxis) Direction.AXIS_Z else spatial.forward()
    val right = if(globalAxis) Direction.AXIS_X else spatial.right()
    val up = if(globalAxis) Direction.AXIS_Y else spatial.up()


    // DEBUG MOVEMENT HANDLING

    if(Input.isPressed(Keyboard.KEY_W)) spatial.move(forward,-movementSpeed)
    if(Input.isPressed(Keyboard.KEY_S)) spatial.move(forward,movementSpeed)
    if(Input.isPressed(Keyboard.KEY_A)) spatial.move(right,-movementSpeed)
    if(Input.isPressed(Keyboard.KEY_D)) spatial.move(right,movementSpeed)

    if(Input.isPressed(Keyboard.KEY_DOWN)) spatial.rotate(-rotateSpeed,right)
    if(Input.isPressed(Keyboard.KEY_UP)) spatial.rotate(rotateSpeed,right)
    if(Input.isPressed(Keyboard.KEY_LEFT)) spatial.rotate(rotateSpeed,up)
    if(Input.isPressed(Keyboard.KEY_RIGHT)) spatial.rotate(-rotateSpeed,up)

    if(Input.isPressed(Keyboard.KEY_E)) spatial.rotate(-rotateSpeed,forward)
    if(Input.isPressed(Keyboard.KEY_Q)) spatial.rotate(rotateSpeed,forward)

    if(Input.isPressed(Keyboard.KEY_F)) spatial.move(up,-movementSpeed)
    if(Input.isPressed(Keyboard.KEY_R)) spatial.move(up,movementSpeed)
  }
}
