package chapter_five

import shapeless.{HNil, Witness}
import shapeless.labelled.{FieldType, KeyTag, field}
import shapeless.syntax.SingletonOps
import shapeless.syntax.singleton._

object Two {

  def apply(): Unit = {

    val number = 42
    //We can midify the type of number at compile time
    //without modifying its runtime behavior by "tagging"
    //it with a "phantom type". Phantom types are types with
    //no runtime semantics
    val numCherries1: Int with Cherries = number.asInstanceOf[Int with Cherries]

    val someNumber = 123

//    val numCherries2: FieldType[SingletonOps#T, Int] = "numCherries" ->> someNumber

    //numCherries3 is a simplified syntax of numCherries2
    val numCherries3: FieldType[Cherries, Int] = field[Cherries](123)

//    println(getFieldName(numCherries2))
//    println(getFieldValue(numCherries2))
//    println(getFieldName(numCherries3))
    println(getFieldValue(numCherries3))

    //Error:(32, 30) type mismatch;
    //found   : String with shapeless.labelled.KeyTag[String("cat"),String] :: Boolean with shapeless.labelled.KeyTag[String("orange"),Boolean] :: shapeless.HNil
    //required: shapeless.labelled.FieldType[shapeless.syntax.SingletonOps#T,String] :: shapeless.labelled.FieldType[shapeless.syntax.SingletonOps#T,Boolean] :: shapeless.HNil
    //(which expands to)  String with shapeless.labelled.KeyTag[shapeless.syntax.SingletonOps#T,String] :: Boolean with shapeless.labelled.KeyTag[shapeless.syntax.SingletonOps#T,Boolean] :: shapeless.HNil
    //("cat" ->> "Garfield") :: ("orange" ->> true) :: HNil


    //val garfield: shapeless.::[FieldType[SingletonOps#T, String], shapeless.::[FieldType[SingletonOps#T, Boolean], HNil]] =
    //  ("cat" ->> "Garfield") :: ("orange" ->> true) :: HNil
  }

  trait Cherries

  def getFieldName[K, V](value: FieldType[K, V])
                        (implicit witness: Witness.Aux[K]): K = witness.value

  def getFieldValue[K, V](value: FieldType[K, V]): V = value
}
