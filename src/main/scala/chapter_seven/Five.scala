package chapter_seven

import shapeless._
import shapeless.ops.hlist

object Five {

  def apply() = {

  }

  trait ProductMapper[A, B, P] {
    def apply(a: A): B
  }

  implicit def genericProductMapper[A, B, P <: Poly, ARepr <: HList, BRepr <: HList]
  (
  implicit
  aGen: Generic.Aux[A, ARepr],
  bGen: Generic.Aux[B, BRepr],
  mapper: hlist.Mapper.Aux[P, ARepr, BRepr]
  ): ProductMapper[A, B, P] =
    new ProductMapper[A, B, P] {
      def apply(a: A): B =
        bGen.from(mapper.apply(aGen.to(a)))
    }

}
