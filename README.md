1. fold right cannot be tail recursive
    * https://stackoverflow.com/questions/4085118/why-foldright-and-reduceright-are-not-tail-recursive
        * https://stackoverflow.com/a/4086098
1. https://www.nurkiewicz.com/2012/04/secret-powers-of-foldleft-in-scala.html
1. https://blog.codecentric.de/en/2016/02/lazy-vals-scala-look-hood/
1. https://stackoverflow.com/questions/9809313/scalas-lazy-arguments-how-do-they-work
    * https://stackoverflow.com/a/9809731
1. https://booksites.artima.com/programming_in_scala_4ed
* https://medium.com/@wiemzin/variances-in-scala-9c7d17af9dc4
    
# list
```
sealed trait List[+A] // data type
case object Nil extends List[Nothing] // represents the empty lis
case class Cons[+A](head: A, tail: List[A]) extends List[A] // represents nonempty lists
```

* covariance: https://github.com/mtumilowicz/java11-covariance-contravariance-invariance
    * covariant: `trait Queue[+T] { ... }`
        ```
        class Queue[+T] {
        def enqueue(x: T) =
            ...
        }
        
        error: covariant type T occurs in contravariant position in type T of value x
        def enqueue(x: T) =
        ```
    * nonvariant: `trait Queue[T] { ... }`
    * contravariant: `trait Queue[-T] { ... }`
    * The + and - symbols you can place next to type parameters are called variance annotations
    * To verify correctness of variance annotations, the Scala compiler clas-
      sifies all positions in a class or trait body as positive, negative or neutral.
      A “position” is any location in the class or trait (but from now on we’ll just
      write “class”) body where a type parameter may be used
        * For example, every
          method value parameter is a position because a method value parameter has
          a type
    * Type parameters annotated with + may only be used in positive positions,
      while type parameters annotated with - may only be used in negative po-
      sitions
    * A type parameter with no variance annotation may be used in any
      position, and is, therefore, the only kind of type parameter that can be used
      in neutral positions of the class body
    * Once the classifications are computed, the compiler checks that each type
      parameter is only used in positions that are classified appropriately
      ```
      abstract class Cat[-T, +U] {
      ...
      }
      ```
      * In this
        case, T is only used in negative positions, and U is only used in positive
        positions. So class Cat is type correct.
    * To classify the positions, the compiler starts from the declaration of a
      type parameter and then moves inward through deeper nesting levels. Po-
      sitions at the top level of the declaring class are classified as positive. By
      default, positions at deeper nesting levels are classified the same as that at
      enclosing levels, but there are a handful of exceptions where the classifica-
      tion changes. Method value parameter positions are classified to the flipped
      classification relative to positions outside the method, where the flip of a pos-
      itive classification is negative, the flip of a negative classification is positive,
      and the flip of a neutral classification is still neutral.
    * function variance: val x1: Int => CharSequence = (x: AnyVal) => x.toString
* pattern matching
    * Pattern matching works a bit like a fancy switch statement that may descend into
      the structure of the expression it examines and extract subexpressions of that structure

* immutable data structure
    * When data is immutable, how do we write functions that, for example, add or remove
      elements from a list? The answer is simple. When we add an element 1 to the front of
      an existing list, say xs , we return a new list, in this case Cons(1,xs) . Since lists are
      immutable, we don’t need to actually copy xs ; we can just reuse it. This is called data
      sharing.
    * We say that functional data structures are
      persistent, meaning that existing references are never changed by operations on the
      data structure

* Cons - (traditionally short for construct)
    * A nonempty list consists of an initial element, head ,
      followed by a List (possibly empty) of remaining elements (the tail )

* useful tricks
    * Variadic function syntax: `def apply[A](as: A*): List[A] =`
    * convert to varargs: `as.tail: _*`
    * sealed class, case class
* 