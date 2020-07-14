package workshop.tree

sealed trait TreeWorkshop[+A] {

  def size(): Int = {
    @scala.annotation.tailrec
    def loop(toVisit: List[TreeWorkshop[A]], size: Int = 0): Int = {
      toVisit match {
        case BranchWorkshop(_, left, right) :: tail => loop(left :: right :: tail, size + 1)
        case EmptyTreeWorkshop :: tail => loop(tail, size)
        case Nil => size
      }
    }

    loop(List(this))
  }

  def depth(): Int = {
    this match {
      case EmptyTreeWorkshop => 0
      case BranchWorkshop(_, left, right) => 1 + (left.depth() max right.depth())
    }
  }

  def map[B](f: A => B): TreeWorkshop[B] = {
    this match {
      case EmptyTreeWorkshop => EmptyTreeWorkshop
      case BranchWorkshop(value, left, right) => BranchWorkshop(f(value), left.map(f), right.map(f))
    }
  }

  def fold[B](z: B)(f: (A, B) => B): B = {
    @scala.annotation.tailrec
    def loop(toVisit: List[TreeWorkshop[A]], accumulator: B = z): B = {
      toVisit match {
        case ::(EmptyTreeWorkshop, next) => loop(next, accumulator)
        case ::(BranchWorkshop(value, left, right), next) => loop(left :: right :: next, f(value, accumulator))
        case Nil => accumulator
      }
    }

    loop(List(this))
  }

  def sizeFold(): Int = {
    fold(0)((_, size) => size + 1)
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

  def maxElemFold(tree: TreeWorkshop[Int]): Int = {
    tree.fold(Int.MinValue)(_ max _)
  }

  def maxElem(tree: TreeWorkshop[Int]): Int = {
    @scala.annotation.tailrec
    def loop(tree: List[TreeWorkshop[Int]], maxElem: Int = Int.MinValue): Int = {
      tree match {
        case ::(EmptyTreeWorkshop, next) => loop(next, maxElem)
        case ::(BranchWorkshop(value, left, right), next) => loop(left :: right :: next, value max maxElem)
        case Nil => maxElem
      }
    }

    loop(List(tree))
  }
}