object EvalMonadExample extends App {

  import cats.Eval
  val now = Eval.now(math.random() + 1000)
  val always = Eval.always(math.random() + 3000)
  val later = Eval.later(math.random() + 2000)

  now.value
  always.value
  later.value

  val greeting =
    Eval
      .always { println("Step 1"); "Hello" }
      .map(str => s"..$str..")
  greeting.value

  val ans: Eval[Int] =
    for {
      a <- Eval.now { println("A"); 40 }
      b <- Eval.always { println("B"); 2 }
    } yield {
      println("A + B")
      a + b
    }

  ans.value
  ans.value

  val saying = Eval
    .always { println("Step 1"); "The cat" }
    .map { str => println("Step 2"); s"$str sat on" }
    .memoize
    .map { str => println("Step 3"); s"$str the mat" }

  def factorial(n: BigInt): Eval[BigInt] =
    if (n == 1) Eval.now(n)
    else Eval.defer(factorial(n - 1).map(_ * n))
}
