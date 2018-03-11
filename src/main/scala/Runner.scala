import shapeless._

object Runner extends App{
  main()

  def main(): Unit = {
    chapter_three.One()
  }

  def chapterTwo() = {
    chapter_two.One()
    chapter_two.Two()
    chapter_two.Three()
  }

  def chapterThree() = {
    chapter_three.One()
  }
}
