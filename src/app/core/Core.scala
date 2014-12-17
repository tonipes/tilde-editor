package app.core

import org.lwjgl.LWJGLException
import org.lwjgl.opengl._
import tilde.game.Game
import tilde.log.Log

/**
 * Created by Toni on 13.12.2014.
 */
class Core {

  private var lastFrame: Long = 0
  private val game: Game = new Game()

  def start() = {

   val pixFormat = new PixelFormat()
   val context = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true)

    try {
      Display.setDisplayMode(new DisplayMode(800, 600))
      Display.setResizable(true)
      Display.setVSyncEnabled(true)

      Display.create(pixFormat,context)
      //Display.create()

      GL11.glViewport(0,0,800,600)

    } catch {
      case e: LWJGLException =>
        Log.error(e.toString)
        System.exit(-1)
    }

    Log.info("Platform: " + System.getProperty("os.name"))
    Log.info("GPU vendor: " + GL11.glGetString(GL11.GL_VENDOR))
    Log.info("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION))

    game.create()
    while (!Display.isCloseRequested) {
      gameLoop()

    }
    Display.destroy()

  }

  private def gameLoop() = {
    if(Display.wasResized()){
      game.resize(Display.getWidth,Display.getHeight)
    }

    update(getDelta)

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
