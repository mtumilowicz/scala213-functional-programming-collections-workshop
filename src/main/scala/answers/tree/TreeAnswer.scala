package answers.tree

sealed trait TreeAnswer[+A] {

  def size(): Int = {
    @scala.annotation.tailrec
    def loop(toVisit: List[TreeAnswer[A]], size: Int = 0): Int = {
      toVisit match {
        case BranchAnswer(_, left, right) :: tail => loop(left :: right :: tail, size + 1)
        case EmptyTreeAnswer :: tail => loop(tail, size)
        case Nil => size
      }
    }

    loop(List(this))
  }

  def depth(): Int = {
    this match {
      case EmptyTreeAnswer => 0
      case BranchAnswer(_, left, right) => 1 + (left.depth() max right.depth())
    }
  }

  def map[B](f: A => B): TreeAnswer[B] = {
    this match {
      case EmptyTreeAnswer => EmptyTreeAnswer
      case BranchAnswer(value, left, right) => BranchAnswer(f(value), left.map(f), right.map(f))
    }
  }

  def fold[B](z: B)(f: (A, B) => B): B = {
    @scala.annotation.tailrec
    def loop(toVisit: List[TreeAnswer[A]], accumulator: B = z): B = {
      toVisit match {
        case ::(EmptyTreeAnswer, next) => loop(next, accumulator)
        case ::(BranchAnswer(value, left, right), next) => loop(left :: right :: next, f(value, accumulator))
        case Nil => accumulator
      }
    }

    loop(List(this))
  }

  def sizeFold(): Int = {
    fold(0)((_, size) => size + 1)
  }
}

case object EmptyTreeAnswer extends TreeAnswer[Nothing]

case class BranchAnswer[A](value: A, left: TreeAnswer[A], right: TreeAnswer[A]) extends TreeAnswer[A]

object BranchAnswer {
  def apply[A](value: A, left: TreeAnswer[A], right: TreeAnswer[A]): BranchAnswer[A] = {
    new BranchAnswer(value, left, right)
  }

  def apply[A](value: A): BranchAnswer[A] = {
    new BranchAnswer(value, EmptyTreeAnswer, EmptyTreeAnswer)
  }

  def apply[A](value: A, treeFp: TreeAnswer[A]): TreeAnswer[A] = {
    treeFp match {
      case EmptyTreeAnswer => EmptyTreeAnswer
      case BranchAnswer(_, left, right) => BranchAnswer(value, left, right)
    }
  }

  def maxElem(tree: TreeAnswer[Int]): Int = {
    @scala.annotation.tailrec
    def loop(tree: List[TreeAnswer[Int]], maxElem: Int = Int.MinValue): Int = {
      tree match {
        case ::(EmptyTreeAnswer, next) => loop(next, maxElem)
        case ::(BranchAnswer(value, left, right), next) => loop(left :: right :: next, value max maxElem)
        case Nil => maxElem
      }
    }

    loop(List(tree))
  }

  def maxElemFold(tree: TreeAnswer[Int]): Int = {
    tree.fold(Int.MinValue)(_ max _)
  }
}