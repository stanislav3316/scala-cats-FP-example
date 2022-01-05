object StateExample {

  import cats.data.State
  val a: State[Int, String] =
    State[Int, String] { state: Int =>
      (state, s"is $state")
    }

  val (state, result) = a.run(10).value
  val onlyState: Int = a.runS(10).value
  val onlyResult: String = a.runA(10).value

  val step1: State[Int, String] =
    State[Int, String] { num =>
      val ans = num + 1
      (ans, s"res $ans")
    }

  val step2: State[Int, String] =
    State[Int, String] { num =>
      val ans = num * 2
      (ans, s"res $ans")
    }

  val both: State[Int, (String, String)] =
    for {
      a <- step1
      b <- step2
    } yield (a, b)
  val (s, r) = both.run(20).value

  val getDemo: State[Int, Int] = State.get[Int]
  getDemo.run(10).value

  val setDemo: State[Int, Unit] = State.set[Int](30)
  setDemo.run(30).value

  val pureDemo: State[Int, String] = State.pure[Int, String]("Result")
  pureDemo.run(10).value

  val inspectDemo: State[Int, String] = State.inspect[Int, String](x => s"$x!")
  inspectDemo.run(10).value

  val modifyDemo: State[Int, Unit] = State.modify[Int](_ + 1)
  modifyDemo.run(10).value

  val program: State[String, (String, String, Int)] =
    for {
      a <- State.get[String]
      _ <- State.set[String](a + 1)
      b <- State.get[String]
      _ <- State.modify[String](_ + 1)
      c <- State.inspect[String, Int](_.toInt * 1000)
    } yield (a, b, c)
  program.run("1").value
}
