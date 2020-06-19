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
    * Any two values x and y can be compared for equality in Scala using the expression x == y
* foldRight
    def foldRight[A,B](as: List[A], z: B)(f: (A, B) => B): B =
        as match {
        case Nil => z
        case Cons(x, xs) => f(x, foldRight(xs, z)(f))
    }
    * One way of describing what foldRight does is that it replaces the constructors of the list, Nil 
    and Cons , with z and f , illustrated here:
        * Cons(1, Cons(2, Nil)) -> f (1, f (2, z ))
    ```
    Cons(1, Cons(2, Cons(3, Nil))).foldRight(0)(_ + _)
    1 + Cons(2, Cons(3, Nil)).foldRight(0)(_ + _)
    1 + (2 + Cons(3, Nil).foldRight(0)(_ + _)
    1 + (2 + (3 + Nil.foldRight(0)(_ + _)
    1 + (2 + (3 + (0)))
    6
    ```
* Underscore notation for anonymous functions
    * You can think of the underscore as a “blank” in the expression that needs
      to be “filled in.”
    * Multiple underscores mean multiple pa-
      rameters, not reuse of a single parameter repeatedly. The first underscore
      represents the first parameter, the second underscore the second parameter,
      the third underscore the third parameter, and so on
    * The anonymous function (x,y) => x + y can be written as _ + _ in situations where
      the types of x and y could be inferred by Scala
    * _ + _ - (x,y) => x + y
    * _.head - xs => xs.head
    * _ drop _ - (xs,n) => xs.drop(n)
* LISTS IN THE STANDARD LIBRARY
    * 1 :: 2 :: Nil
    * pattern matching case h :: t
* pattern matching
    * In general a match expression
      lets you select using arbitrary patterns
    *  case classes and pattern matching, twin constructs
    * Case classes are Scala’s way to allow pattern matching on objects without
      requiring a large amount of boilerplate
    * case modifier. Classes with such a modifier are called case
      classes. Using the modifier makes the Scala compiler add some syntactic
      conveniences to your class.
      ```
      abstract class Expr
      case class Var(parameters list) extends Expr
      case class Number(num: Double) extends Expr
      ```
        * First, it adds a factory method with the name of the class
            * Var("x") to construct a Var object // vs new Var("x")
        * all arguments in the parameter list
          of a case class implicitly get a val prefix, so they are maintained as fields
        * compiler adds “natural” implementations of methods toString ,
          hashCode , and equals
        * compiler adds a copy method to your class for making modified
          copies
          * The method works by using named and default parameters
          * You
            specify the changes you’d like to make by using named parameters. For any
            parameter you don’t specify, the value from the old object is used
        *  the biggest
          advantage of case classes is that they support pattern matching
    * selector match { alternatives }
    * An arrow symbol =>
      separates the pattern from the expressions
    * A constant pattern like "+" or 1 matches values that are equal to the
      constant with respect to ==
    * A variable pattern like e matches every value
        * variable then refers to that value in the right hand side of the case clause
    * The wildcard pattern ( _ ) also
      matches every value, but it does not introduce a variable name to refer to that
      value
    *  constructor pattern looks like UnOp("-", e)
    * Match expressions can be seen as a generalization of Java-style switch es
    * However, there are three differences to keep in mind: First, match is an
      expression in Scala (i.e., it always results in a value). Second, Scala’s alter-
      native expressions never “fall through” into the next case. Third, if none of
      the patterns match, an exception named MatchError is thrown
    * Kinds of patterns
        * Wildcard patterns
            * wildcard pattern ( _ ) matches any object whatsoever
            * used as a default, catch-all alternative
            * can also be used to ignore parts of an object that you do not care about
* Sealed classes
    * You can enlist the help of the Scala compiler in detecting missing com-
      binations of patterns in a match expression. To do this, the compiler needs
      to be able to tell which are the possible cases. In general, this is impossible
      in Scala because new case classes can be defined at any time and in arbitrary
      compilation units. For instance, nothing would prevent you from adding a
      fifth case class to the Expr class hierarchy in a different compilation unit
      from the one where the other four cases are defined.
    * The alternative is to make the superclass of your case classes sealed. A
      sealed class cannot have any new subclasses added except the ones in the
      same file
    *  If you match against case classes
      that inherit from a sealed class, the compiler will flag missing combinations
      of patterns with a warning message
    