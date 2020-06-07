package answers.list

import scala.math.Numeric

sealed trait ListFp[+A] {

  def tail(): ListFp[A] = {
    this match {
      case NilFp => NilFp
      case ConsFp(_, tail) => tail
    }
  }

  def replaceHead[B >: A](head: B): ListFp[B] = {
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

    loop(NilFp, this).reverse()
  }

  def length(): Int = {
    foldLeft(0)((acc, _) => acc + 1)
  }

  def foldLeft[B](z: B)(f: (B, A) => B): B = {
    @scala.annotation.tailrec
    def loop(as: ListFp[A], z: B = z): B = {
      as match {
        case NilFp => z
        case ConsFp(head, tail) => loop(tail, f(z, head))
      }
    }

    loop(this)
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
    this match {
      case NilFp => z
      case ConsFp(x, xs) => f(x, xs.foldRight(z)(f))
    }
  }

  def exists(p: A => Boolean): Boolean =
    this match {
      case NilFp => false
      case ConsFp(head, tail) => p(head) || tail.exists(p) // Because the || operator evaluates its second argument lazily
    }

  def foldLeftByFoldRightRev[B](z: B)(f: (B, A) => B): B = {
    reverse().foldRight(z)((a, b) => f(b, a))
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
    import ListFpUtils._
    val value: ListFp[ListFp[B]] = this.map(f)
    value.flatten()
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
      sequence match {
        case NilFp => true
        case ConsFp(headS, tailS) => list match {
          case NilFp => false
          case ConsFp(head, tail) => listLoop(tail, if (head == headS) tailS else sequence)
        }
      }
    }

    listLoop(this)
  }

  def groupBy[B](f: A => B): Map[B, List[A]] = {
    def aggregate(map: Map[B, List[A]], a: A): Map[B, List[A]] =
      map.updatedWith(f(a))(_.map(a :: _))

    foldLeft(Map[B, List[A]]())(aggregate)
  }
}

case object NilFp extends ListFp[Nothing]

case class ConsFp[+A](head: A, override val tail: ListFp[A]) extends ListFp[A]

object ListFp {
  def apply[A](as: A*): ListFp[A] =
    if (as.isEmpty) NilFp
    else ConsFp(as.head, apply(as.tail: _*))

  def sequence[A](a: List[Option[A]]): Option[List[A]] = {
    @scala.annotation.tailrec
    def loop(a: List[Option[A]], reduced: List[A] = List()): Option[List[A]] = {
      a match {
        case ::(None, _) => None
        case ::(Some(v), next) => loop(next, v :: reduced)
        case Nil => Some(reduced)
      }
    }

    loop(a).map(_.reverse)
  }

  def traverse[A, B](a: List[A])(f: A => Option[B]): Option[List[B]] = {
    @scala.annotation.tailrec
    def loop(a: List[A], traversed: List[B] = List()): Option[List[B]] = {
      a match {
        case ::(head, next) => f(head) match {
          case Some(get) => loop(next, get :: traversed)
          case None => None
        }
        case Nil => Some(traversed)
      }
    }

    loop(a).map(_.reverse)
  }
}

object ListFpUtils {

  implicit class NumericList[A: Numeric](val s: ListFp[A]) {
    def sum(): A = s.foldLeft(Numeric[A].zero)(Numeric[A].plus)
  }

  implicit class ListOfList[A](val s: ListFp[ListFp[A]]) {
    def flatten(): ListFp[A] = s.foldLeft(ListFp[A]())((reduced, a) => reduced.concat(a))
  }

}