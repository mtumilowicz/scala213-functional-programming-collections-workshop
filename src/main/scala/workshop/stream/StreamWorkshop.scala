package workshop.stream

sealed trait StreamWorkshop[+A] {

  def toList: List[A] = {
    // hint: tailrec with loop(stream: StreamWorkshop[A], list: List[A] = List())
    // hint: pattern matching
    // hint: reverse
    null
  }

  def drop(n: Int): StreamWorkshop[A] = {
    // hint: same as ListWorkshop.drop
    null
  }

  def foldRight[B](z: => B)(f: (A, => B) => B): B =
    this match {
      case ConsStreamWorkshop(h, t) => f(h(), t().foldRight(z)(f))
      case _ => z
    }

  def exists(p: A => Boolean): Boolean =
  // hint: foldRight, ||
    false

  def take(n: Int): StreamWorkshop[A] = {
    // hint: auxiliary function: listToStream List[A] => StreamWorkshop[A], foldLeft
    // hint: tailrec with loop(stream: StreamWorkshop[A], list: List[A] = List(), n: Int = n)
    // hint: listToStream(loop(this))
    null
  }

  def takeWhile(p: A => Boolean): StreamWorkshop[A] = {
    // hint: auxiliary function
    // hint: tailrec with loop(stream: StreamWorkshop[A], taken: List[A] = List())
    // hint: listToStream(loop(this))
    null
  }

  def find(p: A => Boolean): Option[A] =
  // hint: filter, headOption
    null

  def forAll(p: A => Boolean): Boolean = {
    // hint: foldRight, &&
    false
  }

  // takeWhile using foldRight
  def takeWhileFoldRight(p: A => Boolean): StreamWorkshop[A] = {
    // hint: foldRight, if ... else empty
    null
  }

  def headOption: Option[A] = {
    // hint: foldRight, Option
    null
  }

  def map[B](f: A => B): StreamWorkshop[B] = {
    // hint: pattern matching + recur
    // or hint: foldRight
    null
  }

  def filter(p: A => Boolean): StreamWorkshop[A] = {
    // hint: foldRight, if ... else tail
    null
  }

  def append[B >: A](s: => StreamWorkshop[B]): StreamWorkshop[B] = {
    // hint: foldRight
    null
  }

  def flatMap[B](f: A => StreamWorkshop[B]): StreamWorkshop[B] = {
    // hint: foldRight, append
    null
  }

  // map using unfold
  def mapUnfold[B](f: A => B): StreamWorkshop[B] = {
    // hint: unfold + pattern matching
    null
  }

  def startsWith[B >: A](s: StreamWorkshop[B]): Boolean = {
    // hint: tailrec with loop(list: StreamWorkshop[A], sequence: StreamWorkshop[B] = s)
    // hint: pattern matching on sequence
    // hint: inner pattern matching on list
    false
  }

  // (1, 2, 3, 4).tails = ((1, 2, 3, 4), (2, 3, 4), (3, 4), (4))
  def tails: StreamWorkshop[StreamWorkshop[A]] = {
    // hint: unfold, pattern matching, pair: (stream, tail)
    // hint: append empty
    null
  }

  // (1,2,3).scanRight(0)(_ + _) = List(1+2+3+0, 2+3+0, 3+0, 0)
  def scanRight[B](z: B)(f: (A, => B) => B): StreamWorkshop[B] = {
    // hint: tails, foldRight
    null
  }
}

case object EmptyStreamWorkshop extends StreamWorkshop[Nothing]

case class ConsStreamWorkshop[+A](h: () => A, t: () => StreamWorkshop[A]) extends StreamWorkshop[A]

object StreamWorkshop {
  def cons[A](hd: => A, tl: => StreamWorkshop[A]): StreamWorkshop[A] = {
    lazy val head = hd
    lazy val tail = tl
    ConsStreamWorkshop(() => head, () => tail)
  }

  def empty[A]: StreamWorkshop[A] = EmptyStreamWorkshop

  def apply[A](as: A*): StreamWorkshop[A] =
    if (as.isEmpty) empty else cons(as.head, apply(as.tail: _*))

  def constant[A](a: A): StreamWorkshop[A] = {
    // hint: recur
    null
  }

  // 1, 2, 3, 4, ...
  def from(n: Int): StreamWorkshop[Int] = {
    // hint: recur
    null
  }

  // stream of fibonacci
  def fibs(): StreamWorkshop[Int] = {
    // hint: auxiliary function: fib(prev: Int, next: Int, stream: => StreamWorkshop[Int] = StreamWorkshop.empty)
    // hint: fib(0, 1)
    null
  }

  // general stream-building functio
  // initial state, and a function for producing next state and the next value in the generated stream
  def unfold[A, S](z: S)(f: S => Option[(A, S)]): StreamWorkshop[A] = {
    // hint: pattern matching on f
    // hint: recur
    null
  }

  def fibUnfold(): StreamWorkshop[Int] = {
    // hint: unfold
    null
  }

}