import fpinscala.errorhandling._

Some(1).map(_ + 1)

val none = None: Option[Int]
val some = Some(10)

none.map(a => Some(a + 1))
some.map(a => Some(a + 1))
none.flatMap(a => Some(a + 1))
some.flatMap(a => Some(a + 1))

some.filter(a => a < 0)
some.filter(a => a > 0)

Option.variance(Seq(-1, 1))

Option.map2(Some(1), Some(2))(_ + _)
