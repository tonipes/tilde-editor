package tilde.system

import tilde.util.{Vec3, Quaternion}
import tilde._
import tilde.systems.EntitySystem
import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw._
import scala.collection.immutable.BitSet

/**
 * Created by Toni on 5.2.2015.
 */

class EditorInputSystem() extends EntitySystem{
  override var compStruct: BitSet = BitSet.empty + Component.getID(classOf[InputComponent])

  private var cameraPanBase: Option[Vec3] = None

  private var cameraRotBase: Option[Vec3] = None
  private var cameraRotBaseOrientation: Option[Quaternion] = None

  private val cPanFac = 0.01f
  private val cZoomFac = 0.25f
  private val cRotFac = 0.1f

  /**
   * Processes single entity
   * @param e Entity to process
   */
  override protected def process(e: Entity, delta: Float): Unit = {
    // TODO: THIS IS ONLY FOR TESTING PURPOSES
    val testSpat = world.getTagged("test").get.getComponent(Component.spatial).get
    val spat = e.getComponent(Component.spatial).get

    basicCameraMovement(spat, delta)
    basicTestMovement(testSpat,delta)
    cameraPanning(spat)
    cameraZooming(spat)
    cameraRotate(spat)

  }

  private def cameraZooming(cameraSpat: SpatialComponent) = {
    val scroll = Input.scrollState
    if(scroll.y != 0){
      val forward = cameraSpat.orientation.getForward()
      val movement = (forward / forward.length()) * -scroll.y * cZoomFac

      cameraSpat.position += movement
    }
  }

  private def cameraRotate(cameraSpat: SpatialComponent)= {

    if(Input.isMouseJustPressed(GLFW_MOUSE_BUTTON_MIDDLE) && !Input.isPressed(GLFW_KEY_LEFT_SHIFT)){
      cameraRotBase = Some(Vec3(cameraSpat.position))
      cameraRotBaseOrientation = Some(Quaternion(cameraSpat.orientation))
    }
    else if(Input.isMouseJustReleased(GLFW_MOUSE_BUTTON_MIDDLE)){
      cameraRotBase = None
      cameraRotBaseOrientation = None
    }
    if(Input.isMousePressed(GLFW_MOUSE_BUTTON_MIDDLE) && cameraRotBase.isDefined){
      val mMov   = Input.mouseDrag(GLFW_MOUSE_BUTTON_MIDDLE)
      val rotX   = Quaternion.fromAxisAngle(Vec3(mMov.y,mMov.x,0),cRotFac)
      //val rotY   = Quaternion.fromAxisAngle(Vec3(0,1,0),mMov.x * cRotFac)
      val pivot  = Vec3(0,0,0)
      val dir    = Vec3(cameraRotBase.get.x,cameraRotBase.get.y,cameraRotBase.get.z)
      val diff = rotX
      val newPos = dir * diff
      println(s"Rotate. dir:$dir, newPos:$newPos + diff:$diff")
      cameraSpat.orientation.setIdentity()
      cameraSpat.orientation *= (cameraRotBaseOrientation.get*diff)
      cameraSpat.position.x = newPos.x
      cameraSpat.position.y = newPos.y
      cameraSpat.position.z = newPos.z
    }
  }

  private def cameraPanning(cameraSpat: SpatialComponent) ={
    if(Input.isMouseJustPressed(GLFW_MOUSE_BUTTON_MIDDLE) && Input.isPressed(GLFW_KEY_LEFT_SHIFT)){
      cameraPanBase = Some(Vec3(cameraSpat.position))
    }
    else if(Input.isMouseJustReleased(GLFW_MOUSE_BUTTON_MIDDLE)){
      cameraPanBase = None
    }

    if(Input.isMousePressed(GLFW_MOUSE_BUTTON_MIDDLE) && cameraPanBase.isDefined){
      val mMov   = Input.mouseDrag(GLFW_MOUSE_BUTTON_MIDDLE)
      val cUp    = cameraSpat.orientation.getUp().normalise()
      val cRight = cameraSpat.orientation.getRight().normalise()
      val yMov   = cUp*mMov.y*cPanFac
      val xMov   = cRight*mMov.x*cPanFac
      val newLoc = cameraPanBase.get + (yMov - xMov)

      cameraSpat.position.x = newLoc.x
      cameraSpat.position.y = newLoc.y
      cameraSpat.position.z = newLoc.z
    }
  }
  private def basicCameraMovement(cameraSpat: SpatialComponent, delta: Float): Unit = {
    val inputSpeed = 10f * delta
    val rotationSpeed = 90f * delta

    if(Input.isPressed(GLFW_KEY_W)){
      cameraSpat.move(cameraSpat.forward(),-inputSpeed)
    }
    if(Input.isPressed(GLFW_KEY_S)){
      cameraSpat.move(cameraSpat.forward(),inputSpeed)
    }
    if(Input.isPressed(GLFW_KEY_A)){
      cameraSpat.move(cameraSpat.right(),-inputSpeed)
    }
    if(Input.isPressed(GLFW_KEY_D)){
      cameraSpat.move(cameraSpat.right(),inputSpeed)
    }
    if(Input.isPressed(GLFW_KEY_Q)){
      cameraSpat.orientation *= Quaternion.fromAxisAngle(cameraSpat.up(),rotationSpeed)
    }
    if(Input.isPressed(GLFW_KEY_E)){
      cameraSpat.orientation *= Quaternion.fromAxisAngle(cameraSpat.up(),-rotationSpeed)
    }
  }

  private def basicTestMovement(spat: SpatialComponent,delta:Float): Unit = {
    val inputSpeed = 10f * delta
    val rotationSpeed = 90f * delta

    if(Input.isPressed(GLFW_KEY_RIGHT)){
      spat.move(spat.right(),inputSpeed)
    }
    if(Input.isPressed(GLFW_KEY_LEFT)){
      spat.move(spat.right(),-inputSpeed)
    }
    if(Input.isPressed(GLFW_KEY_UP)){
      spat.move(spat.forward(),inputSpeed)
    }
    if(Input.isPressed(GLFW_KEY_DOWN)){
      spat.move(spat.forward(),-inputSpeed)
    }
    if(Input.isPressed(GLFW_KEY_R)){
      spat.scale *= 1.01f
    }
    if(Input.isPressed(GLFW_KEY_F)){
      spat.scale *= 0.99f
    }
    if(Input.isPressed(GLFW_KEY_T)){
      spat.orientation *= Quaternion.fromAxisAngle(spat.up(),rotationSpeed)
    }
    if(Input.isPressed(GLFW_KEY_G)){
      spat.orientation *= Quaternion.fromAxisAngle(spat.up(),-rotationSpeed)
    }
  }
}
