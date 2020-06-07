1. list, stream, tree
1. fun getAt(index: Int)
    * fun getAtViaFoldLeft(index: Int): Result<A> =
1. splitAt
    * using fold
        * p. 213
1. tree
    * contains
    * min max size; with / without memoization
    * Tree<A> remove(A a)
    * override fun merge(tree: Tree<Nothing>): Tree<Nothing> = tree
        * p 278
    * Write three functions to fold a tree: foldInOrder , foldPreOrder, and foldPostOrder
        * p 286
1. fold right cannot be tail recursive
    * https://stackoverflow.com/questions/4085118/why-foldright-and-reduceright-are-not-tail-recursive
        * https://stackoverflow.com/a/4086098