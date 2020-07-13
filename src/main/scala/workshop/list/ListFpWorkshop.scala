package workshop.list

import scala.math.Numeric

sealed trait ListFpWorkshop[+A] {

  def tail(): ListFpWorkshop[A] = {
    this match {
      case NilFpWorkshop => NilFpWorkshop
      case ConsFpWorkshop(_, tail) => tail
    }
  }

  def replaceHead[B >: A](head: B): ListFpWorkshop[B] = {
    this match {
      case NilFpWorkshop => NilFpWorkshop
      case ConsFpWorkshop(_, tail) => ConsFpWorkshop(head, tail)
    }
  }

  def drop(n: Int): ListFpWorkshop[A] = {
    @scala.annotation.tailrec
    def drop(l: ListFpWorkshop[A], counter: Int = n): ListFpWorkshop[A] = {
      if (counter <= 0) l
      else
        l match {
          case NilFpWorkshop => NilFpWorkshop
          case ConsFpWorkshop(_, tail) => drop(tail, counter - 1)
        }
    }

    drop(this)
  }

  def dropWhile(f: A => Boolean): ListFpWorkshop[A] = {
    @scala.annotation.tailrec
    def loop(l: ListFpWorkshop[A]): ListFpWorkshop[A] = {
      l match {
        case ConsFpWorkshop(head, tail) if f(head) => loop(tail)
        case _ => l
      }
    }

    loop(this)
  }

  def removeLast(): ListFpWorkshop[A] = {
    @scala.annotation.tailrec
    def loop(prev: ListFpWorkshop[A], next: ListFpWorkshop[A]): ListFpWorkshop[A] = {
      next match {
        case NilFpWorkshop => prev
        case ConsFpWorkshop(_, NilFpWorkshop) => prev
        case ConsFpWorkshop(head, tail) => loop(ConsFpWorkshop(head, prev), tail)
      }
    }

    loop(NilFpWorkshop, this).reverse()
  }

  def exists(p: A => Boolean): Boolean =
    this match {
      case NilFpWorkshop => false
      case ConsFpWorkshop(head, tail) => p(head) || tail.exists(p) // Because the || operator evaluates its second argument lazily
    }

  def zipWith[B, C](second: ListFpWorkshop[B])(f: ((A, B)) => C): ListFpWorkshop[C] = {
    @scala.annotation.tailrec
    def zip(first: ListFpWorkshop[A], second: ListFpWorkshop[B], zipped: ListFpWorkshop[(A, B)] = ListFpWorkshop()): ListFpWorkshop[(A, B)] = {
      first match {
        case NilFpWorkshop => zipped
        case ConsFpWorkshop(headF, tailF) => second match {
          case NilFpWorkshop => zipped
          case ConsFpWorkshop(headS, tailS) => zip(tailF, tailS, ConsFpWorkshop((headF, headS), zipped))
        }
      }
    }

    zip(this, second).reverse().map(f)
  }

  def hasSubsequence[B >: A](sequence: ListFpWorkshop[B]): Boolean = {
    @scala.annotation.tailrec
    def listLoop(list: ListFpWorkshop[A], sequence: ListFpWorkshop[B] = sequence): Boolean = {
      sequence match {
        case NilFpWorkshop => true
        case ConsFpWorkshop(headS, tailS) => list match {
          case NilFpWorkshop => false
          case ConsFpWorkshop(head, tail) => listLoop(tail, if (head == headS) tailS else sequence)
        }
      }
    }

    listLoop(this)
  }

  def splitAt(index: Int): (ListFpWorkshop[A], ListFpWorkshop[A]) = {
    @scala.annotation.tailrec
    def loop(current: ListFpWorkshop[A], split: ListFpWorkshop[A] = ListFpWorkshop(), counter: Int = index): (ListFpWorkshop[A], ListFpWorkshop[A]) = {
      if (counter == 0) return (split, current)
      current match {
        case ConsFpWorkshop(head, next) => loop(next, ConsFpWorkshop(head, split), counter - 1)
        case NilFpWorkshop => (split, ListFpWorkshop())
      }
    }

    val (a, b) = loop(this)
    (a.reverse(), b)
  }

  def foldLeft[B](z: B)(f: (B, A) => B): B = {
    @scala.annotation.tailrec
    def loop(as: ListFpWorkshop[A], z: B = z): B = {
      as match {
        case NilFpWorkshop => z
        case ConsFpWorkshop(head, tail) => loop(tail, f(z, head))
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

  def reverse(): ListFpWorkshop[A] = {
    foldLeft(ListFpWorkshop[A]())((reversed, head) => ConsFpWorkshop(head, reversed))
  }

  def foldRight[B](z: B)(f: (A, B) => B): B = {
    this match {
      case NilFpWorkshop => z
      case ConsFpWorkshop(x, xs) => f(x, xs.foldRight(z)(f))
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

  def map[B](f: A => B): ListFpWorkshop[B] = {
    foldRight(ListFpWorkshop[B]())((elem, newList) => ConsFpWorkshop(f(elem), newList))
  }

  def append[B >: A](elem: B): ListFpWorkshop[B] = {
    foldRight(ListFpWorkshop(elem))(ConsFpWorkshop(_, _))
  }

  def concat[B >: A](second: ListFpWorkshop[B]): ListFpWorkshop[B] = {
    foldRight(second)(ConsFpWorkshop(_, _))
  }

  def flatMap[B](f: A => ListFpWorkshop[B]): ListFpWorkshop[B] = {
    import ListFpWorkshopUtils._
    this.map(f).flatten()
  }

  def filter(f: A => Boolean): ListFpWorkshop[A] = {
    flatMap(a => if (f(a)) ListFpWorkshop(a) else ListFpWorkshop())
  }
}

case object NilFpWorkshop extends ListFpWorkshop[Nothing]

case class ConsFpWorkshop[+A](head: A, override val tail: ListFpWorkshop[A]) extends ListFpWorkshop[A]

object ListFpWorkshop {
  def apply[A](as: A*): ListFpWorkshop[A] =
    if (as.isEmpty) NilFpWorkshop
    else ConsFpWorkshop(as.head, apply(as.tail: _*))

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

object ListFpWorkshopUtils {

  implicit class NumericList[A: Numeric](val s: ListFpWorkshop[A]) {
    def sum(): A = s.foldLeft(Numeric[A].zero)(Numeric[A].plus)
  }

  implicit class ListOfList[A](val s: ListFpWorkshop[ListFpWorkshop[A]]) {
    def flatten(): ListFpWorkshop[A] = s.foldLeft(ListFpWorkshop[A]())((reduced, a) => reduced.concat(a))
  }

}