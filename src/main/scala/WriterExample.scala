import cats.Id
import cats.data.WriterT

object WriterExample {

  import cats.data.Writer
  import cats.instances.vector._ // for monoid

  val w: WriterT[Id, Vector[String], Int] =
    Writer(
      Vector(
        "log1",
        "log2"
      ),
      1234
    )

  import cats.syntax.applicative._ // for pure
  type Logged[A] = Writer[Vector[String], A]
  123.pure[Logged]

  import cats.syntax.writer._ // for tell, writer
  val wr: Writer[Vector[String], Unit] = Vector("1", "2", "3").tell

  val wr3: Writer[Vector[String], Int] = 123.writer(Vector("1", "2", "3"))
  wr3.value
  wr3.written
  val (logs, res) = wr3.run

  // The log in a Writer is preserved when we map or flatMap over it
  val ress = for {
    a <- 10.pure[Logged]
    _ <- Vector("1", "2", "3").tell
    b <- 32.writer(Vector("4", "5"))
  } yield a + b
  // (1, 2, 3, 4, 5) and 42

  ress.mapWritten(_.map(_.toUpperCase))
  ress.bimap(
    log => log.map(_.toUpperCase),
    r => r * 100
  )
  ress.mapBoth { (log, res) => val log2 = log.map(_ + "!")
    val res2 = res * 1000
    (log2, res2)
  }

  ress.reset // clears log
  ress.swap
}
