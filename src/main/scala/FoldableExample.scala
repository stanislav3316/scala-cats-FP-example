object FoldableExample {

  import cats.Foldable
  import cats.instances.list._
  import cats.instances.lazyList._
  import cats.instances.option._
  import cats.instances.int._
  import cats.instances.string._
  import cats.instances.vector._

  val ints = List(1, 2, 3, 4)
  Foldable[List].foldLeft(ints, 0)(_ + _)

  import cats.Eval
  def bigData: LazyList[Int] = (1 to 100000).to(LazyList)
  val eval: Eval[Long] =
    Foldable[LazyList]
      .foldRight(bigData, Eval.now(0L)) { (num, eval) =>
        eval.map(_ + num)
      }

  Foldable[Option].nonEmpty(Option(42))
  Foldable[List].find(List(1, 2, 3))(_ % 2 == 0)
  Foldable[List].combineAll(List(1, 2, 3))
  Foldable[List].foldMap(List(1, 2, 3))(_.toString)

  val ints2 = List(Vector(1, 2, 3), Vector(4, 5, 6))
  (Foldable[List] compose Foldable[Vector]).combineAll(ints2)

  import cats.syntax.foldable._
  List(1, 2, 3).combineAll
  List(1, 2, 3).foldMap(_.toString)
}
