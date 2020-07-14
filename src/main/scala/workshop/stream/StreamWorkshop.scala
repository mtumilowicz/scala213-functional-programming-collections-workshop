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
    foldRight(StreamWorkshop.empty[B])((newElem, flat) => f(newElem) append flat)
  }

  def mapUnfold[B](f: A => B): StreamWorkshop[B] = {
    StreamWorkshop.unfold(this) {
      case ConsStreamWorkshop(h, t) => Some((f(h()), t()))
      case _ => None
    }
  }

  def startsWith[B >: A](s: StreamWorkshop[B]): Boolean = {
    @scala.annotation.tailrec
    def loop(list: StreamWorkshop[A], sequence: StreamWorkshop[B] = s): Boolean = {
      sequence match {
        case EmptyStreamWorkshop => true
        case ConsStreamWorkshop(headS, tailS) => list match {
          case EmptyStreamWorkshop => false
          case ConsStreamWorkshop(head, tail) => if (head() == headS()) loop(tail(), tailS()) else false
        }
      }
    }

    loop(this)
  }

  def tails: StreamWorkshop[StreamWorkshop[A]] = {
    StreamWorkshop.unfold(this) {
      case EmptyStreamWorkshop => Option.empty
      case ConsStreamWorkshop(h, t) => Option.apply((ConsStreamWorkshop(h, t), t()))
    } append StreamWorkshop.cons(StreamWorkshop.empty, StreamWorkshop.empty)
  }

  def scanRight[B](z: B)(f: (A, => B) => B): StreamWorkshop[B] = {
    foldRight((z, StreamWorkshop.cons(z, StreamWorkshop.empty)))((a, p0) => {
      val b2 = f(a, p0._1)
      (b2, StreamWorkshop.cons(b2, p0._2))
    })._2
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
    StreamWorkshop.cons(a, constant(a))
  }

  def from(n: Int): StreamWorkshop[Int] = {
    StreamWorkshop.cons(n, from(n + 1))
  }

  def fibs(): StreamWorkshop[Int] = {
    def fib(prev: Int, next: Int, stream: => StreamWorkshop[Int] = StreamWorkshop.empty): StreamWorkshop[Int] = {
      StreamWorkshop.cons(prev, fib(next, prev + next, stream))
    }

    fib(0, 1)
  }

  def unfold[A, S](z: S)(f: S => Option[(A, S)]): StreamWorkshop[A] = {
    f(z) match {
      case Some((a, s)) => StreamWorkshop.cons(a, unfold(s)(f))
      case None => EmptyStreamWorkshop
    }

  }

  def fibUnfold(): StreamWorkshop[Int] = {
    unfold((0, 1)) { case (prev, next) => Some(prev, (next, prev + next)) }
  }

}