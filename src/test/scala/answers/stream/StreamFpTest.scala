package answers.stream

class StreamFpTest extends org.scalatest.FunSuite with org.scalatest.matchers.should.Matchers {

  test("toList") {
    val stream = StreamFp(1, 2, 3)

    stream.toList == List(1, 2, 3)
    StreamFp.empty.toList shouldBe List()
  }

  test("take") {
    val stream = StreamFp(1, 2, 3)

    stream.take(0).toList shouldBe List()
    stream.take(1).toList shouldBe List(1)
    stream.take(3).toList shouldBe List(1, 2, 3)
    stream.take(4).toList shouldBe List(1, 2, 3)
    StreamFp.empty.take(1).toList shouldBe List()
  }

  test("drop") {
    val stream = StreamFp(1, 2, 3)

    stream.drop(0).toList shouldBe List(1, 2, 3)
    stream.drop(1).toList shouldBe List(2, 3)
    stream.drop(3).toList shouldBe List()
    stream.drop(4).toList shouldBe List()
    StreamFp.empty.drop(1).toList shouldBe List()
  }

  test("takeWhile") {
    val stream = StreamFp(1, 2, 3)

    stream.takeWhile(_ < -1).toList shouldBe List()
    stream.takeWhile(_ <= 1).toList shouldBe List(1)
    stream.takeWhile(_ <= 3).toList shouldBe List(1, 2, 3)
    stream.takeWhile(_ <= 4).toList shouldBe List(1, 2, 3)
    StreamFp.empty[Int].takeWhile(_ < 1).toList shouldBe List()
  }

  test("forAll") {
    val stream = StreamFp(1, 2, 3)

    stream.forAll(_ > 0) shouldBe true
    stream.forAll(_ > 3) shouldBe false
    StreamFp.empty[Int].forAll(_ > 3) shouldBe true
  }

  test("takeWhileFold") {
    val stream = StreamFp(1, 2, 3)

    stream.takeWhileFold(_ < -1).toList shouldBe List()
    stream.takeWhileFold(_ <= 1).toList shouldBe List(1)
    stream.takeWhileFold(_ <= 3).toList shouldBe List(1, 2, 3)
    stream.takeWhileFold(_ <= 4).toList shouldBe List(1, 2, 3)
    StreamFp.empty[Int].takeWhile(_ < 1).toList shouldBe List()
  }

  test("headOption") {
    val stream = StreamFp(1, 2, 3)
    val infinite = StreamFp.constant(1)

    stream.headOption shouldBe Some(1)
    StreamFp.empty[Int].headOption shouldBe None
    infinite.headOption shouldBe Some(1)
  }

  test("map") {
    val stream = StreamFp(1, 2, 3)

    stream.map(_ + 1).toList shouldBe List(2, 3, 4)
    StreamFp.empty[Int].map(_ + 1).toList shouldBe List()
  }

  test("map is lazy") {
    val infinite = StreamFp.constant(1)

    infinite.map(_ * 2)
  }

  test("filter") {
    val stream = StreamFp(1, 2, 3)

    stream.filter(_ >= 2).toList shouldBe List(2, 3)
    StreamFp.empty[Int].filter(_ > 1).toList shouldBe List()
  }

  test("append") {
    val stream = StreamFp(1, 2, 3)
    val stream2 = StreamFp(4, 5)

    stream.append(stream2).toList == List(1, 2, 3, 4, 5)
    stream2.append(stream).toList == List(4, 5, 1, 2, 3)
    StreamFp.empty.append(stream).toList == List(1, 2, 3)
    stream.append(StreamFp.empty).toList == List(1, 2, 3)
  }

  test("flatMap") {
    val stream = StreamFp(1, 2, 3)

    stream.flatMap(i => StreamFp.cons(i, StreamFp.cons(i, StreamFp.empty))).toList shouldBe List(1, 1, 2, 2, 3, 3)
    stream.flatMap(_ => StreamFp.empty).toList shouldBe List()
    StreamFp.empty[Int].flatMap(_ => StreamFp.cons(1, StreamFp.empty)).toList == List()
  }

  test("mapUnfold") {
    val stream = StreamFp(1, 2, 3)

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

  test("fibsUnfold") {
    val stream = StreamFp.fibUnfold()

    stream.take(0).toList shouldBe List()
    stream.take(5).toList shouldBe List(0, 1, 1, 2, 3)
  }

  test("startsWith") {
    val stream = StreamFp(1, 2, 3)

    stream.startsWith(stream) shouldBe true
    stream.startsWith(StreamFp(1, 2)) shouldBe true
    stream.startsWith(StreamFp(1)) shouldBe true
    stream.startsWith(StreamFp.empty) shouldBe true
    stream.startsWith(StreamFp(1, 2, 4)) shouldBe false
    stream.startsWith(StreamFp(2)) shouldBe false
    stream.startsWith(StreamFp(1, 2, 3, 4)) shouldBe false
  }

  test("tails") {
    val stream = StreamFp(1, 2, 3)

    stream.tails.toList.map(_.toList) shouldBe List(List(1, 2, 3), List(2, 3), List(3), List())
    StreamFp.empty[Int].tails.toList.map(_.toList) shouldBe List(List())
  }

  test("scanRight") {
    val stream = StreamFp(1, 2, 3)

    stream.scanRight(0)(_ + _).toList shouldBe List(6, 5, 3, 0)
    StreamFp.empty[Int].scanRight(0)(_ + _).toList shouldBe List(0)
  }
}
