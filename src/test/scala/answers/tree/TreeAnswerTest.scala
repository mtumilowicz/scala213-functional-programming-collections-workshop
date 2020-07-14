package answers.tree

class TreeAnswerTest extends org.scalatest.FunSuite with org.scalatest.matchers.should.Matchers {

  test("size") {
    def leaf = BranchAnswer(1)

    def two = BranchAnswer(1, BranchAnswer(2), BranchAnswer(3))

    def tree = BranchAnswer(1,
      BranchAnswer(2,
        BranchAnswer(3, BranchAnswer(4),
          BranchAnswer(5, BranchAnswer(6), BranchAnswer(7))
        ), BranchAnswer(8)
      ), BranchAnswer(9))

    leaf.size() shouldBe 1
    two.size() shouldBe 3
    tree.size() shouldBe 9
  }

  test("sizeFold") {
    def leaf = BranchAnswer(1)

    def two = BranchAnswer(1, BranchAnswer(2), BranchAnswer(3))

    def tree = BranchAnswer(1,
      BranchAnswer(2,
        BranchAnswer(3, BranchAnswer(4),
          BranchAnswer(5, BranchAnswer(6), BranchAnswer(7))
        ), BranchAnswer(8)
      ), BranchAnswer(9))

    leaf.sizeFold() shouldBe 1
    two.sizeFold() shouldBe 3
    tree.sizeFold() shouldBe 9
  }

  test("maxElem") {
    def leaf = BranchAnswer(1)

    def two = BranchAnswer(1, BranchAnswer(2), BranchAnswer(3))

    def tree = BranchAnswer(1,
      BranchAnswer(2,
        BranchAnswer(3, BranchAnswer(4),
          BranchAnswer(5, BranchAnswer(6), BranchAnswer(7))
        ), BranchAnswer(8)
      ), BranchAnswer(9))

    BranchAnswer.maxElem(leaf) shouldBe 1
    BranchAnswer.maxElem(two) shouldBe 3
    BranchAnswer.maxElem(tree) shouldBe 9
  }

  test("maxElemFold") {
    def leaf = BranchAnswer(1)

    def two = BranchAnswer(1, BranchAnswer(2), BranchAnswer(3))

    def tree = BranchAnswer(1,
      BranchAnswer(2,
        BranchAnswer(3, BranchAnswer(4),
          BranchAnswer(5, BranchAnswer(6), BranchAnswer(7))
        ), BranchAnswer(8)
      ), BranchAnswer(9))

    BranchAnswer.maxElemFold(leaf) shouldBe 1
    BranchAnswer.maxElemFold(two) shouldBe 3
    BranchAnswer.maxElemFold(tree) shouldBe 9
  }

  test("depth") {
    def leaf = BranchAnswer(1)

    def two = BranchAnswer(1, BranchAnswer(2), BranchAnswer(3))

    def tree = BranchAnswer(1,
      BranchAnswer(2,
        BranchAnswer(3,
          BranchAnswer(4),
          BranchAnswer(5,
            BranchAnswer(6),
            BranchAnswer(7)
          )
        ), BranchAnswer(8)
      ), BranchAnswer(9))

    def tree2 =
      BranchAnswer(1,
        BranchAnswer(2,
          BranchAnswer(3),
          EmptyTreeAnswer
        ),
        BranchAnswer(6,
          BranchAnswer(7),
          EmptyTreeAnswer
        )
      )

    leaf.depth() shouldBe 1
    two.depth() shouldBe 2
    tree.depth() shouldBe 5
    tree2.depth() shouldBe 3
  }

  test("map") {
    def leaf = BranchAnswer(1)

    def two = BranchAnswer(1, BranchAnswer(2), BranchAnswer(3))

    def tree = BranchAnswer(1,
      BranchAnswer(2,
        BranchAnswer(3,
          BranchAnswer(4),
          BranchAnswer(5,
            BranchAnswer(6),
            BranchAnswer(7)
          )
        ), BranchAnswer(8)
      ), BranchAnswer(9))

    def expectedTree = BranchAnswer("1a",
      BranchAnswer("2a",
        BranchAnswer("3a",
          BranchAnswer("4a"),
          BranchAnswer("5a",
            BranchAnswer("6a"),
            BranchAnswer("7a")
          )
        ), BranchAnswer("8a")
      ), BranchAnswer("9a"))

    def tree2 =
      BranchAnswer(1,
        BranchAnswer(2,
          BranchAnswer(3),
          EmptyTreeAnswer
        ),
        BranchAnswer(4,
          BranchAnswer(5),
          EmptyTreeAnswer
        )
      )

    def expectedTree2 =
      BranchAnswer("1a",
        BranchAnswer("2a",
          BranchAnswer("3a"),
          EmptyTreeAnswer
        ),
        BranchAnswer("4a",
          BranchAnswer("5a"),
          EmptyTreeAnswer
        )
      )

    def mapper: Int => String = i => s"${i}a"

    leaf.map(mapper) shouldBe BranchAnswer("1a")
    two.map(mapper) shouldBe BranchAnswer("1a", BranchAnswer("2a"), BranchAnswer("3a"))
    tree.map(mapper) shouldBe expectedTree
    tree2.map(mapper) shouldBe expectedTree2
  }

  test("fold") {
    def leaf = BranchAnswer(1)

    def two = BranchAnswer(1, BranchAnswer(2), BranchAnswer(3))

    def tree = BranchAnswer(1,
      BranchAnswer(2,
        BranchAnswer(3,
          BranchAnswer(4),
          BranchAnswer(5,
            BranchAnswer(6),
            BranchAnswer(7)
          )
        ), BranchAnswer(8)
      ), BranchAnswer(9))

    leaf.fold("")(_ + _) shouldBe "1"
    two.fold("")(_ + _) shouldBe "321"
    tree.fold("")(_ + _) shouldBe "987654321"
  }
}
