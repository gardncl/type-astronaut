package chapter_three

import shapeless._

object Six {

  trait MyTypeClass[A]

  implicit def intInstance: MyTypeClass[Int] = ???

  implicit def stringInstance: MyTypeClass[String] = ???

  implicit def booleanInstance: MyTypeClass[Boolean] = ???

  implicit def hnilInstance: MyTypeClass[HNil] = ???

  implicit def hlistInstance[H, T <: HList](
                                             implicit
                                             hInstance: Lazy[MyTypeClass[H]],
                                             tInstance: MyTypeClass[T]
                                           ): MyTypeClass[H :: T] = ???

  implicit def cnilInstance: MyTypeClass[CNil] = ???

  implicit def coproductInstance[H, T <: Coproduct](
                                                     implicit
                                                     hInstance: Lazy[MyTypeClass[H]],
                                                     tInstance: MyTypeClass[T]
                                                   ): MyTypeClass[H :+: T] = ???

  implicit def genericInstance[A, R](
                                      implicit
                                      generic: Generic.Aux[A, R],
                                      rInstance: Lazy[MyTypeClass[R]]
                                    ): MyTypeClass[A] = ???
}
