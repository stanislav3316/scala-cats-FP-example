
object ContravariantInvariantExample extends App {

  import cats.Contravariant
  import cats.Show
  import cats.instances.string._

  val showString = Show[String]
  val showSymbol: Show[Symbol] =
    Contravariant[Show]
      .contramap(showString)((sym: Symbol) => s"'${sym.name}")

  showSymbol.show(Symbol("hello"))

  import cats.syntax.contravariant._
  // contramap когда что-то выводится наружу может только применяться
  showString
    .contramap[Symbol](sym => s"'${sym.name}")
    .show(Symbol("hello"))

  import cats.Monoid
  import cats.syntax.invariant._ // imap
  import cats.syntax.semigroup._ // for |+|

  implicit val symbolMonoid: Monoid[Symbol] =
    Monoid[String]
      .imap(Symbol.apply)(_.name)

  Monoid[Symbol].empty
  Monoid[Symbol].combine(Symbol("a"), Symbol("b"))

  Symbol("a") |+| Symbol("b") |+| Symbol("c")
}
