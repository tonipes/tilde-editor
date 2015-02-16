package sgEngine

import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw.{GLFWCursorPosCallback, GLFWScrollCallback, GLFWMouseButtonCallback, GLFWKeyCallback}
import sgEngine.util.{Vec2, Vec3}

import scala.collection.mutable

/**
 * Created by Toni on 6.2.2015.
 */

object Input{
  val RELEASED      = 0
  val JUST_RELEASED = 1
  val PRESSED       = 2
  val JUST_PRESSED  = 3

  var window: Long = 0

  private val kbState    = mutable.Map[Int, Int]().withDefault(f => RELEASED)
  private val mouseState = mutable.Map[Int, Int]().withDefault(f => RELEASED)

  private val mouseDownPosition = mutable.Map[Int, Vec2]().withDefault(f => Vec2(0,0))

  val scrollState = Vec2(0,0)
  val mouseCursorPos = Vec2(0,0)

  lazy val keyCallback = new GLFWKeyCallback {
    override def invoke(window: Long, key: Int, scanCode: Int, action: Int, mods: Int): Unit = action match {
      case GLFW_PRESS   => kbState(key) = JUST_PRESSED
      case GLFW_RELEASE => kbState(key) = JUST_RELEASED
      case GLFW_REPEAT  =>
    }
  }
  lazy val mouseCallback =  new GLFWMouseButtonCallback {
    override def invoke(window: Long, key: Int, action: Int, mods: Int): Unit = action match {
      case GLFW_PRESS   => {
        mouseDownPosition(key) = Vec2(mouseCursorPos)
        mouseState(key) = JUST_PRESSED
      }
      case GLFW_RELEASE => {
        mouseState(key) = JUST_RELEASED
      }
      case GLFW_REPEAT  =>
    }
  }

  lazy val scrollCallback = new GLFWScrollCallback {
    override def invoke(window: Long, xoffset: Double, yoffset: Double): Unit = {
      scrollState.x = xoffset.toFloat
      scrollState.y = yoffset.toFloat
    }
  }
  
  lazy val cursorPosCallback = new GLFWCursorPosCallback {
    override def invoke(window: Long, xpos: Double, ypos: Double): Unit = {
      mouseCursorPos.x = xpos.toFloat
      mouseCursorPos.y = ypos.toFloat
    }
  }

  def init(window: Long): Unit ={
    glfwSetKeyCallback(window,keyCallback)
    glfwSetMouseButtonCallback(window, mouseCallback)
    glfwSetScrollCallback(window,scrollCallback)
    glfwSetCursorPosCallback(window,cursorPosCallback)
  }

  def mouseDrag(mouseButton: Int): Vec2 = {
    var drag = Vec2(0,0)
    if(mouseState(mouseButton) != RELEASED){
      val mouseDownPos = mouseDownPosition(mouseButton)
      drag = mouseCursorPos - mouseDownPos
    }
    drag
  }
  def isMousePressed(key: Int): Boolean =
    mouseState(key) == PRESSED || mouseState(key) == JUST_PRESSED

  def isMouseJustPressed(key: Int): Boolean =
    mouseState(key) == JUST_PRESSED

  def isMouseJustReleased(key: Int): Boolean =
    mouseState(key) == JUST_RELEASED

  def isPressed(key: Int): Boolean =
    kbState(key) == PRESSED || kbState(key) == JUST_PRESSED

  def isJustPressed(key: Int): Boolean =
    kbState(key) == JUST_PRESSED

  def isJustReleased(key: Int): Boolean =
    kbState(key) == JUST_RELEASED

  def updateState(): Unit = {
    kbState.foreach(f => {
      if(f._2 == JUST_PRESSED) kbState(f._1) = PRESSED
      if(f._2 == JUST_RELEASED) kbState(f._1) = RELEASED
    })
    mouseState.foreach(f => {
      if(f._2 == JUST_PRESSED) mouseState(f._1) = PRESSED
      if(f._2 == JUST_RELEASED) mouseState(f._1) = RELEASED
    })

    scrollState.x = 0
    scrollState.y = 0
  }
}