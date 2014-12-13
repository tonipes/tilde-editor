package tilde.game

/**
 * Created by Toni on 13.12.2014.
 */
class Game {

  def create(): Unit ={
    println("Game create method")
  }

  def render(): Unit ={
    println("Game render method")
  }

  def update(delta: Long): Unit ={
    println("Game update method with delta: " + delta)
  }
}
