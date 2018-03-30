package chapter_five

import shapeless.syntax.singleton._

object One {

  def apply() = {

    //A scala value may have multiple types.
    //For example string has at least three:
    "hello": String
    "hello": AnyRef
    "hello": Any
    //"hello" also has a singleton type associated
    //with just that one value which is similar to the
    //type we get when we define a companion object
    val foo = Foo


    //These two are the same because scala widens the
    //second one to its nearest non-singleton type, but
    //its narrower type is a literal type which is a string
    //with only the value "hello"
    "hello"
    ("hello" : String)
    var x = 42.narrow
    //cant reassign, because x is a narrow, literal type
    //    x = 43
    //can still add because Int(42) is a subtype of Int
    //addition still has normal subtyping rules
    val y = x + 1
    println(x)
    println(y)


    //narrow can be used on any literal in scala
    1.narrow
    true.narrow
    "hello".narrow

    //but cannot be used on compound expressions
    //Error:(43, 14) Expression scala.math.`package`.sqrt(4.0) does not evaluate to a constant or a stable reference value
    //math.sqrt(4).narrow
  }

  object Foo

  Foo
}
