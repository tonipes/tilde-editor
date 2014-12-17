package tilde.game

import tilde.log.Log

/**
 * Created by Toni on 13.12.2014.
 */
class Game {

  def create(): Unit = {
    Log.test()
  }

  def render(): Unit = {

  }

  def update(delta: Long): Unit = {

  }

  def resize(width: Int, height: Int): Unit = {
    Log.info("Game resized", "new size is " + width + ", " + height)
  }
}
