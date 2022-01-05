object MonadTransformerExample {

  import cats.data.OptionT

  type ListOption[A] = OptionT[List, A]

  import cats.instances.list._     // for Monad
  import cats.syntax.applicative._ // for pure

  val result1: ListOption[Int] = OptionT(List(Option(10)))
  val result2: ListOption[Int] = 32.pure[ListOption]

  val result3: ListOption[Int] =
    result1.flatMap { a: Int =>
      result2.map { b: Int =>
        a + b
      }
    }

  type ErrorOr[A] = Either[String, A]
  type ErrorOrOption[A] = OptionT[ErrorOr, A]

  import cats.instances.either._

  val a: ErrorOrOption[Int] = 10.pure[ErrorOrOption]
  val b: ErrorOrOption[Int] = 32.pure[ErrorOrOption]
  val c: OptionT[ErrorOr, Int] = a.flatMap(x => b.map(y => x + y))

  val res: ErrorOr[Option[Int]] = c.value
}
