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

  def take(n: Int): StreamAnswer[A] = {

    @scala.annotation.tailrec
    def loop(stream: StreamAnswer[A], list: List[A] = List(), n: Int = n): List[A] = {
      if (n == 0) return list.reverse
      stream match {
        case EmptyStreamAnswer => list.reverse
        case ConsStreamAnswer(h, t) => loop(t(), h() :: list, n - 1)
      }
    }

    StreamAnswer(loop(this))
  }

  def foldRight[B](z: => B)(f: (A, => B) => B): B =
    this match {
      case ConsStreamAnswer(h, t) => f(h(), t().foldRight(z)(f))
      case _ => z
    }

  def takeWhile(p: A => Boolean): StreamAnswer[A] = {
    @scala.annotation.tailrec
    def loop(stream: StreamAnswer[A], taken: List[A] = List()): List[A] = {
      stream match {
        case ConsStreamAnswer(h, t) if p(h()) => loop(t(), h() :: taken)
        case _ => taken.reverse
      }
    }

    StreamAnswer(loop(this))
  }

  def takeWhileFoldRight(p: A => Boolean): StreamAnswer[A] = {
    foldRight(StreamAnswer.empty[A])((newElem, taken) => if (p(newElem))
      StreamAnswer.cons(newElem, taken)
    else StreamAnswer.empty)
  }

  def exists(p: A => Boolean): Boolean =
    foldRight(false)((a, b) => p(a) || b)

  def headOption: Option[A] = {
    foldRight(Option.empty[A])((newElem, _) => Some(newElem))
  }

  def filter(p: A => Boolean): StreamAnswer[A] = {
    foldRight(StreamAnswer.empty[A])((h, t) =>
      if (p(h)) StreamAnswer.cons(h, t)
      else t)
  }

  def find(p: A => Boolean): Option[A] =
    filter(p).headOption

  def forAll(p: A => Boolean): Boolean = {
    foldRight(true)((newElem, forAll) => p(newElem) && forAll)
  }

  def map[B](f: A => B): StreamAnswer[B] = {
    foldRight(StreamAnswer.empty[B])((next, mapped) => StreamAnswer.cons(f(next), mapped))
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
          case ConsStreamAnswer(head, tail) if head() == headS() => loop(tail(), tailS())
          case _ => false
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

  def apply[A](list: List[A]): StreamAnswer[A] =
    list.foldRight(StreamAnswer.empty[A])((elem, stream) => StreamAnswer.cons(elem, stream))

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