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

//    val pixFormat = new PixelFormat()
//    val context = new ContextAttribs(4,2).withForwardCompatible(true).withProfileCore(true)

    try {
      Display.setDisplayMode(new DisplayMode(800, 600))
      Display.setResizable(false)
      Display.setVSyncEnabled(true)

     // Display.create(pixFormat,context)
      Display.create()
      Log.info("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION))

      GL11.glViewport(0,0,800,600)

    } catch {
      case e: LWJGLException =>
        Log.error(e.toString)
        System.exit(0)
    }

    game.create()

    while (!Display.isCloseRequested) {
      // Main loop
      update(getDelta)
      render()
      Display.update()
      Display.sync(60)
    }
    Display.destroy()

  }

  private def update(delta: Long) = {
    game.update(delta)
  }

  private def render() = {
    game.render()
  }

  private def getDelta:Long =  {
    val time = getTime
    val delta = time - lastFrame
    lastFrame = time
    delta
  }

  private def getTime: Long = System.currentTimeMillis()
}
