package app.core

import org.lwjgl.LWJGLException
import org.lwjgl.opengl.{GL11, Display, DisplayMode}
import tilde.game.Game
import tilde.log.Log

/**
 * Created by Toni on 13.12.2014.
 */
class Core {

  private var lastFrame: Long = 0
  private val game: Game = new Game()

  def start() = {

    try {
      Display.setDisplayMode(new DisplayMode(800, 600))
      Display.setResizable(true)
      Display.setVSyncEnabled(true)
      Display.create()
      GL11.glViewport(0,0,800,600)

    } catch {
      case e: LWJGLException =>
        Log.error(e.toString)
        System.exit(-1)
    }

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
