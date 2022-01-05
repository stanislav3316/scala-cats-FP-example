object PartialUnificationExample extends App {

  import cats.Functor
  import cats.instances.function._
  import cats.syntax.functor._

  val func1 = (x: Int) => x.toDouble
  val func2 = (y: Double) => y * 2

  val func3: Int => Double =
    func1.map(func2)

  type F[A] = Int => A
  val functor = Functor[F]

  val func3a: Int => Double =
    a => func2(func1(a))

  val func3b: Int => Double =
    func2.compose(func1)

  // func3a === func3b
}
