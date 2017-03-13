package fpinscala.datastructures

sealed trait Tree[+A]
case class Leaf[A](value: A) extends Tree[A]
case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]


object Tree {

  def size[A](t: Tree[A]): Int = {
    t match {
      case Leaf(a) => 1
      case Branch(a, b) => size(a) + size(b)
    }
  }

  def max(t: Tree[Int]): Int = {
    t match {
      case Leaf(a) => a
      case Branch(a, b) => max(a) max max(b)
    }
  }

  def depth[A](t: Tree[A], i: Int = 0): Int = {
    t match {
      case Leaf(a) => i + 1
      case Branch(a, b) => (depth(a, i + 1) max depth(b, i + 1))
    }
  }

  def map[A, B](t: Tree[A])(f: A => B): Tree[B] = {
    t match {
      case Leaf(a) => Leaf(f(a))
      case Branch(a, b) => Branch(map(a)(f), map(b)(f))
    }
  }

}