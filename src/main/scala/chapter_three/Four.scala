package chapter_three

import chapter_three.One.CsvEncoder

object Four {

  def apply() = {
    /**
      * Error:(8, 15) could not find implicit value for parameter enc: chapter_three.One.CsvEncoder[chapter_three.Four.Tree[Int]]
    CsvEncoder[Tree[Int]]
      Error:(8, 15) not enough arguments for method apply: (implicit enc: chapter_three.One.CsvEncoder[chapter_three.Four.Tree[Int]])chapter_three.One.CsvEncoder[chapter_three.Four.Tree[Int]] in object CsvEncoder.
Unspecified value parameter enc.
    CsvEncoder[Tree[Int]]
      */
//    CsvEncoder[Tree[Int]]
  }

  sealed trait Tree[A]
  case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
  case class Leaf[A](value: A) extends Tree[A]
}
