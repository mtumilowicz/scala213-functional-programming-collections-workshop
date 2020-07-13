package workshop.list

import workshop.list.ListFpWorkshopUtils._

class ListFpWorkshopTest extends org.scalatest.FunSuite with org.scalatest.matchers.should.Matchers {

  test("tail") {
    val empty = ListFpWorkshop()
    val singleton = ListFpWorkshop(1)
    val list = ListFpWorkshop(1, 2, 3, 4)

    empty.tail() shouldBe NilFpWorkshop
    singleton.tail() shouldBe NilFpWorkshop
    list.tail() shouldBe ListFpWorkshop(2, 3, 4)
  }

  test("replaceHead") {
    val empty = ListFpWorkshop()
    val singleton = ListFpWorkshop(1)
    val list = ListFpWorkshop(1, 2, 3, 4)

    empty.replaceHead(1) shouldBe NilFpWorkshop
    singleton.replaceHead(2) shouldBe ListFpWorkshop(2)
    list.replaceHead(3) shouldBe ListFpWorkshop(3, 2, 3, 4)
  }

  test("drop") {
    val empty = ListFpWorkshop()
    val singleton = ListFpWorkshop(1)
    val list = ListFpWorkshop(1, 2, 3, 4)

    empty.drop(0) shouldBe ListFpWorkshop()
    empty.drop(1) shouldBe ListFpWorkshop()
    singleton.drop(0) shouldBe ListFpWorkshop(1)
    singleton.drop(1) shouldBe ListFpWorkshop()
    list.drop(1) shouldBe ListFpWorkshop(2, 3, 4)
    list.drop(2) shouldBe ListFpWorkshop(3, 4)
    list.drop(4) shouldBe ListFpWorkshop()
  }

  test("dropWhile") {
    val empty = ListFpWorkshop[Int]()
    val singleton = ListFpWorkshop(1)
    val list = ListFpWorkshop(1, 2, 3, 4)

    empty.dropWhile(_ < 1) shouldBe ListFpWorkshop()
    singleton.dropWhile(_ < 1) shouldBe ListFpWorkshop(1)
    singleton.dropWhile(_ == 1) shouldBe ListFpWorkshop()
    list.dropWhile(_ <= 1) shouldBe ListFpWorkshop(2, 3, 4)
    list.dropWhile(_ <= 3) shouldBe ListFpWorkshop(4)
    list.dropWhile(_ <= 4) shouldBe ListFpWorkshop()
  }

  test("removeLast") {
    val empty = ListFpWorkshop()
    val singleton = ListFpWorkshop(1)
    val twice = ListFpWorkshop(1, 2)
    val list = ListFpWorkshop(1, 2, 3, 4)

    empty.removeLast() shouldBe NilFpWorkshop
    singleton.removeLast() shouldBe NilFpWorkshop
    twice.removeLast() shouldBe ListFpWorkshop(1)
    list.removeLast() shouldBe ListFpWorkshop(1, 2, 3)
  }

  test("length") {
    val empty = ListFpWorkshop()
    val singleton = ListFpWorkshop(1)
    val twice = ListFpWorkshop(1, 2)
    val list = ListFpWorkshop(1, 2, 3, 4)

    empty.length() shouldBe 0
    singleton.length() shouldBe 1
    twice.length() shouldBe 2
    list.length() shouldBe 4
  }

  test("foldLeft") {
    val empty = ListFpWorkshop()
    val singleton = ListFpWorkshop(1)
    val twice = ListFpWorkshop(1, 2)
    val list = ListFpWorkshop(1, 2, 3, 4)

    empty.foldLeft("")(_ + _) shouldBe ""
    singleton.foldLeft("")(_ + _) shouldBe "1"
    twice.foldLeft("")(_ + _) shouldBe "12"
    list.foldLeft("")(_ + _) shouldBe "1234"
  }

  test("reverse") {
    val empty = ListFpWorkshop()
    val singleton = ListFpWorkshop(1)
    val twice = ListFpWorkshop(1, 2)
    val list = ListFpWorkshop(1, 2, 3, 4)

    empty.reverse() shouldBe ListFpWorkshop()
    singleton.reverse() shouldBe ListFpWorkshop(1)
    twice.reverse() shouldBe ListFpWorkshop(2, 1)
    list.reverse() shouldBe ListFpWorkshop(4, 3, 2, 1)
  }

  test("foldRight") {
    val empty = ListFpWorkshop[Int]()
    val singleton = ListFpWorkshop(1)
    val twice = ListFpWorkshop(1, 2)
    val list = ListFpWorkshop(1, 2, 3, 4)

    empty.foldRight("")(_ + _) shouldBe ""
    singleton.foldRight("")(_ + _) shouldBe "1"
    twice.foldRight("")(_ + _) shouldBe "12"
    list.foldRight("")(_ + _) shouldBe "1234"
  }

  test("foldLeftByFoldRight") {
    val empty = ListFpWorkshop[String]()
    val singleton = ListFpWorkshop(1)
    val twice = ListFpWorkshop(1, 2)
    val list = ListFpWorkshop(1, 2, 3, 4)

    empty.foldLeftByFoldRight("")(_ + _) shouldBe ""
    singleton.foldLeftByFoldRight("")(_ + _) shouldBe "1"
    twice.foldLeftByFoldRight("")(_ + _) shouldBe "12"
    list.foldLeftByFoldRight("")(_ + _) shouldBe "1234"
  }

  test("append") {
    val empty = ListFpWorkshop[String]()
    val list = ListFpWorkshop(1, 2, 3, 4)

    empty.append(1) shouldBe ListFpWorkshop(1)
    list.append(5) shouldBe ListFpWorkshop(1, 2, 3, 4, 5)
  }

  test("flatten") {
    val l1 = ListFpWorkshop[String]()
    val l2 = ListFpWorkshop(1)
    val l3 = ListFpWorkshop(2, 3)
    val l4 = ListFpWorkshop(4, 5, 6)
    val listOfLists = ListFpWorkshop(l1, l2, l3, l4)

    listOfLists.flatten() shouldBe ListFpWorkshop(1, 2, 3, 4, 5, 6)
  }

  test("sum") {
    val l1 = ListFpWorkshop[Int]()
    val l2 = ListFpWorkshop[Double](1)
    val l3 = ListFpWorkshop[BigDecimal](2, 3)
    val l4 = ListFpWorkshop[Float](4, 5, 6)

    l1.sum() shouldBe 0
    l2.sum() shouldBe 1
    l3.sum() shouldBe 5
    l4.sum() shouldBe 15
  }

  test("map") {
    val empty = ListFpWorkshop[String]()
    val list = ListFpWorkshop(1, 2, 3, 4)

    empty.map(_ + "1") shouldBe ListFpWorkshop()
    list.map(_.toString) shouldBe ListFpWorkshop("1", "2", "3", "4")
  }

  test("filter") {
    val empty = ListFpWorkshop[String]()
    val list = ListFpWorkshop(1, 2, 3, 4)

    empty.filter(_.length == 0) shouldBe ListFpWorkshop()
    list.filter(_ <= 3) shouldBe ListFpWorkshop(1, 2, 3)
  }

  test("flatMap") {
    val empty = ListFpWorkshop[Int]()
    val list = ListFpWorkshop(1, 2, 3, 4)

    def twice: Int => ListFpWorkshop[Int] = i => ListFpWorkshop(i, i)

    empty.flatMap(twice) shouldBe ListFpWorkshop()
    list.flatMap(twice) shouldBe ListFpWorkshop(1, 1, 2, 2, 3, 3, 4, 4)
  }

  test("zipWith") {
    val empty = ListFpWorkshop()
    val first = ListFpWorkshop(1, 2, 3)
    val second = ListFpWorkshop(4, 5, 6)

    empty.zipWith(first)(_.toString) shouldBe ListFpWorkshop()
    first.zipWith(empty)(_.toString) shouldBe ListFpWorkshop()
    first.zipWith(second)(tuple => s"${tuple._1}${tuple._2}") shouldBe ListFpWorkshop("14", "25", "36")
  }

  test("hasSubsequence") {
    val empty = ListFpWorkshop()
    val singleton = ListFpWorkshop(1)
    val twice = ListFpWorkshop(2, 3)
    val list = ListFpWorkshop(1, 2, 3, 4)

    list.hasSubsequence(empty) shouldBe true
    list.hasSubsequence(singleton) shouldBe true
    list.hasSubsequence(twice) shouldBe true
    list.hasSubsequence(list) shouldBe true
    list.hasSubsequence(ListFpWorkshop(5)) shouldBe false
    list.hasSubsequence(ListFpWorkshop(2, 3, 5)) shouldBe false
    list.hasSubsequence(ListFpWorkshop(1, 2, 3, 4, 5)) shouldBe false
  }

  test("sequence") {
    ListFpWorkshop.sequence(List(Some(1), Some(2), Some(3))) shouldBe Some(List(1, 2, 3))
    ListFpWorkshop.sequence(List(Some(1), Some(2), None)) shouldBe None
    ListFpWorkshop.sequence(List(Some(1), None, None)) shouldBe None
    ListFpWorkshop.sequence(List(None, None, None)) shouldBe None
    ListFpWorkshop.sequence(List(None, Some(2), None)) shouldBe None
    ListFpWorkshop.sequence(List(None, Some(2), Some(3))) shouldBe None
    ListFpWorkshop.sequence(List()) shouldBe Some(List())
  }

  test("traverse") {
    def parse(str: String): Option[Int] =
      try Some(str.toInt)
      catch {
        case _: NumberFormatException => Option.empty[Int]
      }

    ListFpWorkshop.traverse(List("1", "2", "3", "a"))(parse) shouldBe None
    ListFpWorkshop.traverse(List("1", "2", "3", "4"))(parse) shouldBe Some(List(1, 2, 3, 4))
    ListFpWorkshop.traverse(List("a", "1", "2", "3"))(parse) shouldBe None
    ListFpWorkshop.traverse(List())(parse) shouldBe Some(List())
  }

  test("concat") {
    val l1 = ListFpWorkshop(1, 2, 3)
    val l2 = ListFpWorkshop(4, 5)

    l1.concat(l2) shouldBe ListFpWorkshop(1, 2, 3, 4, 5)
  }

  test("splitAt") {
    ListFpWorkshop(1, 2, 3, 4, 5).splitAt(2) shouldBe(ListFpWorkshop(1, 2), ListFpWorkshop(3, 4, 5))
  }
}
