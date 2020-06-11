package answers.stream

sealed trait StreamFp[+A] {

  def headOption: Option[A] = this match {
    case EmptyStreamFp => None
    case ConsStreamFp(h, _) => Some(h())
  }

  def foldRight[B](z: => B)(f: (A, => B) => B): B =
    this match {
      case ConsStreamFp(h, t) => f(h(), t().foldRight(z)(f))
      case _ => z
    }

  def exists(p: A => Boolean): Boolean =
    foldRight(false)((a, b) => p(a) || b)

  def toList: List[A] = {
    @scala.annotation.tailrec
    def loop(stream: StreamFp[A], list: List[A] = List()): List[A] = {
      stream match {
        case EmptyStreamFp => list.reverse
        case ConsStreamFp(h, t) => loop(t(), h() :: list)
      }
    }

    loop(this)
  }

  def take(n: Int): StreamFp[A] = {
    def listToStream: List[A] => StreamFp[A] =
      _.foldLeft(StreamFp.empty[A])((stream, elem) => StreamFp.cons(elem, stream))

    @scala.annotation.tailrec
    def loop(stream: StreamFp[A], n: Int, list: List[A] = List()): List[A] = {
      if (n == 0) return list
      stream match {
        case EmptyStreamFp => list
        case ConsStreamFp(h, t) => loop(t(), n - 1, h() :: list)
      }
    }

    listToStream(loop(this, n))
  }

  def find(p: A => Boolean): Option[A] =
    filter(p).headOption

  def drop(n: Int): StreamFp[A] = {
    @scala.annotation.tailrec
    def loop(stream: StreamFp[A], n: Int): StreamFp[A] = {
      if (n == 0) return stream
      stream match {
        case EmptyStreamFp => stream
        case ConsStreamFp(_, t) => loop(t(), n - 1)
      }
    }

    loop(this, n)
  }

  def takeWhile(p: A => Boolean): StreamFp[A] = {
    def listToStream: List[A] => StreamFp[A] =
      _.foldLeft(StreamFp.empty[A])((elem, stream) => StreamFp.cons(stream, elem))

    @scala.annotation.tailrec
    def loop(stream: StreamFp[A], taken: List[A] = List()): StreamFp[A] = {
      stream match {
        case EmptyStreamFp => listToStream(taken)
        case ConsStreamFp(h, t) if p(h()) => loop(t(), h() :: taken)
        case ConsStreamFp(h, t) if !p(h()) => loop(t(), taken)
      }
    }

    loop(this)
  }

  def forAll(p: A => Boolean): Boolean = {
    foldRight(true)((newElem, forAll) => p(newElem) && forAll)
  }

  def takeWhileFold(p: A => Boolean): StreamFp[A] = {
    foldRight(StreamFp.empty[A])((newElem, taken) => if (p(newElem))
      StreamFp.cons(newElem, taken)
    else StreamFp.empty)
  }

  def headOptionFold: Option[A] = {
    foldRight(Option.empty[A])((newElem, _) => Some(newElem))
  }

  def map[B](f: A => B): StreamFp[B] = {
    foldRight(StreamFp.empty[B])((newElem, mapped) => StreamFp.cons(f(newElem), mapped))
  }

  def filter(p: A => Boolean): StreamFp[A] = {
    foldRight(StreamFp.empty[A])((newElem, filtered) => if (p(newElem))
      StreamFp.cons(newElem, filtered)
    else filtered)
  }

  def append[B >: A](s: => StreamFp[B]): StreamFp[B] = {
    foldRight(s)((newElem, appended) => StreamFp.cons(newElem, appended))
  }

  def flatMap[B](f: A => StreamFp[B]): StreamFp[B] = {
    foldRight(StreamFp.empty[B])((newElem, flat) => f(newElem) append flat)
  }

  def mapUnfold[B](f: A => B): StreamFp[B] = {
    StreamFp.unfold(this) {
      case ConsStreamFp(h, t) => Some((f(h()), t()))
      case _ => None
    }
  }

  def startsWith[B >: A](s: StreamFp[B]): Boolean = {
    @scala.annotation.tailrec
    def loop(list: StreamFp[A], sequence: StreamFp[B] = s): Boolean = {
      sequence match {
        case EmptyStreamFp => true
        case ConsStreamFp(headS, tailS) => list match {
          case EmptyStreamFp => false
          case ConsStreamFp(head, tail) => if (head() == headS()) loop(tail(), tailS()) else false
        }
      }
    }

    loop(this)
  }

  def tails: StreamFp[StreamFp[A]] = {
    StreamFp.unfold(this) {
      case EmptyStreamFp => Option.empty
      case ConsStreamFp(h, t) => Option.apply((ConsStreamFp(h, t), t()))
    } append StreamFp.cons(StreamFp.empty, StreamFp.empty)
  }

  def scanRight[B](z: B)(f: (A, => B) => B): StreamFp[B] = {
    foldRight((z, StreamFp.cons(z, StreamFp.empty)))((a, p0) => {
      val b2 = f(a, p0._1)
      (b2, StreamFp.cons(b2, p0._2))
    })._2
  }
}

case object EmptyStreamFp extends StreamFp[Nothing]

case class ConsStreamFp[+A](h: () => A, t: () => StreamFp[A]) extends StreamFp[A]

object StreamFp {
  def cons[A](hd: => A, tl: => StreamFp[A]): StreamFp[A] = {
    lazy val head = hd
    lazy val tail = tl
    ConsStreamFp(() => head, () => tail)
  }

  def empty[A]: StreamFp[A] = EmptyStreamFp

  def apply[A](as: A*): StreamFp[A] =
    if (as.isEmpty) empty else cons(as.head, apply(as.tail: _*))

  def constant[A](a: A): StreamFp[A] = {
    StreamFp.cons(a, constant(a))
  }

  def from(n: Int): StreamFp[Int] = {
    StreamFp.cons(n, from(n + 1))
  }

  def fibs(): StreamFp[Int] = {
    def fib(prev: Int, next: Int, stream: => StreamFp[Int] = StreamFp.empty): StreamFp[Int] = {
      StreamFp.cons(prev, fib(next, prev + next, stream))
    }

    fib(0, 1)
  }

  def unfold[A, S](z: S)(f: S => Option[(A, S)]): StreamFp[A] = {
    f(z) match {
      case Some((a, s)) => StreamFp.cons(a, unfold(s)(f))
      case None => EmptyStreamFp
    }

  }

  def fibUnfold(): StreamFp[Int] = {
    unfold((0, 1)) { case (prev, next) => Some(prev, (next, prev + next)) }
  }

}