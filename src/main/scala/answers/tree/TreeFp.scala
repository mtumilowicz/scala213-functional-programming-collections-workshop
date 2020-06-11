package answers.tree

sealed trait TreeFp[+A] {

  def size(): Int = {
    @scala.annotation.tailrec
    def go(toVisit: List[TreeFp[A]], size: Int = 0): Int = {
      toVisit match {
        case BranchFp(_, left, right) :: tail => go(left :: right :: tail, size + 1)
        case EmptyTree :: tail => go(tail, size)
        case Nil => size
      }
    }

    go(List(this))
  }

  def depth(): Int = {
    this match {
      case EmptyTree => 0
      case BranchFp(_, left, right) => 1 + (left.depth() max right.depth())
    }
  }

  def map[B](f: A => B): TreeFp[B] = {
    this match {
      case EmptyTree => EmptyTree
      case BranchFp(value, left, right) => BranchFp(f(value), left.map(f), right.map(f))
    }
  }

  def fold[B](z: B)(f: (A, B) => B): B = {
    @scala.annotation.tailrec
    def loop(toVisit: List[TreeFp[A]], accumulator: B = z): B = {
      toVisit match {
        case ::(EmptyTree, next) => loop(next, accumulator)
        case ::(BranchFp(value, left, right), next) => loop(left :: right :: next, f(value, accumulator))
        case Nil => accumulator
      }
    }

    loop(List(this))
  }

  def sizeFold(): Int = {
    fold(0)((_, size) => size + 1)
  }
}

case object EmptyTree extends TreeFp[Nothing]

case class BranchFp[A](value: A, left: TreeFp[A], right: TreeFp[A]) extends TreeFp[A]

object BranchFp {
  def apply[A](value: A, left: TreeFp[A], right: TreeFp[A]): BranchFp[A] = {
    new BranchFp(value, left, right)
  }

  def apply[A](value: A): BranchFp[A] = {
    new BranchFp(value, EmptyTree, EmptyTree)
  }

  def apply[A](value: A, treeFp: TreeFp[A]): TreeFp[A] = {
    treeFp match {
      case EmptyTree => EmptyTree
      case BranchFp(_, left, right) => BranchFp(value, left, right)
    }
  }

  def maxElemFold(tree: TreeFp[Int]): Int = {
    tree.fold(Int.MinValue)(_ max _)
  }

  def maxElem(tree: TreeFp[Int]): Int = {
    @scala.annotation.tailrec
    def loop(tree: List[TreeFp[Int]], maxElem: Int = Int.MinValue): Int = {
      tree match {
        case ::(EmptyTree, next) => loop(next, maxElem)
        case ::(BranchFp(value, left, right), next) => loop(left :: right :: next, value max maxElem)
        case Nil => maxElem
      }
    }

    loop(List(tree))
  }
}