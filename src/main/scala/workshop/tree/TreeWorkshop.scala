package workshop.tree

sealed trait TreeWorkshop[+A] {

  def size(): Int = {
    // hint: tailrec with loop(toVisit: List[TreeAnswer[A]], size: Int = 0)
    // every non-empty node may break up into left and right
    // just increment counter and loop over the list until empty
    0
  }

  def depth(): Int = {
    // hint: pattern matching with recur, max
    0
  }

  def map[B](f: A => B): TreeWorkshop[B] = {
    // hint: pattern match with recur
    null
  }

  // many strategies of traversing - pick the easiest one
  def fold[B](z: B)(f: (A, B) => B): B = {
    // hint: tailrec with loop(toVisit: List[TreeWorkshop[A]], accumulator: B = z)
    // same concept as in size()
    null.asInstanceOf
  }

  def sizeFold(): Int = {
    // hint: fold, _
    0
  }
}

case object EmptyTreeWorkshop extends TreeWorkshop[Nothing]

case class BranchWorkshop[A](value: A, left: TreeWorkshop[A], right: TreeWorkshop[A]) extends TreeWorkshop[A]

object BranchWorkshop {
  def apply[A](value: A, left: TreeWorkshop[A], right: TreeWorkshop[A]): BranchWorkshop[A] = {
    new BranchWorkshop(value, left, right)
  }

  def apply[A](value: A): BranchWorkshop[A] = {
    new BranchWorkshop(value, EmptyTreeWorkshop, EmptyTreeWorkshop)
  }

  def apply[A](value: A, treeFp: TreeWorkshop[A]): TreeWorkshop[A] = {
    treeFp match {
      case EmptyTreeWorkshop => EmptyTreeWorkshop
      case BranchWorkshop(_, left, right) => BranchWorkshop(value, left, right)
    }
  }

  def maxElem(tree: TreeWorkshop[Int]): Int = {
    // hint: tailrec with loop(tree: List[TreeWorkshop[Int]], maxElem: Int = Int.MinValue)
    // same approach as in fold
    0
  }

  def maxElemFold(tree: TreeWorkshop[Int]): Int = {
    // hint: fold, Int.MinValue, max
    0
  }
}