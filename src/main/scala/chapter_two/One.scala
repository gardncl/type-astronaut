package chapter_two


object One {
  //These are still products, but they are more generic because any code that
  //operates on a pair of doubles, or just a double will work with Rectangle2
  //and Circle2
  type Rectangle2 = (Double, Double)
  type Circle2 = Double
  type Shape2 = Either[Rectangle2, Circle2]

  def apply(): Unit = {

    println("Using traits and case classes:")

    //Shapes which are products
    val rectangle: Shape = Rectangle(3.0, 4.0)
    val circle: Shape = Circle(1.0)
    println(rectangle.toString)
    println("Area of rectangle: "+area(rectangle))
    println(circle.toString)
    println("Area of circle: "+area(circle))

    println("\nUsing tuples and Either:")

    //More generic ADTs
    val rect2: Shape2 = Left((3.0, 4.0))
    val circ2: Shape2 = Right(1.0)
    println(rect2.toString)
    println("Area of rectangle: "+area2(rect2))
    println(circ2.toString)
    println("Area of circle: "+area2(circ2))

  }

  def area(shape: Shape): Double = {
    shape match {
      case Rectangle(w, h) => w * h
      case Circle(r) => math.Pi * r * r
    }
  }

  def area2(shape: Shape2): Double = {
    shape match {
      case Left((w,h)) => w * h
      case Right(r) => Math.PI * r * r
    }
  }

}

//Traits are coproducts because they define 'or' relationships
sealed trait Shape {
  override def toString: String = {
    this match {
      case Rectangle(w, h) => s"Rectangle with width $w and height $h"
      case Circle(r) => s"Circle with radius $r"
    }
  }
}
//Case classes are products because they define 'and' relationships
final case class Rectangle(width: Double, height: Double) extends Shape
final case class Circle(radius: Double) extends Shape

