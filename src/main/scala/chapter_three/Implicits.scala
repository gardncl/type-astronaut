package chapter_three

import chapter_three.One.CsvEncoder
import shapeless.{:+:, ::, CNil, Coproduct, Generic, HList, HNil, Inl, Inr, Lazy}

object Implicits {

  def createEncoder[A](func: A => List[String]): CsvEncoder[A] =
    (value: A) => func(value)

  implicit val stringEncoder: CsvEncoder[String] =
    createEncoder(str => List(str))

  implicit val intEncoder: CsvEncoder[Int] =
    createEncoder(i => List(i.toString))

  implicit val booleanEncoder: CsvEncoder[Boolean] =
    createEncoder(bool => if (bool) List("yes") else List("no"))

  implicit val doubleEncoder: CsvEncoder[Double] =
    createEncoder(d => List(d.toString))

  implicit val hnilEncoder: CsvEncoder[HNil] =
    createEncoder(hnil => Nil)

  implicit def hlistEncoder[H, T <: HList]
  (implicit hEncoder: Lazy[CsvEncoder[H]], //wrap in lazy to avoid implicit divergence
   tEncoder: CsvEncoder[T]): CsvEncoder[H :: T] =
    createEncoder {
      case h :: t =>
        hEncoder.value.encode(h) ++ tEncoder.encode(t)
    }

  implicit def genericEncoder[A, R]
  (implicit gen: Generic.Aux[A, R],
   enc: Lazy[CsvEncoder[R]]): CsvEncoder[A] =
    createEncoder(a => enc.value.encode(gen.to(a)))

  implicit val cnilEncoder: CsvEncoder[CNil] =
    Two.createEncoder(cnil => throw new Exception("Inconceivable!"))

  implicit def coproductEncoder[H, T <: Coproduct]
  (implicit hEncoder: Lazy[CsvEncoder[H]], tEncoder: CsvEncoder[T]): CsvEncoder[H :+: T] =
    Two.createEncoder {
      case Inl(h) => hEncoder.value.encode(h)
      case Inr(t) => tEncoder.encode(t)
    }
}
