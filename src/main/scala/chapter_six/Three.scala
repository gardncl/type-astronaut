package chapter_six

import cats.Monoid
import cats.instances.all._
import shapeless._
import shapeless.labelled.{field, FieldType}
import shapeless.ops.hlist

object Three {

  def apply() = {
    val v1IceCream = IceCreamV1("sundae", 1, true)
    println(v1IceCream)
    //first iteration works for this using Intersection, but can't reorder
//    val v2aIceCream = v1IceCream.migrateTo[IceCreamV2a]
//    println(v2aIceCream)
    //we add Align to reconfigure the order of fiels, but can't add fields
    val v2bIceCream = v1IceCream.migrateTo[IceCreamV2b]
    println(v2bIceCream)

    //    val v2cIceCream = v1IceCream.migrateTo[IceCreamV2c]
    //    println(v2cIceCream)
  }

  // Original
  case class IceCreamV1(name: String, numCherries: Int, inCone: Boolean)

  // Remove fields:
  case class IceCreamV2a(name: String, inCone: Boolean)

  // Reorder fields:
  case class IceCreamV2b(name: String, inCone: Boolean, numCherries: Int)

  // Insert fields (provided we can determine a default value):
  case class IceCreamV2c(name: String, inCone: Boolean, numCherries: Int, numWaffles: Int)

  trait Migration[A, B] {
    def apply(a: A): B
  }

  implicit class MigrationOps[A](a: A) {
    def migrateTo[B](implicit migration: Migration[A, B]): B =
      migration.apply(a)
  }


  //REMOVING FIELDS
  //step 1: convert A to its generic representation
  //step 2: filter the HList from step 1--only retain fields that are also in B
  //step 3: convert the output of step 2 to B
  //Only works if B has an exact subset of the fields in A and they are in the same order
  /*
  implicit def genericMigration[A, B, ARepr <: HList, BRepr <: HList]
  (
    implicit
    aGen: LabelledGeneric.Aux[A, ARepr],
    bGen: LabelledGeneric.Aux[B, BRepr],
    inter: hlist.Intersection.Aux[ARepr, BRepr, BRepr]
  ): Migration[A, B] = new Migration[A, B] {
    def apply(a: A): B =
      bGen.from(inter.apply(aGen.to(a)))
  }
  */

  //REORDERING FIELDS
  //The Align op lets us reorder the fields in one HList to match the
  //order they appear in another HList
  /*
  implicit def genericMigration[A, B, ARepr <: HList, BRepr <: HList, Unaligned <: HList]
  (
    implicit
    aGen: LabelledGeneric.Aux[A, ARepr],
    bGen: LabelledGeneric.Aux[B, BRepr],
    inter: hlist.Intersection.Aux[ARepr, BRepr, Unaligned],
    align: hlist.Align[Unaligned, BRepr]
  ): Migration[A, B] = new Migration[A, B] {
    def apply(a: A): B =
      bGen.from(align.apply(inter.apply(aGen.to(a))))
  }
  */

  def createMonoid[A](zero: A)(add: (A, A) => A): Monoid[A] =
    new Monoid[A] {
      def empty = zero

      def combine(x: A, y: A): A = add(x, y)
    }

  implicit val hnilMonoid: Monoid[HNil] =
    createMonoid[HNil](HNil)((x, y) => HNil)

  implicit def emptyHList[K <: Symbol, H, T <: HList]
  (
    implicit
    hMonoid: Lazy[Monoid[H]],
    tMonoid: Monoid[T]
  ): Monoid[FieldType[K, H] :: T] =
    createMonoid(field[K](hMonoid.value.empty) :: tMonoid.empty) {
      (x, y) =>
        field[K](hMonoid.value.combine(x.head, y.head)) ::
          tMonoid.combine(x.tail, y.tail)
    }


  //1 use LabelledGeneric to convert A and B to their respective generic representations
  //2 use Intersection to calculate an HList of fields common to A and B
  //3 calculate the types of fields that appear in B but not in A
  //4 use Monoid to caluclate a default value of the type from step 3
  //5 append the common fields from step 2 to the new field from step 4
  //6 use Align to reorder the fields from step 5 in the same order as B
  //7 use Labelled Generic to convert the output of step 6 to B

  implicit def genericMigration[A, B, ARepr <: HList, BRepr <: HList, Common <: HList, Added <: HList, Unaligned <: HList]
  (
    implicit
    aGen: LabelledGeneric.Aux[A, ARepr],
    bGen: LabelledGeneric.Aux[B, BRepr],
    inter: hlist.Intersection.Aux[ARepr, BRepr, Common],
    diff: hlist.Diff.Aux[ARepr, BRepr, Added],
    monoid: Monoid[Added],
    prepend: hlist.Prepend.Aux[Added, Common, Unaligned],
    align: hlist.Align[Unaligned, BRepr]
  ): Migration[A, B] =
    new Migration[A, B] {
      override def apply(a: A): B =
        bGen.from(align(prepend(monoid.empty, inter(aGen.to(a)))))
    }
}
