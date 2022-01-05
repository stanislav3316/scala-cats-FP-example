import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object TraverseExample {

  import cats.Applicative
  import cats.instances.future._ // for Applicative
  import cats.syntax.applicative._ // for pure
  import cats.syntax.apply._

  def oldCombine(
      accum : Future[List[Int]],
      host  : String
  ): Future[List[Int]] = {
    val uptime = getUptime(host)
    for {
      accum <- accum
      uptime <- uptime
    } yield accum :+ uptime
  }

  def newCombine(accum: Future[List[Int]], host: String): Future[List[Int]] =
    (accum, getUptime(host)).mapN(_ :+ _)

  def getUptime(hostname: String): Future[Int] =
    Future(hostname.length * 60)

  def listTraverse[F[_]: Applicative, A, B](list: List[A])(func: A => F[B]): F[List[B]] =
    list.foldLeft(List.empty[B].pure[F]) { (accum, item) =>
      (accum, func(item)).mapN(_ :+ _)
    }

  def listSequence[F[_]: Applicative, B] (list: List[F[B]]): F[List[B]] =
    listTraverse(list)(identity)

  import cats.Traverse
  import cats.instances.future._ // for Applicative
  import cats.instances.list._ // for Traverse

  val totalUptime: Future[List[Int]] =
    Traverse[List].traverse(List("a", "b"))(getUptime)

  val numbers = List(Future(1), Future(2), Future(3))
  val numbers2: Future[List[Int]] =
    Traverse[List].sequence(numbers)

  import cats.syntax.traverse._
  List("a", "b").traverse(getUptime)
  numbers.sequence
}
