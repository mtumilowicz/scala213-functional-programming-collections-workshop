package answers.stream

class StreamAnswerTest extends org.scalatest.FunSuite with org.scalatest.matchers.should.Matchers {

  test("toList") {
    val stream = StreamAnswer(1, 2, 3)

    stream.toList == List(1, 2, 3)
    StreamAnswer.empty.toList shouldBe List()
  }

  test("take") {
    val stream = StreamAnswer(1, 2, 3)

    stream.take(0).toList shouldBe List()
    stream.take(1).toList shouldBe List(1)
    stream.take(3).toList shouldBe List(1, 2, 3)
    stream.take(4).toList shouldBe List(1, 2, 3)
    StreamAnswer.empty.take(1).toList shouldBe List()
  }

  test("drop") {
    val stream = StreamAnswer(1, 2, 3)

    stream.drop(0).toList shouldBe List(1, 2, 3)
    stream.drop(1).toList shouldBe List(2, 3)
    stream.drop(3).toList shouldBe List()
    stream.drop(4).toList shouldBe List()
    StreamAnswer.empty.drop(1).toList shouldBe List()
  }

  test("takeWhile") {
    val stream = StreamAnswer(1, 2, 3)

    stream.takeWhile(_ < -1).toList shouldBe List()
    stream.takeWhile(_ <= 1).toList shouldBe List(1)
    stream.takeWhile(_ <= 3).toList shouldBe List(1, 2, 3)
    stream.takeWhile(_ <= 4).toList shouldBe List(1, 2, 3)
    StreamAnswer(1, 2, 3, 4, 1, 2).takeWhile(_ <= 2).toList shouldBe List(1, 2)
    StreamAnswer.empty[Int].takeWhile(_ < 1).toList shouldBe List()
  }

  test("forAll") {
    val stream = StreamAnswer(1, 2, 3)

    stream.forAll(_ > 0) shouldBe true
    stream.forAll(_ > 3) shouldBe false
    StreamAnswer.empty[Int].forAll(_ > 3) shouldBe true
  }

  test("takeWhileFoldRight") {
    val stream = StreamAnswer(1, 2, 3)

    stream.takeWhileFoldRight(_ < -1).toList shouldBe List()
    stream.takeWhileFoldRight(_ <= 1).toList shouldBe List(1)
    stream.takeWhileFoldRight(_ <= 3).toList shouldBe List(1, 2, 3)
    stream.takeWhileFoldRight(_ <= 4).toList shouldBe List(1, 2, 3)
    StreamAnswer.empty[Int].takeWhile(_ < 1).toList shouldBe List()
  }

  test("headOption") {
    val stream = StreamAnswer(1, 2, 3)
    val infinite = StreamAnswer.constant(1)

    stream.headOption shouldBe Some(1)
    StreamAnswer.empty[Int].headOption shouldBe None
    infinite.headOption shouldBe Some(1)
  }

  test("map") {
    val stream = StreamAnswer(1, 2, 3)

    stream.map(_ + 1).toList shouldBe List(2, 3, 4)
    StreamAnswer.empty[Int].map(_ + 1).toList shouldBe List()
  }

  test("map is lazy") {
    val infinite = StreamAnswer.constant(1)

    infinite.map(_ * 2)
  }

  test("filter") {
    val stream = StreamAnswer(1, 2, 3)

    stream.filter(_ >= 2).toList shouldBe List(2, 3)
    StreamAnswer.empty[Int].filter(_ > 1).toList shouldBe List()
  }

  test("append") {
    val stream = StreamAnswer(1, 2, 3)
    val stream2 = StreamAnswer(4, 5)

    stream.append(stream2).toList == List(1, 2, 3, 4, 5)
    stream2.append(stream).toList == List(4, 5, 1, 2, 3)
    StreamAnswer.empty.append(stream).toList == List(1, 2, 3)
    stream.append(StreamAnswer.empty).toList == List(1, 2, 3)
    StreamAnswer.constant(1).append(StreamAnswer(2)) // terminates
  }

  test("flatMap") {
    val stream = StreamAnswer(1, 2, 3)

    stream.flatMap(i => StreamAnswer.cons(i, StreamAnswer.cons(i, StreamAnswer.empty))).toList shouldBe List(1, 1, 2, 2, 3, 3)
    stream.flatMap(_ => StreamAnswer.empty).toList shouldBe List()
    StreamAnswer.empty[Int].flatMap(_ => StreamAnswer.cons(1, StreamAnswer.empty)).toList == List()
  }

  test("mapUnfold") {
    val stream = StreamAnswer(1, 2, 3)

    stream.mapUnfold(_ + 1).toList shouldBe List(2, 3, 4)
    StreamAnswer.empty[Int].mapUnfold(_ + 1).toList shouldBe List()
  }

  test("constant") {
    val stream = StreamAnswer.constant(1)

    stream.take(3).toList shouldBe List(1, 1, 1)
  }

  test("from") {
    val stream = StreamAnswer.from(1)

    stream.take(0).toList shouldBe List()
    stream.take(3).toList shouldBe List(1, 2, 3)
  }

  test("fibs") {
    val stream = StreamAnswer.fibs()

    stream.take(0).toList shouldBe List()
    stream.take(5).toList shouldBe List(0, 1, 1, 2, 3)
  }

  test("fibsUnfold") {
    val stream = StreamAnswer.fibUnfold()

    stream.take(0).toList shouldBe List()
    stream.take(5).toList shouldBe List(0, 1, 1, 2, 3)
  }

  test("startsWith") {
    val stream = StreamAnswer(1, 2, 3)

    stream.startsWith(stream) shouldBe true
    stream.startsWith(StreamAnswer(1, 2)) shouldBe true
    stream.startsWith(StreamAnswer(1)) shouldBe true
    stream.startsWith(StreamAnswer.empty) shouldBe true
    stream.startsWith(StreamAnswer(1, 2, 4)) shouldBe false
    stream.startsWith(StreamAnswer(2)) shouldBe false
    stream.startsWith(StreamAnswer(1, 2, 3, 4)) shouldBe false
  }

  test("tails") {
    val stream = StreamAnswer(1, 2, 3)

    stream.tails.toList.map(_.toList) shouldBe List(List(1, 2, 3), List(2, 3), List(3), List())
    StreamAnswer.empty[Int].tails.toList.map(_.toList) shouldBe List(List())
  }

  test("scanRight") {
    val stream = StreamAnswer(1, 2, 3)

    stream.scanRight(0)(_ + _).toList shouldBe List(6, 5, 3, 0)
    StreamAnswer.empty[Int].scanRight(0)(_ + _).toList shouldBe List(0)
  }
}
