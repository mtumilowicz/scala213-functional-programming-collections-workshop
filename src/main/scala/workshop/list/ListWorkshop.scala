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
    // hint: tailrec with loop(prev: ListWorkshop[A], next: ListWorkshop[A])
    // hint: pattern matching: nil, (_, nil), (head, tail)
    // hint: reverse
    null
  }

  def exists(p: A => Boolean): Boolean =
  // hint: pattern matching with ||
    false

  // (1, 2, 3) zipWith (a, b, c) (x, y) => x + y = (1a, 2b, 3c)
  def zipWith[B, C](second: ListWorkshop[B])(f: ((A, B)) => C): ListWorkshop[C] = {
    // hint: tailrec with zip(first: ListWorkshop[A], second: ListWorkshop[B], zipped: ListWorkshop[(A, B)] = ListWorkshop())
    // hint: pattern matching on first
    // hint: inner pattern matching on second
    // hint: reverse and map
    null
  }

  // (1, 2, 3) hasSubsequence Nil = true, (2) = true, (2, 3) = true, (1, 3) = false
  def hasSubsequence[B >: A](sequence: ListWorkshop[B]): Boolean = {
    // hint: tailrec with loop(list: ListWorkshop[A], sequence: ListWorkshop[B] = sequence)
    // hint: pattern matching on sequence
    // hint: inner pattern matching on list
    false
  }

  // split at two parts: up to index, starting from index
  def splitAt(index: Int): (ListWorkshop[A], ListWorkshop[A]) = {
    // hint: tailrec with loop(current: ListWorkshop[A], split: ListWorkshop[A] = ListWorkshop(), counter: Int = index)
    // hint: reverse first part
    null
  }

  // same as foldRight but combines from left side
  def foldLeft[B](z: B)(f: (B, A) => B): B = {
    // hint: tailrec with loop(as: ListWorkshop[A], z: B = z)
    // hint: pattern matching
    null.asInstanceOf
  }

  def groupBy[B](f: A => B): Map[B, List[A]] = {
    // hint: define auxiliary function aggregate(map: Map[B, List[A]], a: A)
    // hint: updatedWith, f, map, a :: _
    // hint: foldLeft, Map, aggregate
    null
  }

  def length(): Int = {
    // hint: foldLeft, 0, increment
    0
  }

  def reverse(): ListWorkshop[A] = {
    // hint: foldLeft
    null
  }

  // combine from the right side
  def foldRight[B](z: B)(f: (A, B) => B): B = {
    // hint: pattern matching
    // hint: f(..., recur call)
    null.asInstanceOf
  }

  // note that foldRight = foldLeft on reversed
  def foldLeftByFoldRight[B](z: B)(f: (B, A) => B): B = {
    // hint: foldRight((b: B) => b)((a, delayFunction) => ...)
    null.asInstanceOf

    // 1, 2, 3 - we would like to process element one by one in reverse order
    // d1 = delayFunction(f(b, 1))
    // d2 = d1(f(b, 2))
    // d3 = d2(f(b, 3))
    // d4 = d3(Nil)
    // d4 = d3(Nil) = d2(f(Nil, 3)) = d1(f(f(Nil, 3)), 2) = delayFunction(f(f(f(Nil, 3)), 2), 1))
  }

  def map[B](f: A => B): ListWorkshop[B] = {
    // hint: foldRight
    null
  }

  // adds to the end
  def append[B >: A](elem: B): ListWorkshop[B] = {
    // hint: foldRight
    null
  }

  def concat[B >: A](second: ListWorkshop[B]): ListWorkshop[B] = {
    // hint: foldRight(second)
    null
  }

  def flatMap[B](f: A => ListWorkshop[B]): ListWorkshop[B] = {
    import ListWorkshopUtils._
    // hint: map, flatten
    null
  }

  def filter(f: A => Boolean): ListWorkshop[A] = {
    // hint: flatMap
    null
  }
}

case object NilWorkshop extends ListWorkshop[Nothing]

case class ConsWorkshop[+A](head: A, override val tail: ListWorkshop[A]) extends ListWorkshop[A]

object ListWorkshop {

  def apply[A](as: A*): ListWorkshop[A] =
    // hint: as.isEmpty, as.head, as.tail, _*
    // hint: ConsWorkshop(..., apply(...))
  null

  // if any is empty -> empty, else Some(list of values)
  def sequence[A](a: List[Option[A]]): Option[List[A]] = {
    // hint: define auxiliary function prepend(pair: (List[A], A)): ((2, 3, 4), 1) = (1, 2, 3, 4)
    // hint: pair._1, pair._2
    // hint: define auxiliary function merge(elem: Option[A], list: Option[List[A]])
    // hint: list.zip, map
    // hint: foldRight
    null
  }

  // if for any a in list f(a) is empty -> empty, else some(list of values of f)
  def traverse[A, B](a: List[A])(f: A => Option[B]): Option[List[B]] = {
    // hint: tailrec with loop(a: List[A], traversed: List[B] = List())
    // hint: pattern matching on a, ::
    // hint: pattern matching on f
    // hint: reverse
    null
  }
}

object ListWorkshopUtils {

  // methods for ListWorkshop with numeric type
  implicit class NumericList[A: Numeric](val s: ListWorkshop[A]) {

    def sum(): A =
      // hint: foldLeft, Numeric[A].zero, Numeric[A].plus
      null.asInstanceOf
  }

  // methods for ListWorkshop with list type
  implicit class ListOfList[A](val s: ListWorkshop[ListWorkshop[A]]) {
    def flatten(): ListWorkshop[A] =
      // hint: foldLeft, concat
      null
  }

}