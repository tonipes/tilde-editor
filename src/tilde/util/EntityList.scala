package tilde.util

import tilde.entity.Entity

/**
 * Created by Toni on 25.12.14.
 */
class EntityList(var size: Int = 128) {
  private var entities = Array.ofDim[Entity](size)

  // removes item from list
  def remove(index: Int): Unit = {
    entities(index) = null
  }

  def get(index: Int):Entity = {
    entities(index)
  }

  def contains(e: Entity): Boolean = {
    for (i <- 0 until size) {
      if (entities(i) == e) return true
    }
    return false
  }
  

}
