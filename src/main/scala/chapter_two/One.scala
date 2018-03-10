package chapter_two

object One {
  def apply(): Unit = {
    //Shapes
    val rectangle: Shape = Rectangle(3.0, 4.0)
    val circle: Shape = Circle(1.0)
  }

  def area(shape: Shape)
}

//Traits are coproducts because they define 'or' relationships
sealed trait Shape
//Case classes are products because they define 'and' relationships
final case class Rectangle(width: Double, height: Double) extends Shape
final case class Circle(radius: Double) extends Shape