import java.util.Date

object EqExample extends App {

  import cats.Eq
  import cats.instances.int._

  val eqInt = Eq[Int]
  eqInt.eqv(5, 5)
  eqInt.eqv(1, 5)

  // eqInt.eqv(1, "1")  // compilation error here!

  import cats.syntax.eq._
  123 === 123
  123 =!= 125
  // 111 === "111"  // compilation error here!

  import cats.instances.option._
  // Some(1) === None  // compilation error here!
  (Some(1) : Option[Int]) === (None : Option[Int])
  Option(1) === Option.empty[Int]

  import cats.syntax.option._
  1.some === none[Int]
  1.some =!= none[Int]

  import cats.instances.long._
  implicit val dateEq: Eq[Date] =
    Eq.instance[Date] { (date1, date2) =>
      date1.getTime === date2.getTime
    }

  val x = new Date()
  val y = new Date()

  x === y
  x =!= y
}
