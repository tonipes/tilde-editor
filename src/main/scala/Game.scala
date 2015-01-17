import tilde.World

import org.lwjgl.opengl.GL11._
/**
 * Created by Toni on 17.1.2015.
 */
class Game {

  var world: World = null
  def create(): Unit = {

    glEnable(GL_DEPTH_TEST)
    glEnable(GL_CULL_FACE)
    glClearColor(0.2f, 0.2f, 0.2f, 0.0f)
  }

  def render(): Unit = {

  }

  def resize(width: Int, height: Int): Unit = {

  }

  def update(delta: Float): Unit = {

  }

  def dispose() = {

  }
}
