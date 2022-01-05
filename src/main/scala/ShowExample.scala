import java.util.Date

object ShowExample extends App {

  import cats.Show
  import cats.instances.int._
  import cats.instances.string._

  val showInt = Show.apply[Int]
  val showString = Show.apply[String]

  println(showInt.show(123))
  println(showString.show("hello"))

  import cats.syntax.show._
  println(123.show)
  println("hello".show)

  implicit val showData: Show[Date] =
    (date: Date) => s"${date.getTime}ms since the epoch."
  new Date().show

  Show.show[Date](date => s"${date.getTime}ms since the epoch.")
  Show.fromToString[Date]
}
