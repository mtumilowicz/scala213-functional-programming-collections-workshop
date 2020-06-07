1. list, stream, tree
1. flatten - przejść przez concat
1. list of eithers to list a, jesli failure to pomija
    { ra -> ra.map { List(it) }.getOrElse(List()) }
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
1. fun <B> groupBy(f: (A) -> B): Map<B, List<A>> =
1. fun exists(p:(A) -> Boolean): Boolean =
    * Because the || operator evaluates its second argument lazily
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
1. list
    * show how sum function is implemented
    