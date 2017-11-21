import fpinscala.laziness.Stream
import fpinscala.laziness.Stream._

def ones: Stream[Int] = cons[Int](1, ones)
val st = Stream(1, 2, 3, 4, 5)
val st2 = Stream(1.0, 2.0)
def inc(i: Int) = i + 1
val takeAmnt = 10
("To list: " + st.toList)
("To list2: " + st.toList2)
("Take while minor than 3: " + st.takeWhileFR(_ < 3).toList)
(s"Take $takeAmnt: " + st.take(takeAmnt).toList)
("Incr by one: " + st.map(_ + 1).toList)
("Filter pair: " + st.filter(_ % 2 == 0).toList)
("Append : " + st.append(st).toList)
("Append with supertype : " + st.append(st2).toList)
("Unfold : " + unfold(0)(i => Some(i, i + 1)).take(takeAmnt).toList)
("Fib unfold : " + unfold[Int, (Int, Int)]((0, 1))(s => Some(s._1, (s._2, s._1 + s._2))).take(takeAmnt).toList)
("Unfold from 5 : " + unfold[Int, Int](5)(s => Some((s, s + 1))).take(takeAmnt).toList)
("Constant 5 : " + unfold[Int, Int](5)(s => Some((s, s))).take(takeAmnt).toList)
("Ones using unfold : " + unfold[Int, Int](1)(s => Some((s, s))).take(takeAmnt).toList)
("Map using unfold : " + st.mapWithUnfold(inc).take(takeAmnt).toList)
("Take using unfold : " + ones.takeWithUnfold(takeAmnt).toList)
("Zip using unfold : " + ones.zipWithUsingUnfold(st2).toList)
("Tails using unfold : " + Stream(1,2,3).tails.map(_.toList).toList)

