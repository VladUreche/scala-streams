package streams

object Test {

  def main(args: Array[String]): Unit = {

    val N    = 10000000
    val v    = Array.tabulate(N)(i => i.toLong % 1000)
    val vHi  = Array.tabulate(1000000)(_.toLong)
    val vLo  = Array.tabulate(10)(_.toLong)

    println("streams: sum")
    for (i <- 1 to 8) timed {
      val streams_sum = Stream(v)
        .sum
    }

    println("streams: sumOfSquares")
    for (i <- 1 to 8) timed {
      val sumOfSquares = Stream(v)
        .map(d => d * d)
        .sum
    }

    println("streams: sumOfSquaresEven")
    for (i <- 1 to 8) timed {
      val sumOfSquaresEven = Stream(v)
        .filter(x => x % 2 == 0)
        .map(x => x * x)
        .sum
    }

    println("streams: steamsCart")
    for (i <- 1 to 8) timed {
      val sumCart : Long = Stream(vHi)
        .flatMap(d => Stream(vLo).map (dp => dp * d))
        .sum
    }
  }

  def timed[@generic T](t: => T): T = {
    val start = System.currentTimeMillis()
    val res: T = t
    val stop = System.currentTimeMillis()
    println("Operation took " + (stop-start) + " milliseconds.")
    res
  }
}