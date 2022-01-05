object ApplyExample {

  import cats.syntax.apply._
  import cats.instances.option._

  val a: Option[(Int, String)] =
    (Option(123), Option("abc")).tupled

  final case class Cat(name: String, born: Int, color: String)

  (
    Option("Garfield"),
    Option(1978),
    Option("Orange & black")
  ).mapN(Cat.apply) // Some(Cat(...))

  import cats.Monoid
  import cats.instances.int._
  import cats.instances.invariant._ // for Semigroupal
  import cats.instances.list._
  import cats.instances.string._

  final case class Cat2(
      name: String,
      yearOfBirth: Int,
      favoriteFoods: List[String]
  )

  val tupleToCat2: (String, Int, List[String]) => Cat2 = Cat2.apply
  val cat2ToTuple: Cat2 => (String, Int, List[String]) = cat => (cat.name, cat.yearOfBirth, cat.favoriteFoods)

  implicit val cat2Monoid: Monoid[Cat2] =
    (
      Monoid[String],
      Monoid[Int],
      Monoid[List[String]]
    ).imapN(tupleToCat2)(cat2ToTuple)

  import cats.syntax.semigroup._ // for |+|

  val garfield = Cat2("Garfield", 1978, List("Lasagne"))
  val heathcliff = Cat2("Heathcliff", 1988, List("Junk Food"))
  garfield |+| heathcliff
}
