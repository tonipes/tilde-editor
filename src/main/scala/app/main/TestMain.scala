package app.main

import tilde.util._
import spray.json._


object TestMain {
  import DefaultJsonProtocol._

  sealed trait Animal extends Product
  case class Dog(name: String, age: Int ) extends Animal
  case class Cat(name: String, catchesBirds: Boolean )extends Animal
  case class Whale(name: String, isBig: Boolean) extends Animal

  implicit val dogFormat = jsonFormat(Dog, "name", "age")//jsonFormat2(Dog)
  implicit val catFormat = jsonFormat(Cat, "name", "catchesBirds")//jsonFormat2(Cat)
  implicit val whaleFormat = jsonFormat(Whale, "name", "isBig")//jsonFormat3(Whale)

  implicit val animalFormat = new RootJsonFormat[Animal] {
    def write(obj: Animal): JsValue =
      JsObject((obj match {
        case c: Cat => c.toJson
        case d: Dog => d.toJson
        case w: Whale => w.toJson
      }).asJsObject.fields + ("type" -> JsString(obj.productPrefix)))

    def read(json: JsValue): Animal =
      json.asJsObject.getFields("type") match {
        case Seq(JsString("Cat")) => json.convertTo[Cat]
        case Seq(JsString("Dog")) => json.convertTo[Dog]
        case Seq(JsString("Whale")) => json.convertTo[Whale]
      }
  }

  def main(args: Array[String]): Unit = {
    val test = ResourceUtil.readFromFile("src/main/resources/"
      + "maps/test.json")
    val json = """{"type": "Dog", "name": "Wuff", "age": 23}"""

    val max: Animal = Dog("max", 23)
    val charlie = Dog("charlie", 13)
    val tigger = Cat("tigger",true)
    val blue = Whale("blue",true)

    val list = Array(max,charlie,tigger,blue)
    println(list.toJson)
    println(max.toJson)
    println(max.toJson.convertTo[Animal])

    println(test.parseJson.convertTo[List[Animal]])
    println("""{"type": "Dog", "name": "Wuff", "age": 23}""".parseJson.convertTo[Animal])
    println("""{"type": "Cat", "name": "Meow", "catchesBirds": true}""".parseJson.convertTo[Animal])

  }
}
