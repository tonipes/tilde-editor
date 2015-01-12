package app.main

import argonaut._, Argonaut._
import scalaz._, Scalaz._
import tilde.graphics._
import tilde.entity.component._
import scala.collection.mutable.ListBuffer
import tilde.util._
/**
 * Created by Toni on 26.12.14.
 */
object TestMain {
  def main(args: Array[String]): Unit = {
    val map = ResourceUtil.readFromFile("src/main/resources/"
      + "maps/untitled.map")
println(map)
}
}
