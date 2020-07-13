1. fold right cannot be tail recursive
    * https://stackoverflow.com/questions/4085118/why-foldright-and-reduceright-are-not-tail-recursive
        * https://stackoverflow.com/a/4086098
1. https://www.nurkiewicz.com/2012/04/secret-powers-of-foldleft-in-scala.html
1. https://blog.codecentric.de/en/2016/02/lazy-vals-scala-look-hood/
1. https://stackoverflow.com/questions/9809313/scalas-lazy-arguments-how-do-they-work
    * https://stackoverflow.com/a/9809731
1. https://booksites.artima.com/programming_in_scala_4ed
* https://medium.com/@wiemzin/variances-in-scala-9c7d17af9dc4
* https://docs.scala-lang.org/overviews/scala-book/classes.html
* https://dzone.com/articles/scala-generics-part-2-covariance-and-contravariance-in-generics

# introduction to scala
## variance
* please refer: https://github.com/mtumilowicz/java11-covariance-contravariance-invariance
* variance annotations: `+` and `-` symbols you can place next to type parameters
    * covariant: `trait Queue[+T] { ... }`
    * nonvariant: `trait Queue[T] { ... }`
    * contravariant: `trait Queue[-T] { ... }`
* to verify correctness Scala compiler classifies all positions in a class or trait body 
as positive, negative or neutral
    * "position" is any location in the class or trait where a type parameter may be used
    * for example, every method value parameter
    * type parameters annotated with + may only be used in positive positions
    * type parameters annotated with - may only be used in negative positions
    * type parameter with no variance annotation may be used in any position
        * the only kind of type parameter that can be used in neutral positions of the class body
    * compiler checks that each type parameter is only used in positions that are classified 
    appropriately
* examples
    * covariant position
        ```
        class Pets[+A](val pets: ...) {
          def add(newPet: A): ...
        }
      
        error: covariant type A occurs in contravariant position in type A of value newPet
        ```
        why?
        ```
        val pets: Pets[Animal] = Pets[Cat](List(Cat)) // it is actually a Pets[Cat]
        pets.add(Dog) // accepts Animal or any subtype of Animal
        ```
    * contravariant position
        ```
        class Pets[-A](val pet:A) // contravariant type A occurs in covariant position in type => A of value pet
        ```
        why?
        ```
        Pets[Cat] = Pets[Animal](new Animal)
        pets.pet.meow() // pets.pet is not Cat — it is an Animal
        ```
    * function `S => T` is contravariant in the function argument and covariant in the result type
        * `val x1: Int => CharSequence = (x: AnyVal) => x.toString`

## varargs
* variadic function syntax: `def apply[A](as: A*): List[A] = ...`
    * `apply(Array(1,2,3))`
    * `apply(1,2,3)`
    * any application of an object to some arguments in parentheses will be transformed to an `apply` 
    method call
        * `f(x)` -> `f.apply(x)`
        * digression: `x(0) = "Hello"` -> `x.update(0, "Hello")`
* convert to varargs: `as.tail: _*`

## underscore notation for anonymous functions
* examples
    * `var f: List[String] => List[String] = _.tail`
    * `var f: (List[String], Int) => List[String] = _ drop _`
    * `var f: (Int, Int) => Int = _ + _`
    * `List(-11, -10, -5, 0, 5, 10).filter(_ > 0)`
* compiler must have enough information to infer missing parameter types
* you can think of the underscore as a "blank" in the expression that needs to be "filled in"
* multiple underscores mean multiple parameters
    
## pattern matching
* examples
    ```
    case class Person(name: String, age: Int)
    
    person match {
        case Person("Michal", 29) => println("Hi Michal!")
        case Person(name, 65) => println("Hi " + name + ", retired?")
        case Person(name, age) if age > 100 => println("Hi " + name + ", congratulations!")
        case Person(name, _) => println("Hi " + name + ", age is a state of mind!")
        case _ => println("rest")
    }
    ```
* fancy switch statement that may descend into the structure of the expression it examines 
and extract subexpressions of that structure
    * there are three differences to keep in mind
        * match is an expression in Scala (i.e., it always results in a value)
        * Scala’s alternative expressions never "fall through" into the next case
        * if none of the patterns match, an exception named `MatchError` is thrown
* lets you select using arbitrary patterns

## case classes
* twin constructs: case classes and pattern matching
* case classes are Scala’s way to allow pattern matching on objects without requiring 
a large amount of boilerplate
* case modifier
    ```
    abstract class Animal
    case class Cat(parameters list) extends Animal
    case class Dog() extends Animal
    ```
    * classes with such a modifier are called case classes
    * using the modifier makes the Scala compiler add some syntactic conveniences 
    to your class
        * adds a factory method with the name of the class
        * construct an object: `Cat("x")` vs `new Cat("x")`
        * all arguments in the parameter list get a val prefix, so they are maintained 
        as fields
        * implementations of methods toString, hashCode and equals
        * copy method for making modified copies
            * The method works by using named and default parameters
            * You specify the changes you’d like to make by using named parameters. For any
            parameter you don’t specify, the value from the old object is used
    * the biggest advantage of case classes is that they support pattern matching

## sealed classes
* Scala compiler help in detecting missing combinations of patterns in a match expression
    * compiler needs to be able to tell which are the possible cases
    * in general, this is impossible because new case classes can be defined at any time 
    and in arbitrary compilation units
* alternative is to make the superclass of your case classes sealed
    * sealed class cannot have any new subclasses added except the ones in the same file
* if you match against case classes that inherit from a sealed class, the compiler will 
flag missing combinations of patterns with a warning message

# basics
## class
* public is default access level
* defined a ChecksumAccumulator class and gave it a var field named sum :
  class ChecksumAccumulator {
    private var sum = 0
  }
* in the absence of any explicit return statement - method returns the last computed value
* `class MyClass(index: Int, name: String)`
    * compiler will produce a class
        * two private instance variables
        * two args constructor
* `class MyClass(val index: Int, val name: String)`
    * readonly fields with getters
    * `println(p.firstName + " " + p.lastName)`
* main/primary constructor is defined when you define your class
    ```
    class Person(var firstName: String, var lastName: String) {
    
        println("the constructor begins")
    
        // some methods
        def printHome(): Unit = println(s"HOME = $HOME")    
        def printFullName(): Unit = println(this) 
    
        // A secondary constructor.
          def this(firstName: String) {
            this(firstName, "", 0);
          }
      
        printHome()
        printFullName()
        println("you've reached the end of the constructor")
    }
    ```
## singleton
* classes in Scala cannot have static members
* instead - singleton objects
* singleton object with the same name as a class - it is called class’s companion object
    * class is called the companion class of the singleton object
* class and its companion object can access each other’s private members
* for Java programmers - think of singleton objects as the home for static methods 
* A singleton object is more than a holder of static methods, however. It is a
  first-class object. You can think of a singleton object’s name, therefore, as a
  “name tag” attached to the object
* One difference between classes and singleton objects is that singleton
  objects cannot take parameters, whereas classes can
* have the same initialization semantics as Java statics
* singleton object is initialized the first time some code accesses it
## non-strictness
* To say a function is non-strict just means that the function may choose not to evaluate its arguments
* Boolean functions && and || are non-strict
* if you invoke `standard_method(sys.error("failure"))` you’ll get an exception - `sys.error("failure")` 
will be evaluated before entering the body of the method
* The arguments we’d like to pass unevaluated have an arrow => immediately before
  their type. In the body of the function, we don’t need to do anything special to evalu-
  ate an argument annotated with => . We just reference the identifier as usual
  * Nor do
    we have to do anything special to call this function. We just use the normal function
    call syntax, and Scala takes care of wrapping the expression in a thunk for us
* Scala won’t (by default) cache the result of evaluating an argument
## lazy evaluation
* Adding the lazy keyword to a val declaration will cause Scala to delay evaluation of
  the right-hand side of that lazy val declaration until it’s first referenced. It will also
  cache the result so that subsequent references to it don’t trigger repeated evaluation
* Formal definition of strictness
  * If the evaluation of an expression runs forever or throws an error instead of returning
  a definite value, we say that the expression doesn’t terminate, or that it evaluates to
  bottom. A function f is strict if the expression f(x) evaluates to bottom for all x that
  evaluate to bottom.
* As a final bit of terminology, we say that a non-strict function in Scala takes its argu-
  ments by name rather than by value
* lazy val initialization scheme uses double-checked locking to initialize the lazy val only once

# list
* immutable data structure
    * how we modify them?
        * for example: when we add an element to the front of an existing list `xs`
        we return a new list `(1,xs)`
        * we don’t need to actually copy xs - we can just reuse it
            * it is called data sharing
* functional data structures are persistent - existing references are never changed by 
operations on the data structure
```
sealed trait List[+A] // data type
case object Nil extends List[Nothing] // represents the empty lis
case class Cons[+A](head: A, tail: List[A]) extends List[A] // represents nonempty lists
```
* `Cons`
    * traditionally short for construct
    * nonempty list consists of an initial element - head followed by a List (tail) - possibly 
    empty 
* foldRight
    ```
    def foldRight[A,B](as: List[A], z: B)(f: (A, B) => B): B =
        as match {
        case Nil => z
        case Cons(x, xs) => f(x, foldRight(xs, z)(f))
    }
    ```
    * it replaces Nil and Cons with z and f
        * `Cons(1, Cons(2, Nil)) -> f (1, f (2, z ))`
    * example
        ```
        Cons(1, Cons(2, Cons(3, Nil))).foldRight(0)(_ + _)
        1 + Cons(2, Cons(3, Nil)).foldRight(0)(_ + _)
        1 + (2 + Cons(3, Nil).foldRight(0)(_ + _)
        1 + (2 + (3 + Nil.foldRight(0)(_ + _)
        1 + (2 + (3 + (0)))
        6
        ```
## standard library
* `1 :: 2 :: Nil` - `List(1,2)`
* pattern matching
    * `case h :: t` - split into head and tail

# stream
```
sealed trait Stream[+A]
case object Empty extends Stream[Nothing]
case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A] // head and a tail are both non-strict

object Stream {
    def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = { // cache the head and tail as lazy values to avoid repeated evaluation
        lazy val head = hd
        lazy val tail = tl
        Cons(() => head, () => tail)
    }

    def empty[A]: Stream[A] = Empty
    def apply[A](as: A*): Stream[A] = if (as.isEmpty) empty else cons(as.head, apply(as.tail: _*))
}
```
* type looks identical to our List type, except that the Cons data constructor takes
  explicit thunks ( () => A and () => Stream[A] ) instead of regular strict values
* generator
```
def apply[A](as: A*): Stream[A] =
if (as.isEmpty) empty
else cons(as.head, apply(as.tail: _*))
```
* Again, Scala takes care of wrapping the arguments to cons in thunks, so the as.head
  and apply(as.tail: _*) expressions won’t be evaluated until we force the Stream .
* fold right
```
def foldRight[B](z: => B)(f: (A, => B) => B): B =
this match {
case Cons(h,t) => f(h(), t().foldRight(z)(f))
case _ => z
}
```
* note how our combining function f is non-strict in its second parameter
    * If f chooses not to evaluate its second parameter, this terminates the traversal early
```
def exists(p: A => Boolean): Boolean =
foldRight(false)((a, b) => p(a) || b
```
* If p(a) returns true , b will never be evaluated and the computation terminates early
* implementations are incremental—they don’t fully generate their answers. 
    * It’s not until some other computation looks at the elements of the resulting Stream that the 
    computation to generate that Stream actually takes place—and then it
    will do just enough work to generate the requested elements. 
    * Because of this incremental nature, we can call these functions one after another without 
    fully instantiating the intermediate results.
* we can reuse filter to define find , a method to return just the first element that matches 
if it exists
    * Even though filter transforms the whole stream, that transformation is done lazily, so find 
    terminates as soon as a match is found:
    ```
    def find(p: A => Boolean): Option[A] = filter(p).headOption
    ```
    * Stream.filter tries to find the first matching value and if there is none, it will search forever
        * s.filter(_ => false) will never terminates on infinite stream
        * LazyList - OK
* The unfold function is an example of what’s sometimes called a corecursive func-
  tion. Whereas a recursive function consumes data, a corecursive function produces
  data
## standard library
* `LazyList`
    ```
    list match {
      case LazyList.empty => 1
      case h #:: t => h
    }
    ```
# trees
```
sealed trait Tree[+A]
case object Empty extends Tree[Nothing]
case class Branch[A](value: A, left: Tree[A], right: Tree[A]) extends Tree[A]
```