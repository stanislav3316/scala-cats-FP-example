object SemigroupalExample {

  import cats.Semigroupal
  import cats.instances.option._

  val res: Option[(Int, String)] = // Some((123, "abc"))
    Semigroupal[Option]
      .product(Some(123), Some("abc"))

  val res2: Option[(Nothing, String)] = // None result
    Semigroupal[Option]
      .product(None, Some("abc"))

  Semigroupal
    .tuple3(Option(1), Option(2), Option(3)) // Some((1, 2, ,3))

  Semigroupal
    .map3(Option(1), Option(2), Option(3))(_ + _ + _)
}
