package app.main

import argonaut._, Argonaut._
import scalaz._, Scalaz._
import tilde.graphics._
import tilde.entity.component._
import scala.collection.mutable.ListBuffer

/**
 * Created by Toni on 26.12.14.
 */
object TestMain {

  class MyType(val name: String, val tpe: String, val size: Int)

  def main(args: Array[String]): Unit = {
    println("Start")
    var a = List("a", "b", "c", "d", "e")
    println(a)
    println("index 3: " + a(3))
    a
    println(a)
    println("index 3: " + a(3))

  }
}