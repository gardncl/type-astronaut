package chapter_three

import chapter_three.One.CsvEncoder
import chapter_two.One.{Circle, Rectangle, Shape}
import shapeless.{:+:, CNil, Coproduct, Inl, Inr}

object Three {

  def apply() = {
    /**
      * 1. Error:(12, 17) could not find implicit value for parameter enc: chapter_three.One.CsvEncoder[chapter_two.One.Shape]
    One.writeCsv(shapes)
      * 2. Error:(12, 17) not enough arguments for method writeCsv: (implicit enc: chapter_three.One.CsvEncoder[chapter_two.One.Shape])String.
Unspecified value parameter enc.
    One.writeCsv(shapes)
      */
//    One.writeCsv(shapes)
  }

  val shapes: List[Shape] = List(
    Rectangle(3.0, 4.0),
    Circle(1.0)
  )

  /**
    * 2. Alarmingly, the encoder for CNil throws an exception! Don't panic,
    * though. Remeber that we can't create values of type CNil, so the
    * throw expression is dead code. It's ok to fail abruptly here because we
    * will never reach this point.
    */
  implicit val cnilEncoder: CsvEncoder[CNil] =
    Two.createEncoder(cnil => throw new Exception("Inconceivable!"))

  /**
    * 1. Because Coproducts are disjunctions of types, the encoder for :+: has
    * to choose whether to encode a left or right value. We pattern match on
    * the two subtypes of :+:, which are Inl for left and Inr for right.
    */
  implicit def coproductEncoder[H, T <: Coproduct]
  (implicit hEncoder: CsvEncoder[H], tEncoder: CsvEncoder[T]): CsvEncoder[H :+: T] =
    Two.createEncoder {
    case Inl(h) => hEncoder.encode(h)
    case Inr(t) => tEncoder.encode(t)
  }



}
