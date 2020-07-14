package answers.list

import answers.list.ListAnswerUtils._

class ListAnswerTest extends org.scalatest.FunSuite with org.scalatest.matchers.should.Matchers {

  test("tail") {
    val empty = ListAnswer()
    val singleton = ListAnswer(1)
    val list = ListAnswer(1, 2, 3, 4)

    empty.tail() shouldBe NilAnswer
    singleton.tail() shouldBe NilAnswer
    list.tail() shouldBe ListAnswer(2, 3, 4)
  }

  test("replaceHead") {
    val empty = ListAnswer()
    val singleton = ListAnswer(1)
    val list = ListAnswer(1, 2, 3, 4)

    empty.replaceHead(1) shouldBe NilAnswer
    singleton.replaceHead(2) shouldBe ListAnswer(2)
    list.replaceHead(3) shouldBe ListAnswer(3, 2, 3, 4)
  }

  test("drop") {
    val empty = ListAnswer()
    val singleton = ListAnswer(1)
    val list = ListAnswer(1, 2, 3, 4)

    empty.drop(0) shouldBe ListAnswer()
    empty.drop(1) shouldBe ListAnswer()
    singleton.drop(0) shouldBe ListAnswer(1)
    singleton.drop(1) shouldBe ListAnswer()
    list.drop(1) shouldBe ListAnswer(2, 3, 4)
    list.drop(2) shouldBe ListAnswer(3, 4)
    list.drop(4) shouldBe ListAnswer()
  }

  test("dropWhile") {
    val empty = ListAnswer[Int]()
    val singleton = ListAnswer(1)
    val list = ListAnswer(1, 2, 3, 4)

    empty.dropWhile(_ < 1) shouldBe ListAnswer()
    singleton.dropWhile(_ < 1) shouldBe ListAnswer(1)
    singleton.dropWhile(_ == 1) shouldBe ListAnswer()
    list.dropWhile(_ <= 1) shouldBe ListAnswer(2, 3, 4)
    list.dropWhile(_ <= 3) shouldBe ListAnswer(4)
    list.dropWhile(_ <= 4) shouldBe ListAnswer()
  }

  test("removeLast") {
    val empty = ListAnswer()
    val singleton = ListAnswer(1)
    val twice = ListAnswer(1, 2)
    val list = ListAnswer(1, 2, 3, 4)

    empty.removeLast() shouldBe NilAnswer
    singleton.removeLast() shouldBe NilAnswer
    twice.removeLast() shouldBe ListAnswer(1)
    list.removeLast() shouldBe ListAnswer(1, 2, 3)
  }

  test("length") {
    val empty = ListAnswer()
    val singleton = ListAnswer(1)
    val twice = ListAnswer(1, 2)
    val list = ListAnswer(1, 2, 3, 4)

    empty.length() shouldBe 0
    singleton.length() shouldBe 1
    twice.length() shouldBe 2
    list.length() shouldBe 4
  }

  test("foldLeft") {
    val empty = ListAnswer()
    val singleton = ListAnswer(1)
    val twice = ListAnswer(1, 2)
    val list = ListAnswer(1, 2, 3, 4)

    empty.foldLeft("")(_ + _) shouldBe ""
    singleton.foldLeft("")(_ + _) shouldBe "1"
    twice.foldLeft("")(_ + _) shouldBe "12"
    list.foldLeft("")(_ + _) shouldBe "1234"
  }

  test("reverse") {
    val empty = ListAnswer()
    val singleton = ListAnswer(1)
    val twice = ListAnswer(1, 2)
    val list = ListAnswer(1, 2, 3, 4)

    empty.reverse() shouldBe ListAnswer()
    singleton.reverse() shouldBe ListAnswer(1)
    twice.reverse() shouldBe ListAnswer(2, 1)
    list.reverse() shouldBe ListAnswer(4, 3, 2, 1)
  }

  test("foldRight") {
    val empty = ListAnswer[Int]()
    val singleton = ListAnswer(1)
    val twice = ListAnswer(1, 2)
    val list = ListAnswer(1, 2, 3, 4)

    empty.foldRight("")(_ + _) shouldBe ""
    singleton.foldRight("")(_ + _) shouldBe "1"
    twice.foldRight("")(_ + _) shouldBe "12"
    list.foldRight("")(_ + _) shouldBe "1234"
  }

  test("foldLeftByFoldRight") {
    val empty = ListAnswer[String]()
    val singleton = ListAnswer(1)
    val twice = ListAnswer(1, 2)
    val list = ListAnswer(1, 2, 3, 4)

    empty.foldLeftByFoldRight("")(_ + _) shouldBe ""
    singleton.foldLeftByFoldRight("")(_ + _) shouldBe "1"
    twice.foldLeftByFoldRight("")(_ + _) shouldBe "12"
    list.foldLeftByFoldRight("")(_ + _) shouldBe "1234"
  }

  test("append") {
    val empty = ListAnswer[String]()
    val list = ListAnswer(1, 2, 3, 4)

    empty.append(1) shouldBe ListAnswer(1)
    list.append(5) shouldBe ListAnswer(1, 2, 3, 4, 5)
  }

  test("flatten") {
    val l1 = ListAnswer[String]()
    val l2 = ListAnswer(1)
    val l3 = ListAnswer(2, 3)
    val l4 = ListAnswer(4, 5, 6)
    val listOfLists = ListAnswer(l1, l2, l3, l4)

    listOfLists.flatten() shouldBe ListAnswer(1, 2, 3, 4, 5, 6)
  }

  test("sum") {
    val l1 = ListAnswer[Int]()
    val l2 = ListAnswer[Double](1)
    val l3 = ListAnswer[BigDecimal](2, 3)
    val l4 = ListAnswer[Float](4, 5, 6)

    l1.sum() shouldBe 0
    l2.sum() shouldBe 1
    l3.sum() shouldBe 5
    l4.sum() shouldBe 15
  }

  test("map") {
    val empty = ListAnswer[String]()
    val list = ListAnswer(1, 2, 3, 4)

    empty.map(_ + "1") shouldBe ListAnswer()
    list.map(_.toString) shouldBe ListAnswer("1", "2", "3", "4")
  }

  test("filter") {
    val empty = ListAnswer[String]()
    val list = ListAnswer(1, 2, 3, 4)

    empty.filter(_.length == 0) shouldBe ListAnswer()
    list.filter(_ <= 3) shouldBe ListAnswer(1, 2, 3)
  }

  test("flatMap") {
    val empty = ListAnswer[Int]()
    val list = ListAnswer(1, 2, 3, 4)

    def twice: Int => ListAnswer[Int] = i => ListAnswer(i, i)

    empty.flatMap(twice) shouldBe ListAnswer()
    list.flatMap(twice) shouldBe ListAnswer(1, 1, 2, 2, 3, 3, 4, 4)
  }

  test("zipWith") {
    val empty = ListAnswer()
    val first = ListAnswer(1, 2, 3)
    val second = ListAnswer(4, 5, 6)

    empty.zipWith(first)(_.toString) shouldBe ListAnswer()
    first.zipWith(empty)(_.toString) shouldBe ListAnswer()
    first.zipWith(second)(tuple => s"${tuple._1}${tuple._2}") shouldBe ListAnswer("14", "25", "36")
  }

  test("hasSubsequence") {
    val empty = ListAnswer()
    val singleton = ListAnswer(1)
    val twice = ListAnswer(2, 3)
    val list = ListAnswer(1, 2, 3, 4)

    list.hasSubsequence(empty) shouldBe true
    list.hasSubsequence(singleton) shouldBe true
    list.hasSubsequence(twice) shouldBe true
    list.hasSubsequence(list) shouldBe true
    list.hasSubsequence(ListAnswer(5)) shouldBe false
    list.hasSubsequence(ListAnswer(2, 3, 5)) shouldBe false
    list.hasSubsequence(ListAnswer(1, 2, 3, 4, 5)) shouldBe false
  }

  test("sequence") {
    ListAnswer.sequence(List(Some(1), Some(2), Some(3))) shouldBe Some(List(1, 2, 3))
    ListAnswer.sequence(List(Some(1), Some(2), None)) shouldBe None
    ListAnswer.sequence(List(Some(1), None, None)) shouldBe None
    ListAnswer.sequence(List(None, None, None)) shouldBe None
    ListAnswer.sequence(List(None, Some(2), None)) shouldBe None
    ListAnswer.sequence(List(None, Some(2), Some(3))) shouldBe None
    ListAnswer.sequence(List()) shouldBe Some(List())
  }

  test("traverse") {
    def parse(str: String): Option[Int] =
      try Some(str.toInt)
      catch {
        case _: NumberFormatException => Option.empty[Int]
      }

    ListAnswer.traverse(List("1", "2", "3", "a"))(parse) shouldBe None
    ListAnswer.traverse(List("1", "2", "3", "4"))(parse) shouldBe Some(List(1, 2, 3, 4))
    ListAnswer.traverse(List("a", "1", "2", "3"))(parse) shouldBe None
    ListAnswer.traverse(List())(parse) shouldBe Some(List())
  }

  test("concat") {
    val l1 = ListAnswer(1, 2, 3)
    val l2 = ListAnswer(4, 5)

    l1.concat(l2) shouldBe ListAnswer(1, 2, 3, 4, 5)
  }

  test("splitAt") {
    ListAnswer(1, 2, 3, 4, 5).splitAt(2) shouldBe(ListAnswer(1, 2), ListAnswer(3, 4, 5))
  }
}
