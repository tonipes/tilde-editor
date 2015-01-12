package app.main

import argonaut._, Argonaut._
import scalaz._, Scalaz._
import tilde.graphics._
import tilde.entity.component._
import scala.collection.mutable.ListBuffer
import tilde.util._
import scala.util.parsing.json._
/**
 * Created by Toni on 26.12.14.
 */
class MyType(val name: String, val tpe: String, val pos: Other)


class Other(val x: Float,val y: Float)



object TestMain {
  implicit def OtherCodec: CodecJson[Other] = codec2(
    (x: Float, y: Float) => new Other(x, y),
    (other: Other) => (other.x, other.y)
    )("x", "y")

  implicit def MyTypeCodec: CodecJson[MyType] = codec3(
    (name: String, tpe: String, pos: Other) => new MyType(name, tpe, pos),
    (myType: MyType) => (myType.name, myType.tpe, myType.pos)
    )("name", "type", "pos")

  def main(args: Array[String]): Unit = {
    val map = ResourceUtil.readFromFile("src/main/resources/"
      + "maps/untitled.map")
    println(map)
    val res = JSON.parseFull(map)
    println(res)
  }
}
