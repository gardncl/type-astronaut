package chapter_three

import java.util.Date

import chapter_three.One.CsvEncoder
import chapter_two.Two.IceCream
import shapeless.{::, Generic, HList, HNil}

object Two {

  def apply(): Unit = {
    val iceCreamGeneric = reprEncoder.encode("abc" :: 123 :: true :: HNil)
    println(iceCreamGeneric)

    println(One.writeCsv(One.iceCreams))

  }

  //Foo is not an ADT (case class or sealed trait)
//  def wontCompile1 = {
//    One.writeCsv(List(new Foo("abc", 123)))
//  }

  //No implicit converter for Date
  //  def wontCompile2 = {
  //    One.writeCsv(List(Booking("Lecture hall", new Date()))
  //  }

  case class Booking(room: String, date: Date)
  class Foo(bar: String, baz: Int)

  def createEncoder[A](func: A => List[String]): CsvEncoder[A] =
    (value: A) => func(value)

  implicit val stringEncoder: CsvEncoder[String] =
    createEncoder(str => List(str))

  implicit val intEncoder: CsvEncoder[Int] =
    createEncoder(i => List(i.toString))

  implicit val booleanEncoder: CsvEncoder[Boolean] =
    createEncoder(bool => if (bool) List("yes") else List("no"))

  implicit val hnilEncoder: CsvEncoder[HNil] =
    createEncoder(hnil => Nil)

  //Implicit encoders for the individual parts above can be combined recursively
  //to handle any case class using them
  implicit def hlistEncoder[H, T <: HList]
  (implicit hEncoder: CsvEncoder[H], tEncoder: CsvEncoder[T]): CsvEncoder[H :: T] =
    createEncoder {
      case h :: t =>
        hEncoder.encode(h) ++ tEncoder.encode(t)

    }

  val reprEncoder: CsvEncoder[String :: Int :: Boolean :: HNil] =
    implicitly

  val iceCreamEncoder: CsvEncoder[IceCream] = {
    val gen = Generic[IceCream]
    val enc = CsvEncoder[gen.Repr]
    createEncoder(iceCream => enc.encode(gen.to(iceCream)))
  }

  //Given a type A and it's generic encoder R convert any case class
  //with that generic encoding to a csv
  //Given a type A and an HList type R, an implicit Generic to map A
  //to R, and a CsvEncoder for R, create a CsvEncoder for A.
  implicit def genericEncoder[A, R]
  (implicit gen: Generic.Aux[A, R],
   enc: CsvEncoder[R]): CsvEncoder[A] =
    createEncoder(a => enc.encode(gen.to(a)))

}
