1. list, stream, tree
1. Zipping and unzipping lists
1. fun getAt(index: Int)
    * fun getAtViaFoldLeft(index: Int): Result<A> =
1. short circuit foldLeft
1. splitAt
    * using fold
        * p. 213
1. tailrec fun startsWith(list: List<A>, sub: List<A>): Boolean =
   when (sub)
   * lepiej zacząc od tej drugiej
1. fun divide(depth: Int): List<List<A>>
1. mapping in parallel
    * fun <B> parMap(es: ExecutorService, g: (A) -> B): Result<List<B>> =
1. stream
    * take (proste), drop (ogonowo), takeAtMost
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