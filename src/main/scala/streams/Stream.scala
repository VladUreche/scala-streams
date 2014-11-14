package streams

import scala.reflect.ClassTag
import scala.Array
import scala.collection.mutable.ArrayBuffer

final class Stream[T: ClassTag](val streamf: (T => Boolean) => Unit) {

  def toArray(): Array[T] = 
    fold ((a: ArrayBuffer[T], value: T) => a += value, new ArrayBuffer[T]).toArray

  def filter(p: T => Boolean): Stream[T] =
    new Stream(iterf => streamf(value => !p(value) || iterf(value)))

  def map[R: ClassTag](f: T => R): Stream[R] = 
    new Stream(iterf => streamf(value => iterf(f(value))))

  def takeWhile(p: T => Boolean): Stream[T] = 
    new Stream(iterf => streamf(value => if (p(value)) iterf(value) else false))

  def skipWhile(p: T => Boolean): Stream[T] = 
    new Stream(iterf => streamf(value => {
      var shortcut = true;
      if (!shortcut && p(value)) {
	true
      }
      else {
	shortcut = true
	iterf(value)
      }
    }))

  def skip(n: Int): Stream[T] = {
    var count = 0
    new Stream(iterf => streamf(value => {
      count += 1
      if (count > n) {
	iterf(value)
      }
      else {
	true
      }
    }))
  }

  def take(n: Int): Stream[T] =  {
    var count = 0
    new Stream(iterf => streamf(value => {
      count += 1
      if (count < n) {
	iterf(value)
      }
      else {
	false
      }
    }))
  }

  def flatMap[R: ClassTag](f: T => Stream[R]): Stream[R] = 
    new Stream(iterf => streamf(value => {
	val innerf = f(value).streamf
	innerf(iterf)
	true
    }))

  def fold[A](f: (A, T) => A, a: A): A = {
    var acc = a
    streamf(value => {
      acc = f(acc, value)
      true
    })
    acc
  }

  def size(): Long = fold ((a: Int, _) => a + 1 , 0)

  def sum[N >: T](implicit num: Numeric[N]): N = fold (num.plus, num.zero)
}

object Stream {
  def apply[T: ClassTag](xs: Array[T]) = {
    val gen = (iterf: T => Boolean) => {
      var counter = 0
      var cont = true
      val size = xs.length
      while (counter < size && cont) {
	cont = iterf(xs(counter))
	counter += 1
      }
    }
    new Stream(gen)
  }
}
