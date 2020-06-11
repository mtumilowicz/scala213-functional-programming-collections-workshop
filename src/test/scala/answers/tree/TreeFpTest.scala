package answers.tree

class TreeFpTest extends org.scalatest.FunSuite with org.scalatest.matchers.should.Matchers {

  test("size") {
    def leaf = BranchFp(1)

    def two = BranchFp(1, BranchFp(2), BranchFp(3))

    def tree = BranchFp(1,
      BranchFp(2,
        BranchFp(3, BranchFp(4),
          BranchFp(5, BranchFp(6), BranchFp(7))
        ), BranchFp(8)
      ), BranchFp(9))

    leaf.size() shouldBe 1
    two.size() shouldBe 3
    tree.size() shouldBe 9
  }

  test("sizeFold") {
    def leaf = BranchFp(1)

    def two = BranchFp(1, BranchFp(2), BranchFp(3))

    def tree = BranchFp(1,
      BranchFp(2,
        BranchFp(3, BranchFp(4),
          BranchFp(5, BranchFp(6), BranchFp(7))
        ), BranchFp(8)
      ), BranchFp(9))

    leaf.sizeFold() shouldBe 1
    two.sizeFold() shouldBe 3
    tree.sizeFold() shouldBe 9
  }

  test("maxElem") {
    def leaf = BranchFp(1)

    def two = BranchFp(1, BranchFp(2), BranchFp(3))

    def tree = BranchFp(1,
      BranchFp(2,
        BranchFp(3, BranchFp(4),
          BranchFp(5, BranchFp(6), BranchFp(7))
        ), BranchFp(8)
      ), BranchFp(9))

    BranchFp.maxElem(leaf) shouldBe 1
    BranchFp.maxElem(two) shouldBe 3
    BranchFp.maxElem(tree) shouldBe 9
  }

  test("maxElemFold") {
    def leaf = BranchFp(1)

    def two = BranchFp(1, BranchFp(2), BranchFp(3))

    def tree = BranchFp(1,
      BranchFp(2,
        BranchFp(3, BranchFp(4),
          BranchFp(5, BranchFp(6), BranchFp(7))
        ), BranchFp(8)
      ), BranchFp(9))

    BranchFp.maxElemFold(leaf) shouldBe 1
    BranchFp.maxElemFold(two) shouldBe 3
    BranchFp.maxElemFold(tree) shouldBe 9
  }

  test("depth") {
    def leaf = BranchFp(1)

    def two = BranchFp(1, BranchFp(2), BranchFp(3))

    def tree = BranchFp(1,
      BranchFp(2,
        BranchFp(3,
          BranchFp(4),
          BranchFp(5,
            BranchFp(6),
            BranchFp(7)
          )
        ), BranchFp(8)
      ), BranchFp(9))

    def tree2 =
      BranchFp(1,
        BranchFp(2,
          BranchFp(3),
          EmptyTree
        ),
        BranchFp(6,
          BranchFp(7),
          EmptyTree
        )
      )

    leaf.depth() shouldBe 1
    two.depth() shouldBe 2
    tree.depth() shouldBe 5
    tree2.depth() shouldBe 3
  }

  test("map") {
    def leaf = BranchFp(1)

    def two = BranchFp(1, BranchFp(2), BranchFp(3))

    def tree = BranchFp(1,
      BranchFp(2,
        BranchFp(3,
          BranchFp(4),
          BranchFp(5,
            BranchFp(6),
            BranchFp(7)
          )
        ), BranchFp(8)
      ), BranchFp(9))

    def expectedTree = BranchFp("1a",
      BranchFp("2a",
        BranchFp("3a",
          BranchFp("4a"),
          BranchFp("5a",
            BranchFp("6a"),
            BranchFp("7a")
          )
        ), BranchFp("8a")
      ), BranchFp("9a"))

    def tree2 =
      BranchFp(1,
        BranchFp(2,
          BranchFp(3),
          EmptyTree
        ),
        BranchFp(4,
          BranchFp(5),
          EmptyTree
        )
      )

    def expectedTree2 =
      BranchFp("1a",
        BranchFp("2a",
          BranchFp("3a"),
          EmptyTree
        ),
        BranchFp("4a",
          BranchFp("5a"),
          EmptyTree
        )
      )

    def mapper: Int => String = i => s"${i}a"

    leaf.map(mapper) shouldBe BranchFp("1a")
    two.map(mapper) shouldBe BranchFp("1a", BranchFp("2a"), BranchFp("3a"))
    tree.map(mapper) shouldBe expectedTree
    tree2.map(mapper) shouldBe expectedTree2
  }

  test("fold") {
    def leaf = BranchFp(1)

    def two = BranchFp(1, BranchFp(2), BranchFp(3))

    def tree = BranchFp(1,
      BranchFp(2,
        BranchFp(3,
          BranchFp(4),
          BranchFp(5,
            BranchFp(6),
            BranchFp(7)
          )
        ), BranchFp(8)
      ), BranchFp(9))

    leaf.fold("")(_ + _) shouldBe "1"
    two.fold("")(_ + _) shouldBe "321"
    tree.fold("")(_ + _) shouldBe "987654321"
  }
}
