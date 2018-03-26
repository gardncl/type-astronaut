package chapter_three

import chapter_three.One.CsvEncoder
import chapter_three.Two._
import chapter_two.One.{Circle, Rectangle, Shape}
import shapeless.{:+:, CNil, Coproduct, Inl, Inr, Lazy}

object Three {

  def apply() = {
    val foo = One.writeCsv(shapes)
    println(foo)
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
  (implicit hEncoder: Lazy[CsvEncoder[H]], tEncoder: CsvEncoder[T]): CsvEncoder[H :+: T] =
    Two.createEncoder {
    case Inl(h) => hEncoder.value.encode(h)
    case Inr(t) => tEncoder.encode(t)
  }



}
