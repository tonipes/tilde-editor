package tilde.util

import spray.json._
import tilde._
import tilde.graphics.{Material, Model}
import tilde.util._

/**
 * Created by Toni on 17.1.2015.
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
      }).asJsObject.fields + ("type" -> JsString(obj.productPrefix)))

    def read(json: JsValue): Component =
      json.asJsObject.getFields("type") match {
        case Seq(JsString("SpatialComponent"))     => json.convertTo[SpatialComponent]
        case Seq(JsString("ModelComponent"))       => json.convertTo[ModelComponent]
        case Seq(JsString("LightSourceComponent")) => json.convertTo[LightSourceComponent]
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
