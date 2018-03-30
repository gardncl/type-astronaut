package chapter_five

import shapeless.LabelledGeneric.Aux
import shapeless.{Coproduct, :+:, CNil, Inl, Inr, Witness, Lazy}
import shapeless.{HList, HNil, LabelledGeneric}
import shapeless.labelled.FieldType
import Three._

object Four {

  def apply() = {
    val rect = Rectangle(1.0, 2.0)
    val circle = Circle(3.0)
//    val foo: Aux[Shape, HNil] = LabelledGeneric[Shape]
//    println(foo)
//    val encodedRect: JsonValue = JsonEncoder[Shape].encode(rect)
//    val encodedCircle: JsonValue = JsonEncoder[Shape].encode(circle)
//    println(encodedRect)
//    println(encodedCircle)
  }

  sealed trait Shape
  final case class Rectangle(width: Double, height: Double) extends Shape
  final case class Circle(radius: Double) extends Shape

  implicit val cnilObjectEncoder: JsonObjectEncoder[CNil] =
    createObjectEncoder(cnil => throw new Exception("Inconceivable!"))

  /**
    *
    * @param witness
    * @param hEncoder
    * @param tEncoder
    * @tparam K Head type name
    * @tparam H Head of HList
    * @tparam T Tail of HList
    * @return
    */
  implicit def coproductObjectEncoder[K <: Symbol, H, T <: Coproduct]
  (
  implicit
  witness: Witness.Aux[K],
  hEncoder: Lazy[JsonEncoder[H]],
  tEncoder: JsonObjectEncoder[T]
  ): JsonObjectEncoder[FieldType[K, H] :+: T] = {
    val typeName = witness.value.name
    createObjectEncoder {
      case Inl(h) =>
        JsonObject(List(typeName -> hEncoder.value.encode(h)))

      case Inr(t) =>
        tEncoder.encode(t)
    }
  }

}
