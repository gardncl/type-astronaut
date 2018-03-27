package chapter_four

import shapeless.Generic

object One {

  def apply(): Unit = {
    //Dependent typing: the result type of getRepr depends on
    //its value parameters via their type members.
    println(getRepr(Vec(1,2)))
    println(getRepr(Rect(Vec(0,0), Vec(5,5))))
  }

  case class Vec(x: Int, y: Int)
  case class Rect(origin: Vec, size: Vec)

  def getRepr[A](value: A)(implicit gen: Generic[A]) =
    gen.to(value)
}
