package answers.stream

class StreamFpTest extends org.scalatest.FunSuite with org.scalatest.matchers.should.Matchers {

  test("toList") {
    val stream = StreamFp.cons(1, StreamFp.cons(2, StreamFp.cons(3, StreamFp.empty)))

    stream.toList == List(1, 2, 3)
    StreamFp.empty.toList shouldBe List()
  }

  test("take") {
    val stream = StreamFp.cons(1, StreamFp.cons(2, StreamFp.cons(3, StreamFp.empty)))

    stream.take(0).toList shouldBe List()
    stream.take(1).toList shouldBe List(1)
    stream.take(3).toList shouldBe List(1, 2, 3)
    stream.take(4).toList shouldBe List(1, 2, 3)
    StreamFp.empty.take(1).toList shouldBe List()
  }

  test("drop") {
    val stream = StreamFp.cons(1, StreamFp.cons(2, StreamFp.cons(3, StreamFp.empty)))

    stream.drop(0).toList shouldBe List(1, 2, 3)
    stream.drop(1).toList shouldBe List(2, 3)
    stream.drop(3).toList shouldBe List()
    stream.drop(4).toList shouldBe List()
    StreamFp.empty.drop(1).toList shouldBe List()
  }

  test("takeWhile") {
    val stream = StreamFp.cons(1, StreamFp.cons(2, StreamFp.cons(3, StreamFp.empty)))

    stream.takeWhile(_ < -1).toList shouldBe List()
    stream.takeWhile(_ <= 1).toList shouldBe List(1)
    stream.takeWhile(_ <= 3).toList shouldBe List(1, 2, 3)
    stream.takeWhile(_ <= 4).toList shouldBe List(1, 2, 3)
    StreamFp.empty[Int].takeWhile(_ < 1).toList shouldBe List()
  }

  test("forAll") {
    val stream = StreamFp.cons(1, StreamFp.cons(2, StreamFp.cons(3, StreamFp.empty)))

    stream.forAll(_ > 0) shouldBe true
    stream.forAll(_ > 3) shouldBe false
    StreamFp.empty[Int].forAll(_ > 3) shouldBe true
  }

  test("takeWhileFold") {
    val stream = StreamFp.cons(1, StreamFp.cons(2, StreamFp.cons(3, StreamFp.empty)))

    stream.takeWhileFold(_ < -1).toList shouldBe List()
    stream.takeWhileFold(_ <= 1).toList shouldBe List(1)
    stream.takeWhileFold(_ <= 3).toList shouldBe List(1, 2, 3)
    stream.takeWhileFold(_ <= 4).toList shouldBe List(1, 2, 3)
    StreamFp.empty[Int].takeWhile(_ < 1).toList shouldBe List()
  }

  test("headOptionFold") {
    val stream = StreamFp.cons(1, StreamFp.cons(2, StreamFp.cons(3, StreamFp.empty)))

    stream.headOptionFold shouldBe Some(1)
    StreamFp.empty[Int].headOptionFold shouldBe None
  }

  test("map") {
    val stream = StreamFp.cons(1, StreamFp.cons(2, StreamFp.cons(3, StreamFp.empty)))

    stream.map(_ + 1).toList shouldBe List(2, 3, 4)
    StreamFp.empty[Int].map(_ + 1).toList shouldBe List()
  }

  test("filter") {
    val stream = StreamFp.cons(1, StreamFp.cons(2, StreamFp.cons(3, StreamFp.empty)))

    stream.filter(_ >= 2).toList shouldBe List(2, 3)
    StreamFp.empty[Int].filter(_ > 1).toList shouldBe List()
  }

  test("append") {
    val stream = StreamFp.cons(1, StreamFp.cons(2, StreamFp.cons(3, StreamFp.empty)))
    val stream2 = StreamFp.cons(4, StreamFp.cons(5, StreamFp.empty))

    stream.append(stream2).toList == List(1, 2, 3, 4, 5)
    stream2.append(stream).toList == List(4, 5, 1, 2, 3)
    StreamFp.empty.append(stream).toList == List(1, 2, 3)
    stream.append(StreamFp.empty).toList == List(1, 2, 3)
  }

  test("flatMap") {
    val stream = StreamFp.cons(1, StreamFp.cons(2, StreamFp.cons(3, StreamFp.empty)))

    stream.flatMap(i => StreamFp.cons(i, StreamFp.cons(i, StreamFp.empty))).toList shouldBe List(1, 1, 2, 2, 3, 3)
    stream.flatMap(_ => StreamFp.empty).toList shouldBe List()
    StreamFp.empty[Int].flatMap(_ => StreamFp.cons(1, StreamFp.empty)).toList == List()
  }

  test("mapUnfold") {
    val stream = StreamFp.cons(1, StreamFp.cons(2, StreamFp.cons(3, StreamFp.empty)))

    stream.mapUnfold(_ + 1).toList shouldBe List(2, 3, 4)
    StreamFp.empty[Int].mapUnfold(_ + 1).toList shouldBe List()
  }

  test("constant") {
    val stream = StreamFp.constant(1)

    stream.take(3).toList shouldBe List(1, 1, 1)
  }

  test("from") {
    val stream = StreamFp.from(1)

    stream.take(0).toList shouldBe List()
    stream.take(3).toList shouldBe List(1, 2, 3)
  }

  test("fibs") {
    val stream = StreamFp.fibs()

    stream.take(0).toList shouldBe List()
    stream.take(5).toList shouldBe List(0, 1, 1, 2, 3)
  }

  test("constantUnfold") {
    val stream = StreamFp.constantUnfold(1)

    stream.take(3).toList shouldBe List(1, 1, 1)
  }

  test("fromUnfold") {
    val stream = StreamFp.fromUnfold(1)

    stream.take(0).toList shouldBe List()
    stream.take(3).toList shouldBe List(1, 2, 3)
  }

  test("fibsUnfold") {
    val stream = StreamFp.fibUnfold()

    stream.take(0).toList shouldBe List()
    stream.take(5).toList shouldBe List(0, 1, 1, 2, 3)
  }

  test("startsWith") {
    val stream = StreamFp.cons(1, StreamFp.cons(2, StreamFp.cons(3, StreamFp.empty)))

    stream.startsWith(stream) shouldBe true
    stream.startsWith(StreamFp.cons(1, StreamFp.cons(2, StreamFp.empty))) shouldBe true
    stream.startsWith(StreamFp.cons(1, StreamFp.empty)) shouldBe true
    stream.startsWith(StreamFp.empty) shouldBe true
    stream.startsWith(StreamFp.cons(1, StreamFp.cons(2, StreamFp.cons(4, StreamFp.empty)))) shouldBe false
    stream.startsWith(StreamFp.cons(2, StreamFp.empty)) shouldBe false
    stream.startsWith(StreamFp.cons(1, StreamFp.cons(2, StreamFp.cons(3, StreamFp.cons(4, StreamFp.empty))))) shouldBe false
  }

  test("tails") {
    val stream = StreamFp.cons(1, StreamFp.cons(2, StreamFp.cons(3, StreamFp.empty)))

    stream.tails.toList.map(_.toList) shouldBe List(List(1, 2, 3), List(2, 3), List(3), List())
    StreamFp.empty[Int].tails.toList.map(_.toList) shouldBe List(List())
  }

  test("scanRight") {
    val stream = StreamFp.cons(1, StreamFp.cons(2, StreamFp.cons(3, StreamFp.empty)))

    stream.scanRight(0)(_ + _).toList shouldBe List(6, 5, 3, 0)
    StreamFp.empty[Int].scanRight(0)(_ + _).toList shouldBe List(0)
  }

//  test("foldRight") {
//    val sum = StreamFp.fibUnfold().map(_.toString).take(1000).foldRight("")(_ + _)
//
//    sum shouldBe 1000
//  }
}
