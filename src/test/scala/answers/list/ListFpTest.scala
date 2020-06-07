package answers.list

import answers.list.ListFpUtils._

class ListFpTest extends org.scalatest.FunSuite with org.scalatest.matchers.should.Matchers {

  test("tail") {
    val empty = ListFp()
    val singleton = ListFp(1)
    val list = ListFp(1, 2, 3, 4)

    empty.tail() shouldBe NilFp
    singleton.tail() shouldBe NilFp
    list.tail() shouldBe ListFp(2, 3, 4)
  }

  test("replaceHead") {
    val empty = ListFp()
    val singleton = ListFp(1)
    val list = ListFp(1, 2, 3, 4)

    empty.replaceHead(1) shouldBe NilFp
    singleton.replaceHead(2) shouldBe ListFp(2)
    list.replaceHead(3) shouldBe ListFp(3, 2, 3, 4)
  }

  test("drop") {
    val empty = ListFp()
    val singleton = ListFp(1)
    val list = ListFp(1, 2, 3, 4)

    empty.drop(0) shouldBe ListFp()
    empty.drop(1) shouldBe ListFp()
    singleton.drop(0) shouldBe ListFp(1)
    singleton.drop(1) shouldBe ListFp()
    list.drop(1) shouldBe ListFp(2, 3, 4)
    list.drop(2) shouldBe ListFp(3, 4)
    list.drop(4) shouldBe ListFp()
  }

  test("dropWhile") {
    val empty = ListFp[Int]()
    val singleton = ListFp(1)
    val list = ListFp(1, 2, 3, 4)

    empty.dropWhile(_ < 1) shouldBe ListFp()
    singleton.dropWhile(_ < 1) shouldBe ListFp(1)
    singleton.dropWhile(_ == 1) shouldBe ListFp()
    list.dropWhile(_ <= 1) shouldBe ListFp(2, 3, 4)
    list.dropWhile(_ <= 3) shouldBe ListFp(4)
    list.dropWhile(_ <= 4) shouldBe ListFp()
  }

  test("removeLast") {
    val empty = ListFp()
    val singleton = ListFp(1)
    val twice = ListFp(1, 2)
    val list = ListFp(1, 2, 3, 4)

    empty.removeLast() shouldBe NilFp
    singleton.removeLast() shouldBe NilFp
    twice.removeLast() shouldBe ListFp(1)
    list.removeLast() shouldBe ListFp(1, 2, 3)
  }

  test("length") {
    val empty = ListFp()
    val singleton = ListFp(1)
    val twice = ListFp(1, 2)
    val list = ListFp(1, 2, 3, 4)

    empty.length() shouldBe 0
    singleton.length() shouldBe 1
    twice.length() shouldBe 2
    list.length() shouldBe 4
  }

  test("foldLeft") {
    val empty = ListFp()
    val singleton = ListFp(1)
    val twice = ListFp(1, 2)
    val list = ListFp(1, 2, 3, 4)

    empty.foldLeft("")(_ + _) shouldBe ""
    singleton.foldLeft("")(_ + _) shouldBe "1"
    twice.foldLeft("")(_ + _) shouldBe "12"
    list.foldLeft("")(_ + _) shouldBe "1234"
  }

  test("reverse") {
    val empty = ListFp()
    val singleton = ListFp(1)
    val twice = ListFp(1, 2)
    val list = ListFp(1, 2, 3, 4)

    empty.reverse() shouldBe ListFp()
    singleton.reverse() shouldBe ListFp(1)
    twice.reverse() shouldBe ListFp(2, 1)
    list.reverse() shouldBe ListFp(4, 3, 2, 1)
  }

  test("foldRight") {
    val empty = ListFp[Int]()
    val singleton = ListFp(1)
    val twice = ListFp(1, 2)
    val list = ListFp(1, 2, 3, 4)

    empty.foldRight("")(_ + _) shouldBe ""
    singleton.foldRight("")(_ + _) shouldBe "1"
    twice.foldRight("")(_ + _) shouldBe "12"
    list.foldRight("")(_ + _) shouldBe "1234"
  }

  test("foldLeftByFoldRight") {
    val empty = ListFp[String]()
    val singleton = ListFp(1)
    val twice = ListFp(1, 2)
    val list = ListFp(1, 2, 3, 4)

    empty.foldLeftByFoldRight("")(_ + _) shouldBe ""
    singleton.foldLeftByFoldRight("")(_ + _) shouldBe "1"
    twice.foldLeftByFoldRight("")(_ + _) shouldBe "12"
    list.foldLeftByFoldRight("")(_ + _) shouldBe "1234"
  }

  test("appendFoldRight") {
    val empty = ListFp[String]()
    val list = ListFp(1, 2, 3, 4)

    empty.append(1) shouldBe ListFp(1)
    list.append(5) shouldBe ListFp(1, 2, 3, 4, 5)
  }

  test("flatten") {
    val l1 = ListFp[String]()
    val l2 = ListFp(1)
    val l3 = ListFp(2, 3)
    val l4 = ListFp(4, 5, 6)
    val listOfLists = ListFp(l1, l2, l3, l4)

    listOfLists.flatten() shouldBe ListFp(1, 2, 3, 4, 5, 6)
  }

  test("sum") {
    val l1 = ListFp[Int]()
    val l2 = ListFp[Double](1)
    val l3 = ListFp[BigDecimal](2, 3)
    val l4 = ListFp[Float](4, 5, 6)

    l1.sum() shouldBe 0
    l2.sum() shouldBe 1
    l3.sum() shouldBe 5
    l4.sum() shouldBe 15
  }

  test("map") {
    val empty = ListFp[String]()
    val list = ListFp(1, 2, 3, 4)

    empty.map(_ + "1") shouldBe ListFp()
    list.map(_.toString) shouldBe ListFp("1", "2", "3", "4")
  }

  test("filter") {
    val empty = ListFp[String]()
    val list = ListFp(1, 2, 3, 4)

    empty.filter(_.length == 0) shouldBe ListFp()
    list.filter(_ <= 3) shouldBe ListFp(1, 2, 3)
  }

  test("flatMap") {
    val empty = ListFp[Int]()
    val list = ListFp(1, 2, 3, 4)

    def twice: Int => ListFp[Int] = i => ListFp(i, i)

    empty.flatMap(twice) shouldBe ListFp()
    list.flatMap(twice) shouldBe ListFp(1, 1, 2, 2, 3, 3, 4, 4)
  }

  test("zipWith") {
    val empty = ListFp()
    val first = ListFp(1, 2, 3)
    val second = ListFp(4, 5, 6)

    empty.zipWith(first)(_.toString) shouldBe ListFp()
    first.zipWith(empty)(_.toString) shouldBe ListFp()
    first.zipWith(second)(tuple => s"${tuple._1}${tuple._2}") shouldBe ListFp("14", "25", "36")
  }

  test("hasSubsequence") {
    val empty = ListFp()
    val singleton = ListFp(1)
    val twice = ListFp(2, 3)
    val list = ListFp(1, 2, 3, 4)

    list.hasSubsequence(empty) shouldBe true
    list.hasSubsequence(singleton) shouldBe true
    list.hasSubsequence(twice) shouldBe true
    list.hasSubsequence(list) shouldBe true
    list.hasSubsequence(ListFp(5)) shouldBe false
    list.hasSubsequence(ListFp(2, 3, 5)) shouldBe false
    list.hasSubsequence(ListFp(1, 2, 3, 4, 5)) shouldBe false
  }

  test("sequence") {
    ListFp.sequence(List(Some(1), Some(2), Some(3))) shouldBe Some(List(1, 2, 3))
    ListFp.sequence(List(Some(1), Some(2), None)) shouldBe None
    ListFp.sequence(List(Some(1), None, None)) shouldBe None
    ListFp.sequence(List(None, None, None)) shouldBe None
    ListFp.sequence(List(None, Some(2), None)) shouldBe None
    ListFp.sequence(List(None, Some(2), Some(3))) shouldBe None
    ListFp.sequence(List()) shouldBe Some(List())
  }

  test("traverse") {
    def parse(str: String): Option[Int] =
      try Some(str.toInt)
      catch {
        case _: NumberFormatException => Option.empty[Int]
      }

    ListFp.traverse(List("1", "2", "3", "a"))(parse) shouldBe None
    ListFp.traverse(List("1", "2", "3", "4"))(parse) shouldBe Some(List(1, 2, 3, 4))
    ListFp.traverse(List("a", "1", "2", "3"))(parse) shouldBe None
    ListFp.traverse(List())(parse) shouldBe Some(List())
  }

  test("splitAt") {
    println(ListFp(1,2,3,4,5).splitAt(2))
  }
}
