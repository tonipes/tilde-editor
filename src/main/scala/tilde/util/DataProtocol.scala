package tilde.util

import spray.json._
import tilde._
import tilde.graphics.{Material, Model}
import tilde.util._

/**
 * Here is all code for parsing and decoding json files.
 */

object DataProtocol extends DefaultJsonProtocol {

  implicit val vec3Format = new RootJsonFormat[tilde.util.Vec3] {
    def write(obj: Vec3) = {
      JsArray(JsNumber(obj.x), JsNumber(obj.y), JsNumber(obj.z))
    }

    def read(value: JsValue): Vec3 = value match {
      case JsArray(Vector(JsNumber(x), JsNumber(y), JsNumber(z))) =>
        new Vec3(x.toFloat, y.toFloat, z.toFloat)
      case _ => deserializationError("Vector3 expected")
    }
  }

  implicit val vec4Format = new RootJsonFormat[Vec4] {
    def write(obj: Vec4) =
      JsArray(JsNumber(obj.x),JsNumber(obj.y),JsNumber(obj.z),JsNumber(obj.w))

    def read(value: JsValue): Vec4 = value match {
      case JsArray(Vector(JsNumber(x), JsNumber(y), JsNumber(z),JsNumber(w))) =>
        Vec4(x.toFloat, y.toFloat, z.toFloat, w.toFloat)
      case _ => deserializationError("Vector4 expected")
    }
  }

  // implicit val mat4Format = new RootJsonFormat[Matrix4] {
  //   def write(obj: Matrix4) =
  //     JsArray(
  //       JsNumber(obj.m00),JsNumber(obj.m01),JsNumber(obj.m02),JsNumber(obj.m03),
  //       JsNumber(obj.m10),JsNumber(obj.m11),JsNumber(obj.m12),JsNumber(obj.m13),
  //       JsNumber(obj.m20),JsNumber(obj.m21),JsNumber(obj.m22),JsNumber(obj.m23),
  //       JsNumber(obj.m30),JsNumber(obj.m31),JsNumber(obj.m32),JsNumber(obj.m33))

  //   def read(value: JsValue): Matrix4 = value match {
  //     case JsArray(Vector(
  //       JsNumber(m00),JsNumber(m01),JsNumber(m02),JsNumber(m03),
  //       JsNumber(m10),JsNumber(m11),JsNumber(m12),JsNumber(m13),
  //       JsNumber(m20),JsNumber(m21),JsNumber(m22),JsNumber(m23),
  //       JsNumber(m30),JsNumber(m31),JsNumber(m32),JsNumber(m33))) =>

  //       Matrix4(m00.toFloat,m01.toFloat,m02.toFloat,m03.toFloat,
  //               m10.toFloat,m11.toFloat,m12.toFloat,m13.toFloat,
  //               m20.toFloat,m21.toFloat,m22.toFloat,m23.toFloat,
  //               m30.toFloat,m31.toFloat,m32.toFloat,m33.toFloat)

  //     case _ => deserializationError("Matrix4 expected")
  //   }
  // }

  implicit val quaternionFormat = new RootJsonFormat[Quaternion] {
    def write(obj: Quaternion) =
      JsArray(JsNumber(obj.x),JsNumber(obj.y),JsNumber(obj.z),JsNumber(obj.w))

    def read(value: JsValue): Quaternion = value match {
      case JsArray(Vector(JsNumber(x), JsNumber(y), JsNumber(z),JsNumber(w))) =>
        Quaternion(x.toFloat, y.toFloat, z.toFloat, w.toFloat)
      case _ => deserializationError("Quaternion expected")
    }
  }

  implicit val modelFormat             = jsonFormat2(Model)
  implicit val materialFormat          = jsonFormat5(Material)

  implicit val componentFormat_spatial = jsonFormat3(SpatialComponent)
  implicit val componentFormat_model   = jsonFormat1(ModelComponent)
  implicit val componentFormat_light   = jsonFormat6(LightSourceComponent)

  implicit val componentFormat = new RootJsonFormat[Component] {
    def write(obj: Component): JsValue =
      JsObject((obj match {
        case c: ModelComponent       => c.toJson
        case c: SpatialComponent     => c.toJson
        case c: LightSourceComponent => c.toJson
        case _ =>  serializationError("Component expected")
      }).asJsObject.fields + ("type" -> JsString(obj.productPrefix)))

    def read(json: JsValue): Component =
      json.asJsObject.getFields("type") match {
        case Seq(JsString("SpatialComponent"))     => json.convertTo[SpatialComponent]
        case Seq(JsString("ModelComponent"))       => json.convertTo[ModelComponent]
        case Seq(JsString("LightSourceComponent")) => json.convertTo[LightSourceComponent]
        case _ => deserializationError("Component expected")
      }
  }

  implicit val entityFormat = new RootJsonFormat[Entity] {
    def write(obj: Entity) = {
      obj.components.values.toJson
    }

    def read(value: JsValue): Entity = {
      ???
    }
  }

}
