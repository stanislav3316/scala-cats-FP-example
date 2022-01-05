object ParallelExample {

  import cats.Semigroupal
  import cats.instances.either._

  type ErrorOr[A] = Either[Vector[String], A]
  val error1: ErrorOr[Int] = Left(Vector("err1"))
  val error2: ErrorOr[Int] = Left(Vector("err2"))

  Semigroupal[ErrorOr]
    .product(error1, error2)
  // res0: ErrorOr[(Int, Int)] = Left(Vector("Error 1"))

  import cats.instances.vector._
  import cats.syntax.apply._ // for tupled

  (error1, error2).tupled
  // res0: ErrorOr[(Int, Int)] = Left(Vector("Error 1"))

  import cats.syntax.parallel._
  (error1, error2).parTupled
  // res2: ErrorOr[(Int, Int)] = Left(Vector("Error 1", "Error 2"))

  val success1: ErrorOr[Int] = Right(1)
  val success2: ErrorOr[Int] = Right(2)
  val addTwo = (x: Int, y: Int) => x + y

  (error1, error2).parMapN(addTwo)
  // res4: ErrorOr[Int] = Left(Vector("Error 1", "Error 2"))
  (success1, success2).parMapN(addTwo)
  // res5: ErrorOr[Int] = Right(3)
}
