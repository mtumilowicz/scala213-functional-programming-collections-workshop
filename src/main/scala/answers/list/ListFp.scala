package answers.list

sealed trait ListFp[+A] {

  def tail(): ListFp[A] = {
    this match {
      case NilFp => NilFp
      case ConsFp(_, tail) => tail
    }
  }

  def setHead[B >: A](head: B): ListFp[B] = {
    this match {
      case NilFp => NilFp
      case ConsFp(_, tail) => ConsFp(head, tail)
    }
  }

  def drop(n: Int): ListFp[A] = {
    @scala.annotation.tailrec
    def drop(l: ListFp[A], counter: Int = n): ListFp[A] = {
      if (counter <= 0) l
      else
        l match {
          case NilFp => NilFp
          case ConsFp(_, tail) => drop(tail, counter - 1)
        }
    }

    drop(this)
  }

  // EXERCISE 3.5
  def dropWhile(f: A => Boolean): ListFp[A] = {
    @scala.annotation.tailrec
    def loop(l: ListFp[A]): ListFp[A] = {
      l match {
        case ConsFp(head, tail) if f(head) => loop(tail)
        case _ => l
      }
    }

    loop(this)
  }

  def removeLast(): ListFp[A] = {
    @scala.annotation.tailrec
    def loop(prev: ListFp[A], next: ListFp[A]): ListFp[A] = {
      next match {
        case NilFp => prev
        case ConsFp(_, NilFp) => prev
        case ConsFp(head, tail) => loop(ConsFp(head, prev), tail)
      }
    }

    loop(NilFp, this)
  }

  def length(): Int = {
    foldLeft(0)((acc, _) => acc + 1)
  }

  def foldLeft[B](z: B)(f: (B, A) => B): B = {
    @scala.annotation.tailrec
    def loop(as: ListFp[A], z: B): B = {
      as match {
        case NilFp => z
        case ConsFp(head, tail) => loop(tail, f(z, head))
      }
    }

    loop(this, z)
  }

  def reverse(): ListFp[A] = {
    @scala.annotation.tailrec
    def go(list: ListFp[A], reversed: ListFp[A] = NilFp): ListFp[A] = {
      list match {
        case NilFp => reversed
        case ConsFp(head, tail) => go(tail, ConsFp(head, reversed))
      }
    }

    go(this)
  }

  def reverseFoldLeft(): ListFp[A] = {
    foldLeft(ListFp[A]())((reversed, head) => ConsFp(head, reversed))
  }

  def foldRight[B](z: B)(f: (A, B) => B): B = {
    def loop(current: ListFp[A], z: B = z): B =
      current match {
        case NilFp => z
        case ConsFp(x, xs) => f(x, loop(xs, z))
      }

    loop(this)
  }

  def foldLeftByFoldRightRev[B](z: B)(f: (A, B) => B): B = {
    reverse().foldRight(z)(f)
  }

  def foldLeftByFoldRight[B](z: B)(f: (B, A) => B): B = {
    foldRight((b: B) => b)((a, delayFunction) => b => delayFunction(f(b, a)))(z)

    // 1, 2, 3 - we would like to process element one by one in reverse order
    // d1 = delayFunction(f(b, 1))
    // d2 = d1(f(b, 2))
    // d3 = d2(f(b, 3))
    // d4 = d3(Nil)
    // d4 = d3(Nil) = d2(f(Nil, 3)) = d1(f(f(Nil, 3)), 2) = delayFunction(f(f(f(Nil, 3)), 2), 1))
  }

  def appendFoldRight[B >: A](elem: B): ListFp[B] = {
    foldRight(ListFp(elem))(ConsFp(_, _))
  }

  def appendFoldLeft[B >: A](elem: B): ListFp[B] = {
    foldLeft((b: B) => ListFp(b))((delayFunction, head) => a => ConsFp(head, delayFunction(a)))(elem)
  }

  def map[B](f: A => B): ListFp[B] = {
    @scala.annotation.tailrec
    def loop(list: ListFp[A], transformed: ListFp[B] = ListFp()): ListFp[B] = {
      list match {
        case NilFp => transformed
        case ConsFp(head, tail) => loop(tail, ConsFp(f(head), transformed))
      }
    }

    loop(this).reverse()
  }

  def filter(f: A => Boolean): ListFp[A] = {
    @scala.annotation.tailrec
    def loop(list: ListFp[A], filtered: ListFp[A] = ListFp()): ListFp[A] = {
      list match {
        case NilFp => filtered
        case ConsFp(head, tail) if f(head) => loop(tail, ConsFp(head, filtered))
        case ConsFp(head, tail) if !f(head) => loop(tail, filtered)
      }
    }

    loop(this).reverse()
  }

  def concat[B >: A](second: ListFp[B]): ListFp[B] = {
    this.foldRight(second)((prev, concatenated) => ConsFp(prev, concatenated))
  }

  def flatMap[B](f: A => ListFp[B]): ListFp[B] = {
    @scala.annotation.tailrec
    def loop(list: ListFp[A], transformed: ListFp[B] = ListFp()): ListFp[B] = {
      list match {
        case NilFp => transformed
        case ConsFp(head, tail) => loop(tail, f(head).concat(transformed))
      }
    }

    loop(this).reverse()
  }

  def filterByFlatMap(f: A => Boolean): ListFp[A] = {
    flatMap(a => if (f(a)) ListFp(a) else ListFp())
  }

  def zipWith[B, C](second: ListFp[B])(f: ((A, B)) => C): ListFp[C] = {
    @scala.annotation.tailrec
    def zip(first: ListFp[A], second: ListFp[B], zipped: ListFp[(A, B)] = ListFp()): ListFp[(A, B)] = {
      first match {
        case NilFp => zipped
        case ConsFp(headF, tailF) => second match {
          case NilFp => zipped
          case ConsFp(headS, tailS) => zip(tailF, tailS, ConsFp((headF, headS), zipped))
        }
      }
    }

    zip(this, second).reverse().map(f)
  }

  def hasSubsequence[B >: A](sequence: ListFp[B]): Boolean = {

    @scala.annotation.tailrec
    def listLoop(list: ListFp[A], sequence: ListFp[B] = sequence): Boolean = {
      list match {
        case NilFp => sequence == NilFp
        case ConsFp(head, tail) => sequence match {
          case NilFp => true
          case ConsFp(seqHead, seqTail) => if (head == seqHead)
            listLoop(tail, seqTail)
          else listLoop(tail, sequence)
        }
      }
    }

    listLoop(this)
  }
}

case object NilFp extends ListFp[Nothing]

case class ConsFp[+A](head: A, override val tail: ListFp[A]) extends ListFp[A]

object ListFp {
  def apply[A](as: A*): ListFp[A] =
    if (as.isEmpty) NilFp
    else ConsFp(as.head, apply(as.tail: _*))

  def flatten(listOfLists: ListFp[ListFp[Any]]): ListFp[Any] = {
    listOfLists.foldLeft(ListFp[Any]())((reduced, a) => reduced.concat(a))
  }
}