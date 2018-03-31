import shapeless._

object Runner extends App{
  main()

  def main(): Unit = {
    chapter_seven.Four()
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
    chapter_three.Four()
  }

  def chapterFour() = {
    chapter_four.One()
    chapter_four.Two()
  }

  def chapterFive() = {
    chapter_five.One()
    chapter_five.Two()
    chapter_five.Three()
    chapter_five.Four()
  }

  def chapterSix() = {
    chapter_six.One()
    chapter_six.Two()
    chapter_six.Three()
    chapter_six.Four()
  }

  def chapterSeven() = {
    chapter_seven.Two()
    chapter_seven.Three()
    chapter_seven.Four()
  }
}
