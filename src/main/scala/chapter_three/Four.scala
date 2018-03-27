package chapter_three

import chapter_three.One.CsvEncoder
import chapter_three.Two._
import chapter_three.Three._

object Four {

  def apply() = {
    val foo = CsvEncoder[Tree[Int]]

    println(foo)
  }

  sealed trait Tree[A]
  case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
  case class Leaf[A](value: A) extends Tree[A]
}
