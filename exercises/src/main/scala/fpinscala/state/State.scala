package fpinscala.state


trait RNG {
  def nextInt: (Int, RNG) // Should generate a random `Int`. We'll later define other functions in terms of `nextInt`.
}

object RNG {
  // NB - this was called SimpleRNG in the book text

  case class Simple(seed: Long) extends RNG {
    def nextInt: (Int, RNG) = {
      val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL // `&` is bitwise AND. We use the current seed to generate a new seed.
      val nextRNG = Simple(newSeed) // The next state, which is an `RNG` instance created from the new seed.
      val n = (newSeed >>> 16).toInt // `>>>` is right binary shift with zero fill. The value `n` is our new pseudo-random integer.
      (n, nextRNG) // The return value is a tuple containing both a pseudo-random integer and the next `RNG` state.
    }
  }

  case class Same(seed: Long) extends RNG {
    def nextInt: (Int, RNG) = (seed.toInt, Same(seed))
  }

  type Rand[+A] = RNG => (A, RNG)

  val int: Rand[Int] = _.nextInt

  def unit[A](a: A): Rand[A] =
    rng => (a, rng)

  def map[A,B](s: Rand[A])(f: A => B): Rand[B] =
    rng => {
      val (a, rng2) = s(rng)
      (f(a), rng2)
    }

  def nonNegativeInt(rng: RNG): (Int, RNG) = {
    val (newInt, newRng) = rng.nextInt
    val DefaultIntValue = Int.MaxValue
    val safeNewInt = if (newInt == Int.MinValue) {
      DefaultIntValue // in case Int.MinValue, which is negative and has no positive counter-part
    } else {
      newInt
    }

    (Math.abs(safeNewInt), newRng)
  }

  def double(rng: RNG): (Double, RNG) = {
    val (newPosInt, newRng) = nonNegativeInt(rng)
    (newPosInt.toDouble / Int.MaxValue, newRng)
  }

  def intDouble(rng: RNG): ((Int,Double), RNG) = ???

  def doubleInt(rng: RNG): ((Double,Int), RNG) = ???

  def double3(rng0: RNG): ((Double, Double, Double), RNG) = {
    val (double1, rng1) = double(rng0)
    val (double2, rng2) = double(rng1)
    val (double3, rng3) = double(rng2)
    ((double1, double2, double3), rng3)
  }


  def ints(count: Int)(rng: RNG): (List[Int], RNG) = {
    (0 until count).foldRight[(List[Int], RNG)]((Nil, rng)) { case (x, (l, r)) => (r.nextInt._1 :: l, r.nextInt._2) }
  }

  def map2[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] = { rng0 =>
    val (valueA, rng1) = ra(rng0)
    val (valueB, rng2) = rb(rng1)
    (f(valueA, valueB), rng2)
  }

  def both[A, B](ra: Rand[A], rb: Rand[B]): Rand[(A, B)] = map2[A, B, (A, B)](ra, rb)((_, _))

  def sequence[A](fs: List[Rand[A]]): Rand[List[A]] = fs.foldRight[Rand[List[A]]](unit[List[A]](Nil))((x, xs) => map2(x, xs)(_ :: _))

  def flatMap[A, B](f: Rand[A])(g: A => Rand[B]): Rand[B] = { rng0 =>
    val (value0, rng1) = f(rng0)
    val (value1, rng2) = g(value0)(rng1)
    (value1, rng2)
  }

}

case class State[S,+A](run: S => (A, S)) {
  def map[B](f: A => B): State[S, B] =
    ???
  def map2[B,C](sb: State[S, B])(f: (A, B) => C): State[S, C] =
    ???
  def flatMap[B](f: A => State[S, B]): State[S, B] =
    ???
}

sealed trait Input
case object Coin extends Input
case object Turn extends Input

case class Machine(locked: Boolean, candies: Int, coins: Int)

object State {
  type Rand[A] = State[RNG, A]
  def simulateMachine(inputs: List[Input]): State[Machine, (Int, Int)] = ???
}
