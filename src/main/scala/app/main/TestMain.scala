// package app.main

// import argonaut._, Argonaut._
// import scalaz._, Scalaz._
// import tilde.graphics._
// import tilde.entity.component._
// /**
//  * Created by Toni on 26.12.14.
//  */
// object TestMain {

// class MyType(val name: String, val tpe: String, val size: Int)

//   def main(args:Array[String]): Unit = {
//   	val inputSingle = """{ "name": "Mark", "age": 74 }"""
//   	val input = """
// 	    [
// 	      { "name": "Mark", "age": 74 },
// 	      { "name": "Matt", "age": 22 },
// 	      { "name": "Rick", "age": 4 },
// 	      { "name": "John", "age": 19 },
// 	    ]
// 	  """
// 	  val json = """[
// 							    {
// 							        "name": "apple",
// 							        "type": "fruit",
// 							        "size": 3
// 							    },
// 							    {
// 							        "name": "jam",
// 							        "type": "condiment",
// 							        "size": 5
// 							    },
// 							    {
// 							        "name": "beef",
// 							        "type": "meat",
// 							        "size": 1
// 							    }
// 								]"""

// 	implicit def MyTypeCodec: CodecJson[MyType] = codec3(
// 	  (name: String, tpe: String, size: Int) => new MyType(name, tpe, size),
// 	  (myType: MyType) => (myType.name, myType.tpe, myType.size)
// 		)("name", "type", "size")

//     val list = Parse.decodeValidation[List[MyType]](json)
//   }
// }