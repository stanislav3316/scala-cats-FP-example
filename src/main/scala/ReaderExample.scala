object ReaderExample {

  import cats.data.Reader
  case class Cat(name: String, food: String)

  val catName: Reader[Cat, String] =
    Reader(cat => cat.name)

  catName.run(Cat("Garfield", "fish"))

  val greetKitty: Reader[Cat, String] =
    catName.map(name => s"Hello $name")

  val feedKitty: Reader[Cat, String] =
    Reader(cat => s"Have a nice bowl of ${cat.food}")

  val greetAndFeed: Reader[Cat, String] =
    for {
      greet <- greetKitty
      feed <- feedKitty
    } yield s"$greet. $feed"

  val b: Reader[Cat, String] =
    greetKitty
      .flatMap { greet =>
        feedKitty
          .map(feed => greet + feed)
    }

  greetAndFeed.run(Cat("Garfield", "fish"))
}
