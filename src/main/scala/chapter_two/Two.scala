package chapter_two
import shapeless.Generic.Aux
import shapeless.{HNil, ::, Generic}

object Two {
  def apply(): Unit = {
    println()
    println("Chapter 2.2")
    //HList is either an empty list (just HNil) or a pair ::[H, T] where H is an
    //arbitrary type and T is another HList
    //The compiler knows the length of the HList so it's check at compile time
    val product: String :: Int :: Boolean :: HNil = "Sunday" :: 1 :: false :: HNil

    println("Full HList")
    println(product)
    println("Head of list")
    println(product.head)
    println("Head of tail")
    println(product.tail.head)
    println("Tail of tail")
    println(product.tail.tail)

    println("Prepend a Long to our list:")
    val productTwo: ::[Long, ::[String, ::[Int, ::[Boolean, HNil]]]] =
      42L :: product
    println(productTwo)


    println()
    val iceCreamGen: Aux[IceCream, ::[String, ::[Int, ::[Boolean, HNil]]]] =
      Generic[IceCream]
    val iceCream = IceCream("Sundae", 1, false)
    println("Use generic to print case class as generic representation:")
    val repr = iceCreamGen.to(iceCream)
    println(repr)
    println("Use generic to print generic representation as case class definition:")
    println(iceCreamGen.from(repr))
    println("Create an employee from ice cream:")
    val employee = Generic[Employee].from(Generic[IceCream].to(iceCream))
    println(employee)
    //Scala tuples are actually case classes and can be used with Generic
    val tupleGen = Generic[(String, Int, Boolean)]
    println("Create a generic representation from generic tuple:")
    println(tupleGen.from(repr))
  }

  //Exact same generic representation
  case class IceCream(name: String, numCherries: Int, inCone: Boolean)
  case class Employee(name: String, numCherries: Int, inCone: Boolean)
}
