object MonoidAndSemigroupExample extends App {

  import cats.Monoid
  import cats.instances.string._
  Monoid[String].combine("a", "b")
  Monoid[String].empty

  import cats.Semigroup
  Semigroup[String].combine("a", "b")

  import cats.instances.option._
  Monoid
    .apply[Option[String]]
    .combine(Some("1"), Some("2"))

  import cats.syntax.semigroup._
  "a" |+| "b" |+| Monoid[String].empty

  import cats.instances.int._
  1 |+| 2 |+| Monoid[Int].empty

  def add[A](items: List[A])(implicit monoid: Monoid[A]): A =
    items.fold(monoid.empty)(_ |+| _)

  def add2[A: Monoid](items: List[A]): A =
    items.fold(Monoid[A].empty)(_ |+| _)

  import cats.instances.map._
  val m1 = Map("a" -> 1, "b" -> 2)
  val m2 = Map("b" -> 3, "d" -> 4)
  m1 |+| m2

  import cats.instances.tuple._
  val t1 = ("a", 10)
  val t2 = ("b", 5)
  t1 |+| t2
}
