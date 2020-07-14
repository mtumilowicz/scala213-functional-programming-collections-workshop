package workshop.tree

import org.scalatest.Ignore

@Ignore
class TreeWorkshopTest extends org.scalatest.FunSuite with org.scalatest.matchers.should.Matchers {

  test("size") {
    def leaf = BranchWorkshop(1)

    def two = BranchWorkshop(1, BranchWorkshop(2), BranchWorkshop(3))

    def tree = BranchWorkshop(1,
      BranchWorkshop(2,
        BranchWorkshop(3, BranchWorkshop(4),
          BranchWorkshop(5, BranchWorkshop(6), BranchWorkshop(7))
        ), BranchWorkshop(8)
      ), BranchWorkshop(9))

    leaf.size() shouldBe 1
    two.size() shouldBe 3
    tree.size() shouldBe 9
  }

  test("sizeFold") {
    def leaf = BranchWorkshop(1)

    def two = BranchWorkshop(1, BranchWorkshop(2), BranchWorkshop(3))

    def tree = BranchWorkshop(1,
      BranchWorkshop(2,
        BranchWorkshop(3, BranchWorkshop(4),
          BranchWorkshop(5, BranchWorkshop(6), BranchWorkshop(7))
        ), BranchWorkshop(8)
      ), BranchWorkshop(9))

    leaf.sizeFold() shouldBe 1
    two.sizeFold() shouldBe 3
    tree.sizeFold() shouldBe 9
  }

  test("maxElem") {
    def leaf = BranchWorkshop(1)

    def two = BranchWorkshop(1, BranchWorkshop(2), BranchWorkshop(3))

    def tree = BranchWorkshop(1,
      BranchWorkshop(2,
        BranchWorkshop(3, BranchWorkshop(4),
          BranchWorkshop(5, BranchWorkshop(6), BranchWorkshop(7))
        ), BranchWorkshop(8)
      ), BranchWorkshop(9))

    BranchWorkshop.maxElem(leaf) shouldBe 1
    BranchWorkshop.maxElem(two) shouldBe 3
    BranchWorkshop.maxElem(tree) shouldBe 9
  }

  test("maxElemFold") {
    def leaf = BranchWorkshop(1)

    def two = BranchWorkshop(1, BranchWorkshop(2), BranchWorkshop(3))

    def tree = BranchWorkshop(1,
      BranchWorkshop(2,
        BranchWorkshop(3, BranchWorkshop(4),
          BranchWorkshop(5, BranchWorkshop(6), BranchWorkshop(7))
        ), BranchWorkshop(8)
      ), BranchWorkshop(9))

    BranchWorkshop.maxElemFold(leaf) shouldBe 1
    BranchWorkshop.maxElemFold(two) shouldBe 3
    BranchWorkshop.maxElemFold(tree) shouldBe 9
  }

  test("depth") {
    def leaf = BranchWorkshop(1)

    def two = BranchWorkshop(1, BranchWorkshop(2), BranchWorkshop(3))

    def tree = BranchWorkshop(1,
      BranchWorkshop(2,
        BranchWorkshop(3,
          BranchWorkshop(4),
          BranchWorkshop(5,
            BranchWorkshop(6),
            BranchWorkshop(7)
          )
        ), BranchWorkshop(8)
      ), BranchWorkshop(9))

    def tree2 =
      BranchWorkshop(1,
        BranchWorkshop(2,
          BranchWorkshop(3),
          EmptyTreeWorkshop
        ),
        BranchWorkshop(6,
          BranchWorkshop(7),
          EmptyTreeWorkshop
        )
      )

    leaf.depth() shouldBe 1
    two.depth() shouldBe 2
    tree.depth() shouldBe 5
    tree2.depth() shouldBe 3
  }

  test("map") {
    def leaf = BranchWorkshop(1)

    def two = BranchWorkshop(1, BranchWorkshop(2), BranchWorkshop(3))

    def tree = BranchWorkshop(1,
      BranchWorkshop(2,
        BranchWorkshop(3,
          BranchWorkshop(4),
          BranchWorkshop(5,
            BranchWorkshop(6),
            BranchWorkshop(7)
          )
        ), BranchWorkshop(8)
      ), BranchWorkshop(9))

    def expectedTree = BranchWorkshop("1a",
      BranchWorkshop("2a",
        BranchWorkshop("3a",
          BranchWorkshop("4a"),
          BranchWorkshop("5a",
            BranchWorkshop("6a"),
            BranchWorkshop("7a")
          )
        ), BranchWorkshop("8a")
      ), BranchWorkshop("9a"))

    def tree2 =
      BranchWorkshop(1,
        BranchWorkshop(2,
          BranchWorkshop(3),
          EmptyTreeWorkshop
        ),
        BranchWorkshop(4,
          BranchWorkshop(5),
          EmptyTreeWorkshop
        )
      )

    def expectedTree2 =
      BranchWorkshop("1a",
        BranchWorkshop("2a",
          BranchWorkshop("3a"),
          EmptyTreeWorkshop
        ),
        BranchWorkshop("4a",
          BranchWorkshop("5a"),
          EmptyTreeWorkshop
        )
      )

    def mapper: Int => String = i => s"${i}a"

    leaf.map(mapper) shouldBe BranchWorkshop("1a")
    two.map(mapper) shouldBe BranchWorkshop("1a", BranchWorkshop("2a"), BranchWorkshop("3a"))
    tree.map(mapper) shouldBe expectedTree
    tree2.map(mapper) shouldBe expectedTree2
  }

  test("fold") {
    def leaf = BranchWorkshop(1)

    def two = BranchWorkshop(1, BranchWorkshop(2), BranchWorkshop(3))

    def tree = BranchWorkshop(1,
      BranchWorkshop(2,
        BranchWorkshop(3,
          BranchWorkshop(4),
          BranchWorkshop(5,
            BranchWorkshop(6),
            BranchWorkshop(7)
          )
        ), BranchWorkshop(8)
      ), BranchWorkshop(9))

    leaf.fold("")(_ + _) shouldBe "1"
    two.fold("")(_ + _) shouldBe "321"
    tree.fold("")(_ + _) shouldBe "987654321"
  }
}
