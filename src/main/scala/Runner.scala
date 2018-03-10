import shapeless._

object Runner extends App{
  main()

  def main(): Unit = {
    val product: String :: Int :: Boolean :: HNil =
      "Sunday" :: 1 :: false :: HNil

    println(product.head)
    println(product.tail)


  }
}
