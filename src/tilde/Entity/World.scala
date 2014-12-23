package tilde.entity

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

  val entities = Buffer[Entity]()


}
