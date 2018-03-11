package chapter_two
import chapter_two.One.{Circle, Rectangle, Shape}
import shapeless.{:+:, CNil, Coproduct, Generic, Inl, Inr}

object Three {

  def apply() = {
    val red: Light = Inl(Red())

    val green: Light = Inr(Inr(Inl(Green())))
    println("Red in a head:")
    println(red)
    println("Green in a tail:")
    println(green)

    val gen = Generic[Shape]
    println()
    println("Generic understands sealed traits and abstract classes:")
    println(gen.to(Rectangle(3.0, 4.0)))
    println(gen.to(Circle(3.0)))
  }

  case class Red()
  case class Amber()
  case class Green()

  //This is a coproduct meaning `Red` or `Amber` or `Green`
  // :+: can loosely be interpreted as `either`
  type Light = Red :+: Amber :+: Green :+: CNil

}
