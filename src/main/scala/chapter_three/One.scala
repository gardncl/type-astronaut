package chapter_three

import chapter_two.Two.{Employee, IceCream}
import shapeless.the

object One {
  def apply(): Unit = {
    val employees: List[Employee] = List(
      Employee("Bill", 1, true),
      Employee("Peter", 2, false),
      Employee("Milton", 3, false)
    )

    val iceCreams: List[IceCream] = List(
      IceCream("Sundae", 8, false),
      IceCream("Cornetto", 0, true),
      IceCream("Banana Split", 4, false)
    )

    //Compiler calculates the value of the type parameter and
    //looks for the implicit implementation of it
    println(writeCsv(employees))
    println(writeCsv(iceCreams))
    //the compiler's implicit resolution makes this line work
    println(writeCsv(employees zip iceCreams))

    val iceCreamEncoder: CsvEncoder[IceCream] =
      CsvEncoder[IceCream]

    val employeeEncoder: CsvEncoder[Employee] =
      the[CsvEncoder[Employee]]

    println(iceCreamEncoder.encode(iceCreams.head))
    println(employeeEncoder.encode(employees.head))
  }

  def writeCsv[A](values: List[A])(implicit enc: CsvEncoder[A]): String =
    values
      .map(value =>
        enc.encode(value).mkString(","))
      .mkString("\n")

  //A type class is a parameterised trait representing some sort of
  //general functionality that we would like to apply to a wide range of types
  trait CsvEncoder[A] {
    def encode(value: A): List[String]
  }

  //We implement type classes with instances for each type we care about.
  //If we want them automatically in scope put them in the companion object
  //like done below, otherwise put them in a separate library object.
  object CsvEncoder {
    implicit val employeeEncoder: CsvEncoder[Employee] =
      new CsvEncoder[Employee] {
        override def encode(e: Employee): List[String] =
          List(e.name,
            e.number.toString,
            if(e.isManager) "yes" else "no")
      }

    //the 'instance' method, sometimes named pure, provides a terse
    //syntax  for creating new type class instances, reducing
    //the boilerplate of anaymous class syntax
    implicit val iceCreamEncoder: CsvEncoder[IceCream] =
      (i: IceCream) => List(i.name,
        i.numCherries.toString,
        if (i.inCone) "yes" else "no")

    implicit def pairEncoder[A, B]
    (implicit aEncoder: CsvEncoder[A],
     bEncoder: CsvEncoder[B]): CsvEncoder[(A, B)] = {
      new CsvEncoder[(A,B)] {
        override def encode(pair: (A, B)): List[String] = {
          val (a, b) = pair
          aEncoder.encode(a) ++ bEncoder.encode(b)
        }
      }
    }

    // "Summoner" method
    def apply[A](implicit enc: CsvEncoder[A]): CsvEncoder[A] =
      enc

    // "Constructor" method
    def instance[A](func: A => List[String]): CsvEncoder[A] =
      new CsvEncoder[A] {
        def encode(value: A): List[String] =
          func(value)
      }
  }
}
