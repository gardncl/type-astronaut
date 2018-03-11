import shapeless._

object Runner extends App{
  main()

  def main(): Unit = {
    chapter_two.Three()

  }

  def chapterTwo() = {
    chapter_two.One()
    chapter_two.Two()
    chapter_two.Three()
  }
}
