package tilde.entity.system

import org.lwjgl.input.Keyboard
import org.lwjgl.util.vector.Matrix4f
import tilde.Input
import tilde.entity.{World, Entity}
import tilde.entity.component.{CameraComponent, SpatialComponent}
import tilde.log.Log
import tilde.util.{QuaternionUtil, SystemUtil}

/**
 * Created by Toni on 26.12.14.
 */

class CameraSystem() extends EntitySystem(SystemUtil.createAspect(SpatialComponent,CameraComponent)){
  val movementSpeed = 0.01f
  val rotateSpeed = 1f

  override def process(e: Entity): Unit = {
   // Log.debug("CameraSystem processing entity", "" + e)
    // update camera viewMatrix
    val spatial = e.getComponent(SpatialComponent.id).get
    val camera = e.getComponent(CameraComponent.id).get

    val viewMatrix = QuaternionUtil.rotationMatrix(spatial.getOrientation)
    Matrix4f.translate(spatial.getPosition.negate(null), viewMatrix, viewMatrix)
    viewMatrix.store(camera.viewBuffer)
    camera.viewBuffer.rewind()

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

  override def begin(): Unit = {
    super.begin()
    //Log.debug("System stared", "CameraSystem")
  }

  override def end(): Unit = {
    super.end()
    //Log.debug("System ended", "CameraSystem")
  }

  override def toString() ={
    "CameraSystem"
  }
}
