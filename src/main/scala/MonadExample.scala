import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}
import scala.util.Try

object MonadExample extends App {

  import cats.Monad
  import cats.instances.option._
  import cats.instances.list._

  val opt1 = Monad[Option].pure(3)
  val opt2 = Monad[Option].flatMap(opt1)(a => Some(a + 2))
  val opt3 = Monad[Option].map(opt2)(a => 100 * a)

  val list1 = Monad[List].pure(3)
  val list2 = Monad[List].flatMap(List(1, 2, 3))((a: Int) => List(a, a * 10))
  val list3 = Monad[List].map(list1)((a: Int) => a + 123)

  import cats.instances.future._
  import scala.concurrent.ExecutionContext.Implicits.global
  val fm = Monad[Future]
  Await.result(
    fm.flatMap(fm.pure(""))(x => fm.pure(x + ".")),
    1.second
  )

  import cats.syntax.flatMap._     // flatMap
  import cats.syntax.functor._     // map
  import cats.syntax.applicative._ // pure

  1.pure[Option]
  1.pure[List]

  def square[F[_] : Monad](a: F[Int], b: F[Int]): F[Int] =
    a.flatMap(x => b.map(y => x*x + y*y))

  import cats.Id
  square(3 : Id[Int], 4 : Id[Int])
  val l = List(1, 2, 3) : Id[List[Int]]

  Monad[Id].pure(3)

  val either0: Right[Nothing, Int] = Right(1)
  val either1: Right[Throwable, Int] = Right[Throwable, Int](1)
  val either2: Right[Throwable, Int] = Right[Throwable, Int](2)
  val either3: Either[Throwable, Int] =
    for {
      v1 <- either1
      v2 <- either2
    } yield v1 + v2

  import cats.syntax.either._
  3.asRight[Throwable]
  Either.catchOnly[NumberFormatException]("foo".toInt)
  Either.catchNonFatal(sys.error("aaa"))
  Either.fromTry(Try("foo".toInt))
  Either.fromOption(Some(""), -1)
  "Error".asLeft[Int].getOrElse(0)
  "error".asLeft[Int].orElse(2.asRight[String])
  (-1).asRight[String].ensure("must be non-negative")(_ > 0)
  "error".asLeft[Int].recover {
    case _: String => -1
  }
  "error".asLeft[Int].recoverWith {
    case _:String => Right(-1)
  }
  "foo".asLeft[Int].leftMap(_.reverse)
  6.asRight[String].bimap(_.reverse, _ * 7)
  123.asRight[String].swap // Either[Int, String] = Left(123)
  // toOption, toList, toTry, toValidated
  // 123.asRight[String].fold(handleError, resultHandler)

  import cats.MonadError
  import cats.instances.either._ // for MonadError
  type ErrorOr[A] = Either[String, A]
  val monadError = MonadError[ErrorOr, String]
  val success: ErrorOr[Int] = monadError.pure(412)// Right
  val failure: ErrorOr[String] = monadError.raiseError[String]("error") // Left
  monadError.handleErrorWith(failure) {
    case "error" =>
      monadError.pure("ok")
    case _ =>
      monadError.raiseError("isn't ok")
  }
  monadError.handleError(failure) {
    case "error" => "42"
    case _ => "-1"
  }

  monadError.ensure(success)("Number too low")(_ > 100)

  import cats.syntax.applicative._      // for pure
  import cats.syntax.applicativeError._ // for raiseError etc
  import cats.syntax.monadError._       // for ensure

  val s = 42.pure[ErrorOr]
  s.ensure("low")(_ > 1000)
  "error".raiseError[ErrorOr, Int]

  import scala.util.Try
  import cats.instances.try_._
  val ex: Throwable = new RuntimeException("aaa")
  ex.raiseError[Try, Int]
}
