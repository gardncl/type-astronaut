package chapter_seven

import shapeless._
import scala.math.Numeric

object Two {

  def apply() = {
    //    val baz: Double = myPoly.apply(123)
    //    println(baz)
    //    val qux: Int = myPoly.apply("123")
    //    println(qux)

    val product: Int = multiply(3, 4)
    println(product)
    val stringProduct: String = multiply(3, "4")
    println(stringProduct)

//    val total10: Double = total(10)
//    println(total10)
//    val totalOption: Double = total(Option(20))
//    println(totalOption)
//    val totalList: Double = total(List(1L, 2L, 3L))
//    println(totalList)
  }


  /**
    * 1) We're extending a trait called Poly1. Shapeless has a Poly type and
    * a set of subtypes, Poly1 through Poly 22, supporting different arities
    * of polymorphic functions.
    *
    * 2) The Case.Aux types doesn't seem to reference the singleton type of
    * the Poly.Case.Aux is actually a type alias defined withing the body of
    * Poly1. The singleton type is there--we just don't see it.
    *
    * 3) We're using a helper method, at, to define cases. This acts as an
    * instance constructor method as discussed in Section 3.1.2), which
    * eliminates a lot of boilerplate.
    */
  object myPoly extends Poly1 {
    implicit val intCase: Case.Aux[Int, Double] =
      at(num => num / 2.0)

    implicit val stringCase: Case.Aux[String, Int] =
      at(str => str.length)
  }

  object multiply extends Poly2 {
    implicit val intIntCase: Case.Aux[Int, Int, Int] =
      at((a, b) => a * b)

    implicit val intStrCase: Case.Aux[Int, String, String] =
      at((a, b) => b.toString * a)
  }

  object total extends Poly1 {
    implicit def base[A](implicit num: Numeric[A]): Case.Aux[A, Double] =
      at(num.toDouble)

    implicit def option[A](implicit num: Numeric[A]): Case.Aux[Option[A], Double] =
      at(opt => opt.map(num.toDouble).getOrElse(0.0))

    implicit def list[A](implicit num: Numeric[A]): Case.Aux[List[A], Double] =
      at(list => num.toDouble(list.sum))
  }

}
