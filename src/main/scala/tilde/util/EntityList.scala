// package tilde.util

// import tilde.entity._
// import scala.collection.mutable._
// /**
//  * Created by Toni on 25.12.14.
//  */
// class EntityList(var size: Int = 4096) {
//   val currentIndex = 0
//   val emptyIndices = Buffer[Int]()
//   val components = Map[Class[_ <: Component], Array[_ <: Component]]()

//   // removes item from list
//   def remove(index: Int): Unit = {

//   }

//   def getComponent[T <: Component](entity: Int, component: Class[T]): Option[T] = {
//     Some(components(component)(entity))
//   }

//   def removeComponent[T <: Component](entity: Int, component: Class[T]) = {
//     components(component)(entity) = null
//   }
// }
