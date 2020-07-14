package answers.list

import scala.math.Numeric

sealed trait ListAnswer[+A] {

  def tail(): ListAnswer[A] = {
    this match {
      case NilAnswer => NilAnswer
      case ConsAnswer(_, tail) => tail
    }
  }

  def replaceHead[B >: A](head: B): ListAnswer[B] = {
    this match {
      case NilAnswer => NilAnswer
      case ConsAnswer(_, tail) => ConsAnswer(head, tail)
    }
  }

  def drop(n: Int): ListAnswer[A] = {
    @scala.annotation.tailrec
    def drop(l: ListAnswer[A], counter: Int = n): ListAnswer[A] = {
      if (counter <= 0) l
      else
        l match {
          case NilAnswer => NilAnswer
          case ConsAnswer(_, tail) => drop(tail, counter - 1)
        }
    }

    drop(this)
  }

  def dropWhile(f: A => Boolean): ListAnswer[A] = {
    @scala.annotation.tailrec
    def loop(l: ListAnswer[A]): ListAnswer[A] = {
      l match {
        case ConsAnswer(head, tail) if f(head) => loop(tail)
        case _ => l
      }
    }

    loop(this)
  }

  def removeLast(): ListAnswer[A] = {
    @scala.annotation.tailrec
    def loop(prev: ListAnswer[A], next: ListAnswer[A]): ListAnswer[A] = {
      next match {
        case NilAnswer => prev
        case ConsAnswer(_, NilAnswer) => prev
        case ConsAnswer(head, tail) => loop(ConsAnswer(head, prev), tail)
      }
    }

    loop(NilAnswer, this).reverse()
  }

  def exists(p: A => Boolean): Boolean =
    this match {
      case NilAnswer => false
      case ConsAnswer(head, tail) => p(head) || tail.exists(p) // Because the || operator evaluates its second argument lazily
    }

  def zipWith[B, C](second: ListAnswer[B])(f: ((A, B)) => C): ListAnswer[C] = {
    @scala.annotation.tailrec
    def zip(first: ListAnswer[A], second: ListAnswer[B], zipped: ListAnswer[(A, B)] = ListAnswer()): ListAnswer[(A, B)] = {
      first match {
        case NilAnswer => zipped
        case ConsAnswer(headF, tailF) => second match {
          case NilAnswer => zipped
          case ConsAnswer(headS, tailS) => zip(tailF, tailS, ConsAnswer((headF, headS), zipped))
        }
      }
    }

    zip(this, second).reverse().map(f)
  }

  def hasSubsequence[B >: A](sequence: ListAnswer[B]): Boolean = {
    @scala.annotation.tailrec
    def listLoop(list: ListAnswer[A], sequence: ListAnswer[B] = sequence): Boolean = {
      sequence match {
        case NilAnswer => true
        case ConsAnswer(headS, tailS) => list match {
          case NilAnswer => false
          case ConsAnswer(head, tail) => listLoop(tail, if (head == headS) tailS else sequence)
        }
      }
    }

    listLoop(this)
  }

  def splitAt(index: Int): (ListAnswer[A], ListAnswer[A]) = {
    @scala.annotation.tailrec
    def loop(current: ListAnswer[A], split: ListAnswer[A] = ListAnswer(), counter: Int = index): (ListAnswer[A], ListAnswer[A]) = {
      if (counter == 0) return (split, current)
      current match {
        case ConsAnswer(head, next) => loop(next, ConsAnswer(head, split), counter - 1)
        case NilAnswer => (split, ListAnswer())
      }
    }

    val (a, b) = loop(this)
    (a.reverse(), b)
  }

  def foldLeft[B](z: B)(f: (B, A) => B): B = {
    @scala.annotation.tailrec
    def loop(as: ListAnswer[A], z: B = z): B = {
      as match {
        case NilAnswer => z
        case ConsAnswer(head, tail) => loop(tail, f(z, head))
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

  def reverse(): ListAnswer[A] = {
    foldLeft(ListAnswer[A]())((reversed, head) => ConsAnswer(head, reversed))
  }

  def foldRight[B](z: B)(f: (A, B) => B): B = {
    this match {
      case NilAnswer => z
      case ConsAnswer(x, xs) => f(x, xs.foldRight(z)(f))
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

  def map[B](f: A => B): ListAnswer[B] = {
    foldRight(ListAnswer[B]())((elem, newList) => ConsAnswer(f(elem), newList))
  }

  def append[B >: A](elem: B): ListAnswer[B] = {
    foldRight(ListAnswer(elem))(ConsAnswer(_, _))
  }

  def concat[B >: A](second: ListAnswer[B]): ListAnswer[B] = {
    foldRight(second)(ConsAnswer(_, _))
  }

  def flatMap[B](f: A => ListAnswer[B]): ListAnswer[B] = {
    import ListAnswerUtils._
    this.map(f).flatten()
  }

  def filter(f: A => Boolean): ListAnswer[A] = {
    flatMap(a => if (f(a)) ListAnswer(a) else ListAnswer())
  }
}

case object NilAnswer extends ListAnswer[Nothing]

case class ConsAnswer[+A](head: A, override val tail: ListAnswer[A]) extends ListAnswer[A]

object ListAnswer {
  def apply[A](as: A*): ListAnswer[A] =
    if (as.isEmpty) NilAnswer
    else ConsAnswer(as.head, apply(as.tail: _*))

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

object ListAnswerUtils {

  implicit class NumericList[A: Numeric](val s: ListAnswer[A]) {
    def sum(): A = s.foldLeft(Numeric[A].zero)(Numeric[A].plus)
  }

  implicit class ListOfList[A](val s: ListAnswer[ListAnswer[A]]) {
    def flatten(): ListAnswer[A] = s.foldLeft(ListAnswer[A]())((reduced, a) => reduced.concat(a))
  }

}