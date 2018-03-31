package chapter_six

import shapeless._
import shapeless.record._
import shapeless.labelled.FieldType

object Four {

  def apply() = {
    val sundae =
      LabelledGeneric[IceCream].to(IceCream("Sundae", 1, false))

    //fetch a field by tag using record extension methods
    println(sundae.get('name))
    println(sundae.get('numCherries))
    println(sundae.updated('numCherries, 3))
    println(sundae.get('numCherries))
    println(sundae.remove('inCone))
    println(sundae.updateWith('name)("Massive " + _))
    println(sundae.toMap)
  }

  case class IceCream(name: String, numCherries: Int, inCone: Boolean)

}
