package tilde.game

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.{GL11, GL20, GL15, GL30}
import tilde.log.Log

/**
 * Created by Toni on 13.12.2014.
 */
class Game {

  def create(): Unit = {

  }

  def render(): Unit = {

  }

  def resize(width: Int, height: Int): Unit = {
    Log.info("Game resize", ""+ width + ", " + height)
  }

  def update(delta: Float): Unit = {

  }
}
