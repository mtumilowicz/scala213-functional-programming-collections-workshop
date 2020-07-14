package answers.stream

sealed trait StreamAnswer[+A] {

  def toList: List[A] = {
    @scala.annotation.tailrec
    def loop(stream: StreamAnswer[A], list: List[A] = List()): List[A] = {
      stream match {
        case EmptyStreamAnswer => list.reverse
        case ConsStreamAnswer(h, t) => loop(t(), h() :: list)
      }
    }

    loop(this)
  }

  def drop(n: Int): StreamAnswer[A] = {
    @scala.annotation.tailrec
    def loop(stream: StreamAnswer[A], n: Int): StreamAnswer[A] = {
      if (n == 0) return stream
      stream match {
        case EmptyStreamAnswer => stream
        case ConsStreamAnswer(_, t) => loop(t(), n - 1)
      }
    }

    loop(this, n)
  }

  def foldRight[B](z: => B)(f: (A, => B) => B): B =
    this match {
      case ConsStreamAnswer(h, t) => f(h(), t().foldRight(z)(f))
      case _ => z
    }

  def exists(p: A => Boolean): Boolean =
    foldRight(false)((a, b) => p(a) || b)

  def take(n: Int): StreamAnswer[A] = {
    def listToStream: List[A] => StreamAnswer[A] =
      _.foldLeft(StreamAnswer.empty[A])((stream, elem) => StreamAnswer.cons(elem, stream))

    @scala.annotation.tailrec
    def loop(stream: StreamAnswer[A], n: Int, list: List[A] = List()): List[A] = {
      if (n == 0) return list
      stream match {
        case EmptyStreamAnswer => list
        case ConsStreamAnswer(h, t) => loop(t(), n - 1, h() :: list)
      }
    }

    listToStream(loop(this, n))
  }

  def takeWhile(p: A => Boolean): StreamAnswer[A] = {
    def listToStream: List[A] => StreamAnswer[A] =
      _.foldLeft(StreamAnswer.empty[A])((stream, elem) => StreamAnswer.cons(elem, stream))

    @scala.annotation.tailrec
    def loop(stream: StreamAnswer[A], taken: List[A] = List()): List[A] = {
      stream match {
        case ConsStreamAnswer(h, t) if p(h()) => loop(t(), h() :: taken)
        case _ => taken
      }
    }

    listToStream(loop(this))
  }

  def find(p: A => Boolean): Option[A] =
    filter(p).headOption

  def forAll(p: A => Boolean): Boolean = {
    foldRight(true)((newElem, forAll) => p(newElem) && forAll)
  }

  def takeWhileFoldRight(p: A => Boolean): StreamAnswer[A] = {
    foldRight(StreamAnswer.empty[A])((newElem, taken) => if (p(newElem))
      StreamAnswer.cons(newElem, taken)
    else StreamAnswer.empty)
  }

  def headOption: Option[A] = {
    foldRight(Option.empty[A])((newElem, _) => Some(newElem))
  }

  def map[B](f: A => B): StreamAnswer[B] = {
    /*
    this match {
      case EmptyStreamFp => EmptyStreamFp
      case ConsStreamFp(h, t) => StreamFp.cons(f(h()), t().map(f))
    }
     */

    /*

    Cons(1, Cons(2, Cons(3, Empty))).map(_ * 2)
    Cons(1, Cons(2, Cons(3, Empty))).foldRight(Empty)((next, mapped) => StreamFp.cons(next * 2, mapped))
    StreamFp.cons(2, Cons(2, Cons(3, Empty)).foldRight(Empty)(...))

     */
    foldRight(StreamAnswer.empty[B])((next, mapped) => StreamAnswer.cons(f(next), mapped))
  }

  def filter(p: A => Boolean): StreamAnswer[A] = {
    foldRight(StreamAnswer.empty[A])((h, t) =>
      if (p(h)) StreamAnswer.cons(h, t)
      else t)
  }

  def append[B >: A](s: => StreamAnswer[B]): StreamAnswer[B] = {
    foldRight(s)((newElem, appended) => StreamAnswer.cons(newElem, appended))
  }

  def flatMap[B](f: A => StreamAnswer[B]): StreamAnswer[B] = {
    foldRight(StreamAnswer.empty[B])((newElem, flat) => f(newElem) append flat)
  }

  def mapUnfold[B](f: A => B): StreamAnswer[B] = {
    StreamAnswer.unfold(this) {
      case ConsStreamAnswer(h, t) => Some((f(h()), t()))
      case _ => None
    }
  }

  def startsWith[B >: A](s: StreamAnswer[B]): Boolean = {
    @scala.annotation.tailrec
    def loop(list: StreamAnswer[A], sequence: StreamAnswer[B] = s): Boolean = {
      sequence match {
        case EmptyStreamAnswer => true
        case ConsStreamAnswer(headS, tailS) => list match {
          case EmptyStreamAnswer => false
          case ConsStreamAnswer(head, tail) => if (head() == headS()) loop(tail(), tailS()) else false
        }
      }
    }

    loop(this)
  }

  def tails: StreamAnswer[StreamAnswer[A]] = {
    StreamAnswer.unfold(this) {
      case EmptyStreamAnswer => Option.empty
      case ConsStreamAnswer(h, t) => Some((ConsStreamAnswer(h, t), t()))
    } append StreamAnswer.cons(StreamAnswer.empty, StreamAnswer.empty)
  }

  // (1,2,3).scanRight(0)(_ + _) = List(1+2+3+0, 2+3+0, 3+0, 0)
  def scanRight[B](z: B)(f: (A, => B) => B): StreamAnswer[B] = {
    tails.map(_.foldRight(z)(f))
  }
}

case object EmptyStreamAnswer extends StreamAnswer[Nothing]

case class ConsStreamAnswer[+A](h: () => A, t: () => StreamAnswer[A]) extends StreamAnswer[A]

object StreamAnswer {
  def cons[A](hd: => A, tl: => StreamAnswer[A]): StreamAnswer[A] = {
    lazy val head = hd
    lazy val tail = tl
    ConsStreamAnswer(() => head, () => tail)
  }

  def empty[A]: StreamAnswer[A] = EmptyStreamAnswer

  def apply[A](as: A*): StreamAnswer[A] =
    if (as.isEmpty) empty else cons(as.head, apply(as.tail: _*))

  def constant[A](a: A): StreamAnswer[A] = {
    StreamAnswer.cons(a, constant(a))
  }

  def from(n: Int): StreamAnswer[Int] = {
    StreamAnswer.cons(n, from(n + 1))
  }

  def fibs(): StreamAnswer[Int] = {
    def fib(prev: Int, next: Int, stream: => StreamAnswer[Int] = StreamAnswer.empty): StreamAnswer[Int] = {
      StreamAnswer.cons(prev, fib(next, prev + next, stream))
    }

    fib(0, 1)
  }

  def unfold[A, S](z: S)(f: S => Option[(A, S)]): StreamAnswer[A] = {
    f(z) match {
      case Some((a, s)) => StreamAnswer.cons(a, unfold(s)(f))
      case None => EmptyStreamAnswer
    }

  }

  def fibUnfold(): StreamAnswer[Int] = {
    unfold((0, 1)) { case (prev, next) => Some(prev, (next, prev + next)) }
  }

}