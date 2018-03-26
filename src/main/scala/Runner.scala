import shapeless._

object Runner extends App{
  main()

  def main(): Unit = {
    chapter_three.Three()
  }

  def chapterTwo() = {
    chapter_two.One()
    chapter_two.Two()
    chapter_two.Three()
  }

  def chapterThree() = {
    chapter_three.One()
    chapter_three.Two()
    chapter_three.Three()
  }
}
