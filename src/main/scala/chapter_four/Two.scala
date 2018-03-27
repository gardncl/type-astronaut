package chapter_four

import shapeless.Generic.Aux
import shapeless.{::, HList, HNil}
import shapeless.ops.hlist.Last

object Two {

  def apply() = {
    val last1 = Last[String :: Int :: HNil]
    val last2 = Last[Int :: String :: HNil]
    println(last1)
    println(last2)

    println(last1("foo" :: 123 :: HNil))
    println(last2(123 :: "foo" :: HNil))
  }

  trait Second[L <: HList] {
    type Out
    def apply(value: L): Out
  }

  object Second {
    type Aux[L <: HList, O] = Second[L] {type Out = O}

    def apply[L <: HList](implicit inst: Second[L]): Aux[L, inst.Out] =
      inst
  }

//  implicit def hlistSecond[A, B, Rest <: HList]: Aux[A :: B :: Rest, B] =
//    new Second[A :: B :: Rest] {
//      type Out = B
//      def apply(value: A :: B :: Rest): B =
//        value.tail.head
//    }

}
