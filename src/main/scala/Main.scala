import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw.GLFWCursorPosCallback
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWMouseButtonCallback
import org.lwjgl.glfw.GLFWScrollCallback
import org.lwjgl.glfw._
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GLContext
import org.lwjgl.system.MemoryUtil._
import tilde.{Entity, LightSourceComponent, SpatialComponent, ModelComponent}
import tilde.util.{Quaternion, Vec3}

/**
 * Created by Toni on 17.1.2015.
 */
object Main {

  private var windowID: Long = 0

  var errorCallback:       GLFWErrorCallback       = null
  var keyCallback:         GLFWKeyCallback         = null
  var cursorPosCallback:   GLFWCursorPosCallback   = null
  var mouseButtonCallback: GLFWMouseButtonCallback = null
  var scrollCallback:      GLFWScrollCallback      = null

  private def init(): Unit ={
    if (glfwInit() != GL_TRUE)
    {
      System.err.println("Error initializing GLFW")
      System.exit(1)
    }
    glfwWindowHint(GLFW_SAMPLES, 4)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
    glfwWindowHint(GLFW_RESIZABLE, GL_FALSE)

    windowID = glfwCreateWindow(640, 480, "Tilde Engine", NULL, NULL)

    if (windowID == NULL)
    {
      System.err.println("Error creating a window")
      System.exit(1)
    }

    glfwMakeContextCurrent(windowID)

    GLContext.createFromCurrent()

    glfwSwapInterval(1) // vsync

    glfwShowWindow(windowID)

  }

  private def start(): Unit = {
    init()

    var lastFrame: Float = 0
    var currentTime: Float = 0
    var deltaTime: Float = 0


    while (glfwWindowShouldClose(windowID) != GL_TRUE) { // Main Game loop
      // Time
      currentTime = glfwGetTime().toFloat
      deltaTime = currentTime - lastFrame
      lastFrame = currentTime

      update(deltaTime)

      glfwPollEvents()
      glfwSwapBuffers(windowID)
    }

    // Game ended
    dispose()

    glfwDestroyWindow(windowID)
    glfwTerminate()

    System.exit(0)
  }


  private def update(delta: Float): Unit = {

  }

  private def dispose(): Unit = {

  }

  def main (args: Array[String]) {
    start()
  }
}
