
import fpinscala.state._
import fpinscala.state.RNG._

val rng = Simple(Int.MinValue)
val sameRng = Same(Int.MinValue)
nonNegativeInt(sameRng)

double(sameRng)
double3(sameRng)

double(rng)
double3(rng)
