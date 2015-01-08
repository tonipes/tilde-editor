package app.core

import org.lwjgl.LWJGLException
import org.lwjgl.opengl._
import tilde.Input
import tilde.game.Game
import tilde.log.Log
import org.lwjgl.input.{Mouse, Keyboard}

/**
 * Created by Toni on 13.12.2014.
 */
class Core {
  println("Testing")
  private var lastFrame: Long = 0
  private val game: Game = new Game()

  def start() = {
    val width = 800
    val height = 600
    val pixFormat = new PixelFormat(8,8,8,8)
    val context = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true)

    // Comment out for non high dpi node
    System.setProperty("org.lwjgl.opengl.Display.enableHighDPI", "true")

    try {
      Display.setDisplayMode(new DisplayMode(width, height))
      Display.setResizable(true)
      Display.setVSyncEnabled(true)
      Display.create(pixFormat,context)

      Display.setTitle("Tilde engine")
      //Display.setDisplayConfiguration(2.2f,0f,0.6f)
      GL11.glViewport(0,0,width,height)
      Keyboard.create()
      Mouse.create()
    } catch {
      case e: LWJGLException =>
        Log.error(e.toString)
        System.exit(-1)
    }

    Log.info("Platform: " + System.getProperty("os.name"))
    Log.info("GPU vendor: " + GL11.glGetString(GL11.GL_VENDOR))
    Log.info("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION))
    Log.info("Pixel scale factor", "" + Display.getPixelScaleFactor())

    game.create()

    while (!Display.isCloseRequested) {
      gameLoop()

    }
    Display.destroy()

  }

  var timeSinceLastFPS = 0f
  private def gameLoop() = {
    if(Display.wasResized()){
      game.resize(Display.getWidth,Display.getHeight)
    }
    Input.update()
    val delta = getDelta
    if(timeSinceLastFPS >= 1f){
      Display.setTitle("Tilde engine. FPS: " + (1 /delta).toInt)
      timeSinceLastFPS = 0f
    }else {
      timeSinceLastFPS += delta
    }

    update(delta)
    render()
    Display.update()
    Display.sync(60)
  }

  private def update(delta: Float) = {
    game.update(delta)
  }

  private def render() = {
    game.render()
  }

  private def getDelta:Float =  {
    val time = getTime
    val delta = time - lastFrame
    lastFrame = time
    (delta / 1000000000.0).toFloat
  }

  private def getTime: Long = System.nanoTime()
}
