package chapter_seven

import shapeless.{HNil, Poly2}

object Four {

  def apply() = {
    val hlistFold: Int = (10 :: "hello" :: 100 :: HNil).foldLeft(0)(sum)
    println(hlistFold)
  }

  object sum extends Poly2 {
    implicit val intIntCase: Case.Aux[Int, Int, Int] =
      at((a, b) => a + b)

    implicit val intStringCase: Case.Aux[Int, String, Int] =
      at((a, b) => a + b.length)
  }

}
