package workshop.stream

import org.scalatest.Ignore

@Ignore
class StreamWorkshopTest extends org.scalatest.FunSuite with org.scalatest.matchers.should.Matchers {

  test("toList") {
    val stream = StreamWorkshop(1, 2, 3)

    stream.toList shouldBe List(1, 2, 3)
    StreamWorkshop.empty.toList shouldBe List()
  }

  test("take") {
    val stream = StreamWorkshop(1, 2, 3)

    stream.take(0).toList shouldBe List()
    stream.take(1).toList shouldBe List(1)
    stream.take(3).toList shouldBe List(1, 2, 3)
    stream.take(4).toList shouldBe List(1, 2, 3)
    StreamWorkshop.empty.take(1).toList shouldBe List()
  }

  test("drop") {
    val stream = StreamWorkshop(1, 2, 3)

    stream.drop(0).toList shouldBe List(1, 2, 3)
    stream.drop(1).toList shouldBe List(2, 3)
    stream.drop(3).toList shouldBe List()
    stream.drop(4).toList shouldBe List()
    StreamWorkshop.empty.drop(1).toList shouldBe List()
  }

  test("takeWhile") {
    val stream = StreamWorkshop(1, 2, 3)

    stream.takeWhile(_ < -1).toList shouldBe List()
    stream.takeWhile(_ <= 1).toList shouldBe List(1)
    stream.takeWhile(_ <= 3).toList shouldBe List(1, 2, 3)
    stream.takeWhile(_ <= 4).toList shouldBe List(1, 2, 3)
    StreamWorkshop.empty[Int].takeWhile(_ < 1).toList shouldBe List()
  }

  test("forAll") {
    val stream = StreamWorkshop(1, 2, 3)

    stream.forAll(_ > 0) shouldBe true
    stream.forAll(_ > 3) shouldBe false
    StreamWorkshop.empty[Int].forAll(_ > 3) shouldBe true
  }

  test("takeWhileFoldRight") {
    val stream = StreamWorkshop(1, 2, 3)

    stream.takeWhileFoldRight(_ < -1).toList shouldBe List()
    stream.takeWhileFoldRight(_ <= 1).toList shouldBe List(1)
    stream.takeWhileFoldRight(_ <= 3).toList shouldBe List(1, 2, 3)
    stream.takeWhileFoldRight(_ <= 4).toList shouldBe List(1, 2, 3)
    StreamWorkshop.empty[Int].takeWhile(_ < 1).toList shouldBe List()
  }

  test("headOption") {
    val stream = StreamWorkshop(1, 2, 3)
    val infinite = StreamWorkshop.constant(1)

    stream.headOption shouldBe Some(1)
    StreamWorkshop.empty[Int].headOption shouldBe None
    infinite.headOption shouldBe Some(1)
  }

  test("map") {
    val stream = StreamWorkshop(1, 2, 3)

    stream.map(_ + 1).toList shouldBe List(2, 3, 4)
    StreamWorkshop.empty[Int].map(_ + 1).toList shouldBe List()
  }

  test("map is lazy") {
    val infinite = StreamWorkshop.constant(1)

    infinite.map(_ * 2)
  }

  test("filter") {
    val stream = StreamWorkshop(1, 2, 3)

    stream.filter(_ >= 2).toList shouldBe List(2, 3)
    StreamWorkshop.empty[Int].filter(_ > 1).toList shouldBe List()
  }

  test("append") {
    val stream = StreamWorkshop(1, 2, 3)
    val stream2 = StreamWorkshop(4, 5)

    stream.append(stream2).toList shouldBe List(1, 2, 3, 4, 5)
    stream2.append(stream).toList shouldBe List(4, 5, 1, 2, 3)
    StreamWorkshop.empty.append(stream).toList shouldBe List(1, 2, 3)
    stream.append(StreamWorkshop.empty).toList shouldBe List(1, 2, 3)
  }

  test("flatMap") {
    val stream = StreamWorkshop(1, 2, 3)

    stream.flatMap(i => StreamWorkshop.cons(i, StreamWorkshop.cons(i, StreamWorkshop.empty))).toList shouldBe List(1, 1, 2, 2, 3, 3)
    stream.flatMap(_ => StreamWorkshop.empty).toList shouldBe List()
    StreamWorkshop.empty[Int].flatMap(_ => StreamWorkshop.cons(1, StreamWorkshop.empty)).toList shouldBe List()
  }

  test("mapUnfold") {
    val stream = StreamWorkshop(1, 2, 3)

    stream.mapUnfold(_ + 1).toList shouldBe List(2, 3, 4)
    StreamWorkshop.empty[Int].mapUnfold(_ + 1).toList shouldBe List()
  }

  test("constant") {
    val stream = StreamWorkshop.constant(1)

    stream.take(3).toList shouldBe List(1, 1, 1)
  }

  test("from") {
    val stream = StreamWorkshop.from(1)

    stream.take(0).toList shouldBe List()
    stream.take(3).toList shouldBe List(1, 2, 3)
  }

  test("fibs") {
    val stream = StreamWorkshop.fibs()

    stream.take(0).toList shouldBe List()
    stream.take(5).toList shouldBe List(0, 1, 1, 2, 3)
  }

  test("fibsUnfold") {
    val stream = StreamWorkshop.fibUnfold()

    stream.take(0).toList shouldBe List()
    stream.take(5).toList shouldBe List(0, 1, 1, 2, 3)
  }

  test("startsWith") {
    val stream = StreamWorkshop(1, 2, 3)

    stream.startsWith(stream) shouldBe true
    stream.startsWith(StreamWorkshop(1, 2)) shouldBe true
    stream.startsWith(StreamWorkshop(1)) shouldBe true
    stream.startsWith(StreamWorkshop.empty) shouldBe true
    stream.startsWith(StreamWorkshop(1, 2, 4)) shouldBe false
    stream.startsWith(StreamWorkshop(2)) shouldBe false
    stream.startsWith(StreamWorkshop(1, 2, 3, 4)) shouldBe false
  }

  test("tails") {
    val stream = StreamWorkshop(1, 2, 3)

    stream.tails.toList.map(_.toList) shouldBe List(List(1, 2, 3), List(2, 3), List(3), List())
    StreamWorkshop.empty[Int].tails.toList.map(_.toList) shouldBe List(List())
  }

  test("scanRight") {
    val stream = StreamWorkshop(1, 2, 3)

    stream.scanRight(0)(_ + _).toList shouldBe List(6, 5, 3, 0)
    StreamWorkshop.empty[Int].scanRight(0)(_ + _).toList shouldBe List(0)
  }
}
