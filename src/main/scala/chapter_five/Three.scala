package chapter_five

import shapeless.{HList, HNil, LabelledGeneric, Lazy, Witness, ::, labelled, tag}
import shapeless.labelled.FieldType

object Three {

  def apply() = {
    val iceCream = IceCream("Sundae", 1, false)
//    val gen:
//      shapeless.::[FieldType[String with labelled.KeyTag[Symbol with tag.Tagged[ {type name}], String], String],
//        shapeless.::[FieldType[Int with labelled.KeyTag[Symbol with tag.Tagged[ {type numCherries}], Int], Int],
//          shapeless.::[FieldType[Boolean with labelled.KeyTag[Symbol with tag.Tagged[ {type inCone}], Boolean], Boolean], HNil]]] =
    //      LabelledGeneric[IceCream].to(iceCream)

    val encoder: JsonValue = JsonEncoder[IceCream].encode(iceCream)
    println(encoder)
  }

  case class IceCream(name: String, numCherries: Int, inCone: Boolean)

  sealed trait JsonValue

  case class JsonObject(fields: List[(String, JsonValue)]) extends JsonValue

  case class JsonArray(items: List[JsonValue]) extends JsonValue

  case class JsonString(items: String) extends JsonValue

  case class JsonNumber(items: Double) extends JsonValue

  case class JsonBoolean(items: Boolean) extends JsonValue

  case object JsonNull extends JsonValue

  trait JsonEncoder[A] {
    def encode(value: A): JsonValue
  }

  object JsonEncoder {
    def apply[A](implicit enc: JsonEncoder[A]): JsonEncoder[A] = enc
  }

  def createEncoder[A](func: A => JsonValue): JsonEncoder[A] =
  new JsonEncoder[A] {
      def encode(value: A): JsonValue = func(value)
    }

  implicit val stringEncoder: JsonEncoder[String] =
  createEncoder(str => JsonString(str))

  implicit val doubleEncoder: JsonEncoder[Double] =
  createEncoder(num => JsonNumber(num))

  implicit val intEncoder: JsonEncoder[Int] =
  createEncoder(num => JsonNumber(num))

  implicit val booleanEncoder: JsonEncoder[Boolean] =
  createEncoder(boolean => JsonBoolean(boolean))

  implicit def listEncoder[A](implicit enc: JsonEncoder[A]): JsonEncoder[List[A]] =
  createEncoder(list => JsonArray(list.map(enc.encode)))

  implicit def optionEncoder[A](implicit enc: JsonEncoder[A]): JsonEncoder[Option[A]] =
  createEncoder(opt => opt.map(enc.encode).getOrElse(JsonNull))

  trait JsonObjectEncoder[A] extends JsonEncoder[A] {
    def encode(value: A): JsonObject
  }

  def createObjectEncoder[A](fn: A => JsonObject): JsonObjectEncoder[A] =
  new JsonObjectEncoder[A] {
      def encode(value: A): JsonObject =
      fn(value)
    }

  implicit val hnilEncoder: JsonObjectEncoder[HNil] =
  createObjectEncoder(_ => JsonObject(Nil))


  /**
    *
    * @param witness When combined with FieldType lets you get the
    *                fieldname of a type
    * @param hEncoder Encodes the head of the list into a JSON Value
    * @param tEncoder Encodes the rest of the list into a JSON Object
    * @tparam K Used to get the field name of the first
    *           element in the HList when combined with FieldType
    * @tparam H Type of the head
    * @tparam T Type of the tail which must be an HList
    *           or HNil (extends from HList)
    * @return Generic JsonObjectEncoder
    */
  implicit def hlistObjectEncoder[K <: Symbol, H, T <: HList]
  (implicit
   witness: Witness.Aux[K],
   hEncoder: Lazy[JsonEncoder[H]],
   tEncoder: JsonObjectEncoder[T]
  ): JsonObjectEncoder[FieldType[K, H] :: T] = {
    val fieldName: String = witness.value.name
    createObjectEncoder { hList =>
      val head = hEncoder.value.encode(hList.head)
      val tail = tEncoder.encode(hList.tail)
      JsonObject((fieldName, head) :: tail.fields)
    }
  }

  /**
    *
    * @param generic Gets the generic representation of an ADT with labels
    * @param hEncoder Object encoder for any type as long as their are implicits
    *                 in scope to support it. This works for ADTs, because
    *                 we have written hlistObjectEncoder.
    * @tparam A Case class type
    * @tparam H Its generic representation as an HList
    * @return
    */
  implicit def genericObjectEncoder[A, H <: HList]
  (implicit
  generic: LabelledGeneric.Aux[A, H],
   hEncoder: Lazy[JsonObjectEncoder[H]]
  ): JsonEncoder[A] = {
    createObjectEncoder { value =>
      hEncoder.value.encode(generic.to(value))
    }
  }

}
