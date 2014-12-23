package tilde.entity

import tilde.entity.system.EntitySystem

import scala.collection.mutable._
/**
 * Created by Toni on 23.12.14.
 */
/*
  - Groups?
  - Tags ( to indentify eg. player or camera)
  - Managers? is needed
  - Systems ! ! !
  - Signals !?
 */
class World {

  // Tag is used to identify unique entities like "camera" and "player"
  val tag = HashMap[String, Entity]()

  // Systems alter world's state.
  val systems = HashMap[Class[_ <: EntitySystem], EntitySystem]()


  val entities = Buffer[Entity]()


}
