import scala.concurrent.{ExecutionContext, Future}

object FunctorExample extends App {

  val func1: Int => Double =
    (x: Int) => x.toDouble

  val func2: Double => Double =
    (y: Double) => y * 2

  import cats.instances.function._
  import cats.syntax.functor._
  (func1 map func2)(1) // with Cats
  func1.map(func2)(1)  // with Cats

  (func1 andThen func2)(1) // in Scala lib
  func2(func1(1))

  import cats.Functor
  import cats.instances.list._
  val list1 = List(1, 2, 3)
  Functor[List].map(list1)(_ * 2)

  import cats.instances.option._
  val option1 = Option(123)
  Functor[Option].map(option1)(_.toString)

  val func = (x: Int) => x + 1
  val f: Option[Int] => Option[Int] =
    Functor[Option].lift(func)

  Functor[List].as(list1, "As")
  list1.as("As")

  def doMath[F[_]](start: F[Int])(
      implicit functor: Functor[F]
  ): F[Int] =
    start.map(n => n + 1 * 2)

  doMath(Option(20))
  doMath(List(1, 2, 3))

  val functor = new Functor[Option] {
    override def map[A, B](value: Option[A])(f: A => B): Option[B] =
      value.map(f)
  }

  def futureFunctor(implicit ec: ExecutionContext): Functor[Future] =
    new Functor[Future] {
      override def map[A, B](value: Future[A])(f: A => B): Future[B] =
        value.map(f)
    }
}
