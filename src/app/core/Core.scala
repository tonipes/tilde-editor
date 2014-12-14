package app.core

import org.lwjgl.LWJGLException
import org.lwjgl.opengl.{Display, DisplayMode}
import tilde.game.Game

/**
 * Created by Toni on 13.12.2014.
 */
class Core {

  private var lastFrame: Long = 0
  private val game: Game = new Game()

  def start() = {

    try {
      Display.setDisplayMode(new DisplayMode(800, 600))
      Display.setResizable(false)
      Display.setVSyncEnabled(true)
      Display.create()
    } catch {
      case e: LWJGLException =>
        e.printStackTrace()
        System.exit(0)
    }

    game.create()
    
    while (!Display.isCloseRequested) {
      // Main loop
      update(getDelta)
      render()
      Display.update()
    }

    Display.destroy()

  }

  private def update(delta: Long) = {
    game.update(delta)
  }

  private def render() = {
    game.render()
  }

  private def getDelta():Long =  {
    val time = getTime()
    val delta = time - lastFrame
    lastFrame = time
    delta
  }

  private def getTime(): Long = System.currentTimeMillis()
}
