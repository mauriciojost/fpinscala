package fpinscala.laziness

import Stream._
trait Stream[+A] {

  def foldRight[B](z: => B)(f: (A, => B) => B): B = // The arrow `=>` in front of the argument type `B` means that the function `f` takes its second argument by name and may choose not to evaluate it.
    this match {
      case Cons(h,t) => f(h(), t().foldRight(z)(f)) // If `f` doesn't evaluate its second argument, the recursion never occurs.
      case _ => z
    }

  def exists(p: A => Boolean): Boolean = 
    foldRight(false)((a, b) => p(a) || b) // Here `b` is the unevaluated recursive step that folds the tail of the stream. If `p(a)` returns `true`, `b` will never be evaluated and the computation terminates early.

  @annotation.tailrec
  final def find(f: A => Boolean): Option[A] = this match {
    case Empty => None
    case Cons(h, t) => if (f(h())) Some(h()) else t().find(f)
  }
  def take(n: Int): Stream[A] = this match {
    case Cons(h, t) if n>0 => Cons(h, () => t().take(n - 1))
    case _ => Empty
  }

  def drop(n: Int): Stream[A] = ???

  def takeWhile(p: A => Boolean): Stream[A] = this match {
    case Cons(h, t) if (p(h())) => Cons(h, () => t().takeWhile(p))
    case Empty => Empty
  }

  def forAll(p: A => Boolean): Boolean = foldRight(true)((a, b) => b && p(a))

  def headOption: Option[A] = this match {
    case Empty => None
    case Cons(h, _) => Some(h())
  }

  // 5.7 map, filter, append, flatmap using foldRight. Part of the exercise is
  // writing your own function signatures.

  def map[B](f: A => B) = foldRight(Empty: Stream[B])((x, xs) => cons(f(x), xs))

  def filter(f: A => Boolean): Stream[A] = foldRight(Empty: Stream[A])( (x, xs) => if (f(x: A)) cons[A](x, xs) else xs )

  def append[B >: A](other: Stream[B]): Stream[B] = foldRight(other)((x, xs) => cons(x, xs)) // can only make a Stream of an upper type to avoid assigning Cats to Dogs

  def startsWith[B](s: Stream[B]): Boolean = {
    (this, s) match {
      case (Cons(h1, t1), Cons(h2, t2)) => (h1() == h2()) && t1().startsWith(t2())
      case (_, Empty) => true
    }
  }

  def mapWithUnfold[B](f: A => B): Stream[B] = {
    unfold[B, Stream[A]](this)(s =>
      s match {
        case Cons(h, t) => Some((f(h()), t()))
        case Empty => None
      }
    )
  }

  def takeWithUnfold(n: Int): Stream[A] = {
    unfold[A, (Stream[A], Int)]((this, n))(s =>
      s match {
        case (Cons(h, t), n) if n > 0 => Some((h(), (t(), n - 1)))
        case _ => None
      }
    )
  }

  def takeWhileWithUnfold(f: A => Boolean): Stream[A] = {
    unfold[A, Stream[A]](this)(s =>
      s match {
        case Cons(h, t) if f(h()) => Some((h(), t()))
        case _ => None
      }
    )
  }

  def zipWithUsingUnfold[B](other: Stream[B]): Stream[(A, B)] = {
    unfold[(A, B), (Stream[A], Stream[B])]((this, other))(s =>
      s match {
        case (Cons(h1, t1), Cons(h2, t2)) => Some(((h1(), h2()), (t1(), t2())))
        case _ => None
      }
    )
  }

  def tails: Stream[Stream[A]] = {
    unfold[Stream[A], Stream[A]](this)(state =>
      state match {
        case s @ Cons(v, rest) => Some(s, rest())
        case _ => None
      }
    )
  }

  def takeWhileFR(p: A => Boolean): Stream[A] = {
    this.foldRight[Stream[A]](Empty: Stream[A]){
      (x, xs) => if (p(x)) cons(x, xs) else Empty: Stream[A]
    }
  }

  import fpinscala.datastructures.{List, Nil, Cons => ListCons}

  def toList: List[A] = {
    this match {
      case Empty => Nil
      case Cons(h, t) => ListCons(h(), t().toList)
    }
  }

  def toList2: List[A] = foldRight(Nil: List[A])((x, xs) => ListCons(x, xs))

}
case object Empty extends Stream[Nothing]
case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]

object Stream {
  def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
    lazy val head = hd
    lazy val tail = tl
    Cons(() => head, () => tail)
  }

  def empty[A]: Stream[A] = Empty

  def apply[A](as: A*): Stream[A] =
    if (as.isEmpty) empty
    else cons(as.head, apply(as.tail: _*))

  val ones: Stream[Int] = Stream.cons(1, ones)

  def from(n: Int): Stream[Int] = ???

  def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] = { // create a Stream from a seed and a function taking previous seed
    f(z) match {
      case Some((a, s)) => cons[A](a, unfold(s)(f))
      case None => Empty
    }
  }
}
