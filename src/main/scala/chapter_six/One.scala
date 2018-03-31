package chapter_six

import shapeless.{HList, HNil, ::}
import shapeless.ops.hlist.{Init, Last}

object One {

  def apply() = {
    val foo: Boolean = { "Hello" :: 123 :: true :: HNil}.last
    val qux: shapeless.::[String, shapeless.::[Int, HNil]] = { "Hello" :: 123 :: true :: HNil}.init
    println(foo)
    println(qux)
  }

  //simplified definitions of HList extention methods for shapeless.ops.hlist._
  implicit class HListOps[L <: HList](l : L) {
    def last(implicit last: Last[L]): last.Out = last.apply(l)
    def init(implicit init: Init[L]): init.Out = init.apply(l)
  }
  //The return type of each method is determined by a dependent type on the
  //implicit parameter. The instances for each type class provide the actual
  //mapping. Here's the skeleton definition of Last as an example:
  trait LastSkeleton[L <: HList] {
    type Out
    def apply(in: L): Out
  }

  object LastSkeleton {
    type Aux[L <: HList, O] = Last[L] { type Out = O }
    implicit def pair[H]: Aux[H :: HNil, H] = ???
    implicit def list[H, T <: HList]
    (implicit last: Last[T]): Aux[ H :: T, last.Out] = ???
  }

}
