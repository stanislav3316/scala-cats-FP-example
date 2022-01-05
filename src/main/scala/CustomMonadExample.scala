class CustomMonadExample {

  import cats.Monad
  import scala.annotation.tailrec

  val optionMonad: Monad[Option] = new Monad[Option] {
    def flatMap[A, B](fa: Option[A])(
        f: A => Option[B]
    ): Option[B] = fa.flatMap(f)

    def pure[A](x: A): Option[A] = Some(x)

    @tailrec
    def tailRecM[A, B](a: A)(
        f: A => Option[Either[A, B]]
    ): Option[B] =
      f(a) match {
        case None           => None
        case Some(Left(a1)) => tailRecM(a1)(f)
        case Some(Right(b)) => Some(b)
      }
  }

  import cats.syntax.flatMap._ // for flatMap
  def retry[F[_]: Monad, A](start: A)(f: A => F[A]): F[A] =
    f(start).flatMap { a =>
      retry(a)(f)
    }

  import cats.instances.option._
  retry[Option, Int](100)(a => if (a == 0) None else Some(a - 1))

  import cats.syntax.functor._ // for map
  def retryTailRecM[F[_]: Monad, A](start: A)(f: A => F[A]): F[A] =
    Monad[F].tailRecM(start) { a =>
      f(a).map(a2 => Left(a2))
    }

  retryTailRecM[Option, Int](100000)(a => if (a == 0) None else Some(a - 1))

  import cats.syntax.monad._ // for iterateWhileM
  def retryM[F[_]: Monad, A](start: A)(f: A => F[A]): F[A] =
    start.iterateWhileM(f)(_ => true)
}
