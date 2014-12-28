package tilde.entity.system

import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.Display
import tilde.Input
import tilde.entity.Entity
import tilde.entity.component.{CameraComponent, SpatialComponent}
import tilde.log.Log
import tilde.util.MatrixUtil

/**
 * Created by Toni on 27.12.14.
 */
class InputSystem extends LogicSystem {
  val movementSpeed = 0.01f
  val rotateSpeed = 1f

  override def processSystem(): Unit = {
    val camEntity = world.getTagged("camera")
    val camSpatial = camEntity.getComponent(SpatialComponent.id).get
    val camCamera = camEntity.getComponent(CameraComponent.id).get
    handleMovement(camSpatial)

    if(Input.isJustPressed(Keyboard.KEY_O)){
      val height = Display.getHeight/200f
      val width = Display.getWidth/200f
      camCamera.projectionMatrix = MatrixUtil.createOrthographicProjection(100,0.1f,-width,width,height,-height)
      camCamera.projectionMatrix.store(camCamera.projectionBuffer)
      camCamera.projectionBuffer.rewind()
    }
    if(Input.isJustPressed(Keyboard.KEY_P)){
      val height = Display.getHeight.toFloat
      val width = Display.getWidth.toFloat
      camCamera.projectionMatrix = MatrixUtil.createPerspectiveProjection(100,0.1f,width/height,45f)
      camCamera.projectionMatrix.store(camCamera.projectionBuffer)
      camCamera.projectionBuffer.rewind()
    }
  }

  private def handleMovement(spatial: SpatialComponent) = {

    // DEBUG MOVEMENT HANDLING

    if(Input.isPressed(Keyboard.KEY_W)) spatial.move(spatial.forward(),-movementSpeed)
    if(Input.isPressed(Keyboard.KEY_S)) spatial.move(spatial.forward(),movementSpeed)
    if(Input.isPressed(Keyboard.KEY_A)) spatial.move(spatial.right(),-movementSpeed)
    if(Input.isPressed(Keyboard.KEY_D)) spatial.move(spatial.right(),movementSpeed)

    if(Input.isPressed(Keyboard.KEY_DOWN)) spatial.rotate(-rotateSpeed,spatial.right())
    if(Input.isPressed(Keyboard.KEY_UP)) spatial.rotate(rotateSpeed,spatial.right())
    if(Input.isPressed(Keyboard.KEY_LEFT)) spatial.rotate(rotateSpeed,spatial.up())
    if(Input.isPressed(Keyboard.KEY_RIGHT)) spatial.rotate(-rotateSpeed,spatial.up())

    if(Input.isPressed(Keyboard.KEY_E)) spatial.rotate(-rotateSpeed,spatial.forward())
    if(Input.isPressed(Keyboard.KEY_Q)) spatial.rotate(rotateSpeed,spatial.forward())

    if(Input.isPressed(Keyboard.KEY_F)) spatial.move(spatial.up(),-movementSpeed)
    if(Input.isPressed(Keyboard.KEY_R)) spatial.move(spatial.up(),movementSpeed)
  }
}
