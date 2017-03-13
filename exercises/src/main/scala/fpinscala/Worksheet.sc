import fpinscala.datastructures.List
import fpinscala.datastructures.Tree
import fpinscala.datastructures.Leaf
import fpinscala.datastructures.Branch
import List._
import Tree._
length(List(1, 2, 1, 4, 7))
foldRight(List(1, 2, 3, 4), 0)((a: Int, b: Int) => a - b)
Vector(1, 2,3,4).foldRight(0)((a: Int, b: Int) => a - b) // Standard one
foldLeft(List(1, 2, 3, 4), 0)((a: Int, b: Int) => a - b)
Vector(1, 2, 3, 4).foldLeft(0)((a: Int, b: Int) => a - b)

reverse(List(1, 2, 3))

appendWithFold(List(1, 2, 3), List(4, 5, 6))

flatten(List(List(1, 2, 3), List(4, 5, 6), List(7, 8, 9)))

foldRight(List(1, 2, 3, 4), 0)((a: Int, b: Int) => a - b)

foldRightUsingFoldLeft(List(1, 2, 3, 4), 0)((a: Int, b: Int) => a - b)
Vector(1, 2,3,4).foldRight(0)((a: Int, b: Int) => a - b) // Standard one
mapAddOne(List(1, 2, 3))
map(List(1, 2, 3))(_ + 1)
filter(List(1, 2, 3, 4, 5))(_ % 2 == 0)
flatMap(List(1,2,3))(a => List(a, a))
filterUsingFlatMap(List(-2,-1,0,1,2,3))((a: Int) => a>=0)
List.zipWith(List(1, 2, 3), List(1, 2, 3))((a: Int, b:Int) => a * b)

val tree1 = Branch(Leaf(1), Branch(Leaf(2), Leaf(9)))
size(tree1)
max(tree1)

