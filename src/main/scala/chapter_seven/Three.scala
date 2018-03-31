package chapter_seven

import shapeless._

object Three {

  def apply() = {
    val hlistMap = (10 :: "hello" :: true :: HNil).map(sizeOf)
    println(hlistMap)

    //Error:(11, 38) could not find implicit value for parameter mapper:
    //shapeless.ops.hlist.Mapper[chapter_seven.Three.sizeOf.type,Double :: shapeless.HNil]
//    val errorList = (1.5 :: HNil).map(sizeOf)

    val hlistFlatMap = (10 :: "hello" :: true :: HNil).flatMap(valueAndSizeOf)
    println(hlistFlatMap)
  }

  //polymorphic map
  object sizeOf extends Poly1 {
    implicit val intCase: Case.Aux[Int, Int] =
      at(identity)

    implicit val stringCase: Case.Aux[String, Int] =
      at(_.length)

    implicit val booleanCase: Case.Aux[Boolean, Int] =
      at(bool => if (bool) 1 else 0)
  }

  //polymorphic flatmap
  object valueAndSizeOf extends Poly1 {
    implicit val intCase: Case.Aux[Int, Int :: Int :: HNil] =
      at(num => num :: num :: HNil)

    implicit val stringCase: Case.Aux[String, String :: Int :: HNil] =
      at(str => str :: str.length :: HNil)

    implicit val booleanCase: Case.Aux[Boolean, Boolean :: Int :: HNil] =
      at(bool => bool :: (if(bool) 1 else 0) :: HNil)
  }

}
