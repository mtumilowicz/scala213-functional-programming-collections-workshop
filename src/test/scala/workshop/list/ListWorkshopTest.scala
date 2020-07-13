package workshop.list

import workshop.list.ListWorkshopUtils._

class ListWorkshopTest extends org.scalatest.FunSuite with org.scalatest.matchers.should.Matchers {

  test("tail") {
    val empty = ListWorkshop()
    val singleton = ListWorkshop(1)
    val list = ListWorkshop(1, 2, 3, 4)

    empty.tail() shouldBe NilWorkshop
    singleton.tail() shouldBe NilWorkshop
    list.tail() shouldBe ListWorkshop(2, 3, 4)
  }

  test("replaceHead") {
    val empty = ListWorkshop()
    val singleton = ListWorkshop(1)
    val list = ListWorkshop(1, 2, 3, 4)

    empty.replaceHead(1) shouldBe NilWorkshop
    singleton.replaceHead(2) shouldBe ListWorkshop(2)
    list.replaceHead(3) shouldBe ListWorkshop(3, 2, 3, 4)
  }

  test("drop") {
    val empty = ListWorkshop()
    val singleton = ListWorkshop(1)
    val list = ListWorkshop(1, 2, 3, 4)

    empty.drop(0) shouldBe ListWorkshop()
    empty.drop(1) shouldBe ListWorkshop()
    singleton.drop(0) shouldBe ListWorkshop(1)
    singleton.drop(1) shouldBe ListWorkshop()
    list.drop(1) shouldBe ListWorkshop(2, 3, 4)
    list.drop(2) shouldBe ListWorkshop(3, 4)
    list.drop(4) shouldBe ListWorkshop()
  }

  test("dropWhile") {
    val empty = ListWorkshop[Int]()
    val singleton = ListWorkshop(1)
    val list = ListWorkshop(1, 2, 3, 4)

    empty.dropWhile(_ < 1) shouldBe ListWorkshop()
    singleton.dropWhile(_ < 1) shouldBe ListWorkshop(1)
    singleton.dropWhile(_ == 1) shouldBe ListWorkshop()
    list.dropWhile(_ <= 1) shouldBe ListWorkshop(2, 3, 4)
    list.dropWhile(_ <= 3) shouldBe ListWorkshop(4)
    list.dropWhile(_ <= 4) shouldBe ListWorkshop()
  }

  test("removeLast") {
    val empty = ListWorkshop()
    val singleton = ListWorkshop(1)
    val twice = ListWorkshop(1, 2)
    val list = ListWorkshop(1, 2, 3, 4)

    empty.removeLast() shouldBe NilWorkshop
    singleton.removeLast() shouldBe NilWorkshop
    twice.removeLast() shouldBe ListWorkshop(1)
    list.removeLast() shouldBe ListWorkshop(1, 2, 3)
  }

  test("length") {
    val empty = ListWorkshop()
    val singleton = ListWorkshop(1)
    val twice = ListWorkshop(1, 2)
    val list = ListWorkshop(1, 2, 3, 4)

    empty.length() shouldBe 0
    singleton.length() shouldBe 1
    twice.length() shouldBe 2
    list.length() shouldBe 4
  }

  test("foldLeft") {
    val empty = ListWorkshop()
    val singleton = ListWorkshop(1)
    val twice = ListWorkshop(1, 2)
    val list = ListWorkshop(1, 2, 3, 4)

    empty.foldLeft("")(_ + _) shouldBe ""
    singleton.foldLeft("")(_ + _) shouldBe "1"
    twice.foldLeft("")(_ + _) shouldBe "12"
    list.foldLeft("")(_ + _) shouldBe "1234"
  }

  test("reverse") {
    val empty = ListWorkshop()
    val singleton = ListWorkshop(1)
    val twice = ListWorkshop(1, 2)
    val list = ListWorkshop(1, 2, 3, 4)

    empty.reverse() shouldBe ListWorkshop()
    singleton.reverse() shouldBe ListWorkshop(1)
    twice.reverse() shouldBe ListWorkshop(2, 1)
    list.reverse() shouldBe ListWorkshop(4, 3, 2, 1)
  }

  test("foldRight") {
    val empty = ListWorkshop[Int]()
    val singleton = ListWorkshop(1)
    val twice = ListWorkshop(1, 2)
    val list = ListWorkshop(1, 2, 3, 4)

    empty.foldRight("")(_ + _) shouldBe ""
    singleton.foldRight("")(_ + _) shouldBe "1"
    twice.foldRight("")(_ + _) shouldBe "12"
    list.foldRight("")(_ + _) shouldBe "1234"
  }

  test("foldLeftByFoldRight") {
    val empty = ListWorkshop[String]()
    val singleton = ListWorkshop(1)
    val twice = ListWorkshop(1, 2)
    val list = ListWorkshop(1, 2, 3, 4)

    empty.foldLeftByFoldRight("")(_ + _) shouldBe ""
    singleton.foldLeftByFoldRight("")(_ + _) shouldBe "1"
    twice.foldLeftByFoldRight("")(_ + _) shouldBe "12"
    list.foldLeftByFoldRight("")(_ + _) shouldBe "1234"
  }

  test("append") {
    val empty = ListWorkshop[String]()
    val list = ListWorkshop(1, 2, 3, 4)

    empty.append(1) shouldBe ListWorkshop(1)
    list.append(5) shouldBe ListWorkshop(1, 2, 3, 4, 5)
  }

  test("flatten") {
    val l1 = ListWorkshop[String]()
    val l2 = ListWorkshop(1)
    val l3 = ListWorkshop(2, 3)
    val l4 = ListWorkshop(4, 5, 6)
    val listOfLists = ListWorkshop(l1, l2, l3, l4)

    listOfLists.flatten() shouldBe ListWorkshop(1, 2, 3, 4, 5, 6)
  }

  test("sum") {
    val l1 = ListWorkshop[Int]()
    val l2 = ListWorkshop[Double](1)
    val l3 = ListWorkshop[BigDecimal](2, 3)
    val l4 = ListWorkshop[Float](4, 5, 6)

    l1.sum() shouldBe 0
    l2.sum() shouldBe 1
    l3.sum() shouldBe 5
    l4.sum() shouldBe 15
  }

  test("map") {
    val empty = ListWorkshop[String]()
    val list = ListWorkshop(1, 2, 3, 4)

    empty.map(_ + "1") shouldBe ListWorkshop()
    list.map(_.toString) shouldBe ListWorkshop("1", "2", "3", "4")
  }

  test("filter") {
    val empty = ListWorkshop[String]()
    val list = ListWorkshop(1, 2, 3, 4)

    empty.filter(_.length == 0) shouldBe ListWorkshop()
    list.filter(_ <= 3) shouldBe ListWorkshop(1, 2, 3)
  }

  test("flatMap") {
    val empty = ListWorkshop[Int]()
    val list = ListWorkshop(1, 2, 3, 4)

    def twice: Int => ListWorkshop[Int] = i => ListWorkshop(i, i)

    empty.flatMap(twice) shouldBe ListWorkshop()
    list.flatMap(twice) shouldBe ListWorkshop(1, 1, 2, 2, 3, 3, 4, 4)
  }

  test("zipWith") {
    val empty = ListWorkshop()
    val first = ListWorkshop(1, 2, 3)
    val second = ListWorkshop(4, 5, 6)

    empty.zipWith(first)(_.toString) shouldBe ListWorkshop()
    first.zipWith(empty)(_.toString) shouldBe ListWorkshop()
    first.zipWith(second)(tuple => s"${tuple._1}${tuple._2}") shouldBe ListWorkshop("14", "25", "36")
  }

  test("hasSubsequence") {
    val empty = ListWorkshop()
    val singleton = ListWorkshop(1)
    val twice = ListWorkshop(2, 3)
    val list = ListWorkshop(1, 2, 3, 4)

    list.hasSubsequence(empty) shouldBe true
    list.hasSubsequence(singleton) shouldBe true
    list.hasSubsequence(twice) shouldBe true
    list.hasSubsequence(list) shouldBe true
    list.hasSubsequence(ListWorkshop(5)) shouldBe false
    list.hasSubsequence(ListWorkshop(2, 3, 5)) shouldBe false
    list.hasSubsequence(ListWorkshop(1, 2, 3, 4, 5)) shouldBe false
  }

  test("sequence") {
    ListWorkshop.sequence(List(Some(1), Some(2), Some(3))) shouldBe Some(List(1, 2, 3))
    ListWorkshop.sequence(List(Some(1), Some(2), None)) shouldBe None
    ListWorkshop.sequence(List(Some(1), None, None)) shouldBe None
    ListWorkshop.sequence(List(None, None, None)) shouldBe None
    ListWorkshop.sequence(List(None, Some(2), None)) shouldBe None
    ListWorkshop.sequence(List(None, Some(2), Some(3))) shouldBe None
    ListWorkshop.sequence(List()) shouldBe Some(List())
  }

  test("traverse") {
    def parse(str: String): Option[Int] =
      try Some(str.toInt)
      catch {
        case _: NumberFormatException => Option.empty[Int]
      }

    ListWorkshop.traverse(List("1", "2", "3", "a"))(parse) shouldBe None
    ListWorkshop.traverse(List("1", "2", "3", "4"))(parse) shouldBe Some(List(1, 2, 3, 4))
    ListWorkshop.traverse(List("a", "1", "2", "3"))(parse) shouldBe None
    ListWorkshop.traverse(List())(parse) shouldBe Some(List())
  }

  test("concat") {
    val l1 = ListWorkshop(1, 2, 3)
    val l2 = ListWorkshop(4, 5)

    l1.concat(l2) shouldBe ListWorkshop(1, 2, 3, 4, 5)
  }

  test("splitAt") {
    ListWorkshop(1, 2, 3, 4, 5).splitAt(2) shouldBe(ListWorkshop(1, 2), ListWorkshop(3, 4, 5))
  }
}
