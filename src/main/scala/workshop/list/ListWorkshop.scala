package workshop.list

import scala.math.Numeric

sealed trait ListWorkshop[+A] {

  // if empty -> empty; otherwise - all apart head
  def tail(): ListWorkshop[A] = {
    // pattern matching
    null
  }

  // if empty -> empty; otherwise - new list with replaced head
  def replaceHead[B >: A](head: B): ListWorkshop[B] = {
    // pattern matching, hint: use _
    null
  }

  // skip n first elements, if n >= length -> empty
  def drop(n: Int): ListWorkshop[A] = {
    // hint: tailrec with loop(l: ListWorkshop[A], counter: Int = n)
    // hint: pattern matching with _
    null
  }

  // skip until true, if list ends before false -> empty
  def dropWhile(f: A => Boolean): ListWorkshop[A] = {
    // hint: tailrec with loop(l: ListWorkshop[A])
    // hint: pattern matching with if
    null
  }

  def removeLast(): ListWorkshop[A] = {
    @scala.annotation.tailrec
    def loop(prev: ListWorkshop[A], next: ListWorkshop[A]): ListWorkshop[A] = {
      next match {
        case NilWorkshop => prev
        case ConsWorkshop(_, NilWorkshop) => prev
        case ConsWorkshop(head, tail) => loop(ConsWorkshop(head, prev), tail)
      }
    }

    loop(NilWorkshop, this).reverse()
  }

  def exists(p: A => Boolean): Boolean =
    this match {
      case NilWorkshop => false
      case ConsWorkshop(head, tail) => p(head) || tail.exists(p) // Because the || operator evaluates its second argument lazily
    }

  def zipWith[B, C](second: ListWorkshop[B])(f: ((A, B)) => C): ListWorkshop[C] = {
    @scala.annotation.tailrec
    def zip(first: ListWorkshop[A], second: ListWorkshop[B], zipped: ListWorkshop[(A, B)] = ListWorkshop()): ListWorkshop[(A, B)] = {
      first match {
        case NilWorkshop => zipped
        case ConsWorkshop(headF, tailF) => second match {
          case NilWorkshop => zipped
          case ConsWorkshop(headS, tailS) => zip(tailF, tailS, ConsWorkshop((headF, headS), zipped))
        }
      }
    }

    zip(this, second).reverse().map(f)
  }

  def hasSubsequence[B >: A](sequence: ListWorkshop[B]): Boolean = {
    @scala.annotation.tailrec
    def listLoop(list: ListWorkshop[A], sequence: ListWorkshop[B] = sequence): Boolean = {
      sequence match {
        case NilWorkshop => true
        case ConsWorkshop(headS, tailS) => list match {
          case NilWorkshop => false
          case ConsWorkshop(head, tail) => listLoop(tail, if (head == headS) tailS else sequence)
        }
      }
    }

    listLoop(this)
  }

  def splitAt(index: Int): (ListWorkshop[A], ListWorkshop[A]) = {
    @scala.annotation.tailrec
    def loop(current: ListWorkshop[A], split: ListWorkshop[A] = ListWorkshop(), counter: Int = index): (ListWorkshop[A], ListWorkshop[A]) = {
      if (counter == 0) return (split, current)
      current match {
        case ConsWorkshop(head, next) => loop(next, ConsWorkshop(head, split), counter - 1)
        case NilWorkshop => (split, ListWorkshop())
      }
    }

    val (a, b) = loop(this)
    (a.reverse(), b)
  }

  def foldLeft[B](z: B)(f: (B, A) => B): B = {
    @scala.annotation.tailrec
    def loop(as: ListWorkshop[A], z: B = z): B = {
      as match {
        case NilWorkshop => z
        case ConsWorkshop(head, tail) => loop(tail, f(z, head))
      }
    }

    loop(this)
  }

  def groupBy[B](f: A => B): Map[B, List[A]] = {
    def aggregate(map: Map[B, List[A]], a: A): Map[B, List[A]] =
      map.updatedWith(f(a))(_.map(a :: _))

    foldLeft(Map[B, List[A]]())(aggregate)
  }

  def length(): Int = {
    foldLeft(0)((acc, _) => acc + 1)
  }

  def reverse(): ListWorkshop[A] = {
    foldLeft(ListWorkshop[A]())((reversed, head) => ConsWorkshop(head, reversed))
  }

  def foldRight[B](z: B)(f: (A, B) => B): B = {
    this match {
      case NilWorkshop => z
      case ConsWorkshop(x, xs) => f(x, xs.foldRight(z)(f))
    }
  }

  // note that foldRight = foldLeft on reversed
  def foldLeftByFoldRight[B](z: B)(f: (B, A) => B): B = {
    foldRight((b: B) => b)((a, delayFunction) => b => delayFunction(f(b, a)))(z)

    // 1, 2, 3 - we would like to process element one by one in reverse order
    // d1 = delayFunction(f(b, 1))
    // d2 = d1(f(b, 2))
    // d3 = d2(f(b, 3))
    // d4 = d3(Nil)
    // d4 = d3(Nil) = d2(f(Nil, 3)) = d1(f(f(Nil, 3)), 2) = delayFunction(f(f(f(Nil, 3)), 2), 1))
  }

  def map[B](f: A => B): ListWorkshop[B] = {
    foldRight(ListWorkshop[B]())((elem, newList) => ConsWorkshop(f(elem), newList))
  }

  def append[B >: A](elem: B): ListWorkshop[B] = {
    foldRight(ListWorkshop(elem))(ConsWorkshop(_, _))
  }

  def concat[B >: A](second: ListWorkshop[B]): ListWorkshop[B] = {
    foldRight(second)(ConsWorkshop(_, _))
  }

  def flatMap[B](f: A => ListWorkshop[B]): ListWorkshop[B] = {
    import ListWorkshopUtils._
    this.map(f).flatten()
  }

  def filter(f: A => Boolean): ListWorkshop[A] = {
    flatMap(a => if (f(a)) ListWorkshop(a) else ListWorkshop())
  }
}

case object NilWorkshop extends ListWorkshop[Nothing]

case class ConsWorkshop[+A](head: A, override val tail: ListWorkshop[A]) extends ListWorkshop[A]

object ListWorkshop {
  def apply[A](as: A*): ListWorkshop[A] =
    if (as.isEmpty) NilWorkshop
    else ConsWorkshop(as.head, apply(as.tail: _*))

  def sequence[A](a: List[Option[A]]): Option[List[A]] = {

    def add(pair: (List[A], A)): List[A] = pair._2 :: pair._1

    def merge(elem: Option[A], list: Option[List[A]]): Option[List[A]] =
      list.zip(elem).map(add)

    a.foldRight(Option(List[A]()))(merge)
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

object ListWorkshopUtils {

  implicit class NumericList[A: Numeric](val s: ListWorkshop[A]) {
    def sum(): A = s.foldLeft(Numeric[A].zero)(Numeric[A].plus)
  }

  implicit class ListOfList[A](val s: ListWorkshop[ListWorkshop[A]]) {
    def flatten(): ListWorkshop[A] = s.foldLeft(ListWorkshop[A]())((reduced, a) => reduced.concat(a))
  }

}