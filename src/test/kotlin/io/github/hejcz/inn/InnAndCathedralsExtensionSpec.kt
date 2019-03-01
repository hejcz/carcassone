package io.github.hejcz.inn

import io.github.hejcz.basic.tiles.*
import io.github.hejcz.core.*
import io.github.hejcz.helper.*
import io.github.hejcz.inn.tiles.*
import org.amshove.kluent.*
import org.spekframework.spek2.*
import org.spekframework.spek2.style.specification.*

object InnAndCathedralsExtensionSpec : Spek({

    fun singlePlayer(vararg tiles: Tile) = Game(
        setOf(Player(id = 1, order = 1, initialPieces = listOf(SmallPiece, BigPiece))),
        InnAndCathedralsTestGameSetup(TestBasicRemainingTiles(*tiles))
    )

    fun multiPlayer(vararg tiles: Tile) = Game(
        setOf(
            Player(id = 1, order = 1, initialPieces = listOf(SmallPiece, SmallPiece, SmallPiece, BigPiece)),
            Player(id = 2, order = 2, initialPieces = listOf(SmallPiece, SmallPiece, SmallPiece, BigPiece))),
        InnAndCathedralsTestGameSetup(TestBasicRemainingTiles(*tiles))
    )

    describe("should score 2 points per castle and emblem in finished castle with cathedral") {
        it("castle with emblem and without cathedral") {
            val game = singlePlayer(TileC, TileD, TileD, TileD, TileD)
            game.dispatch(PutTile(Position(0, 1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(PutTile(Position(1, 1), Rotation270))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, 1), Rotation90)) shouldContain PlayerScored(1, 12, listOf(SmallPiece))
        }

        it("castle without emblem and without cathedral") {
            val game = singlePlayer(TileD, TileD)
            game.dispatch(PutTile(Position(0, 1), Rotation180))
            game.dispatch(PutPiece(SmallPiece, Knight(Down))) shouldContain PlayerScored(1, 4, listOf(SmallPiece))
        }
    }

    describe("should score 3 points per castle and emblem in finished castle with cathedral") {

        it("smallest castle with cathedral") {
            val game = singlePlayer(TileEK, TileD, TileD, TileD, TileD)
            game.dispatch(PutTile(Position(0, 1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(PutTile(Position(1, 1), Rotation270))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, 1), Rotation90)) shouldContain PlayerScored(1, 15, listOf(SmallPiece))
        }

        it("bigger castle with cathedral") {
            val game = singlePlayer(TileEK, TileG, TileE, TileE, TileE, TileE)
            game.dispatch(PutTile(Position(0, 1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(PutTile(Position(1, 1), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(2, 1), Rotation270))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, 1), Rotation90)) shouldContain PlayerScored(1, 18, listOf(SmallPiece))
        }

        it("castle with two cathedral is treated the same as with single cathedral") {
            val game = singlePlayer(TileEK, TileEK, TileE, TileE, TileE, TileE, TileE, TileE)
            game.dispatch(PutTile(Position(0, 1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(PutTile(Position(0, 2), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 1), Rotation270))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 2), Rotation270))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, 3), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, 1), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, 2), Rotation90)) shouldContain PlayerScored(1, 24, listOf(SmallPiece))
        }

        it("castle with cathedral and emblem") {
            val game = singlePlayer(TileEK, TileC, TileE, TileE, TileE, TileE, TileE, TileE)
            game.dispatch(PutTile(Position(0, 1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(PutTile(Position(0, 2), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 1), Rotation270))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 2), Rotation270))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, 3), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, 1), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, 2), Rotation90)) shouldContain PlayerScored(1, 27, listOf(SmallPiece))
        }

        it("castle with cathedral and multiple emblem") {
            val game = singlePlayer(TileEK, TileC, TileF, TileE, TileE, TileE, TileE, TileE, TileE)
            game.dispatch(PutTile(Position(0, 1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(PutTile(Position(0, 2), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 1), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(2, 1), Rotation270))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 2), Rotation270))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, 3), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, 1), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, 2), Rotation90)) shouldContain PlayerScored(1, 33, listOf(SmallPiece))
        }

    }

    describe("should score 0 points per unfinished castle with cathedral") {

        it("small castle with cathedral") {
            val game = singlePlayer(TileEK, TileD, TileD)
            game.dispatch(PutTile(Position(0, 1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(PutTile(Position(1, 1), Rotation270))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(SkipPiece) shouldEqual emptyList()
        }

        it("bigger castle with cathedral") {
            val game = singlePlayer(TileEK, TileG, TileE, TileE)
            game.dispatch(PutTile(Position(0, 1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(PutTile(Position(1, 1), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, 1), Rotation90))
            game.dispatch(SkipPiece)  shouldEqual emptyList()
        }

    }

    describe("should score 1 points per unfinished castle without cathedral") {

        it("small castle without cathedral") {
            val game = singlePlayer(TileC, TileD, TileD)
            game.dispatch(PutTile(Position(0, 1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(PutTile(Position(1, 1), Rotation270))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(SkipPiece) shouldContain PlayerScored(1, 5, emptySet())
        }

        it("bigger castle without cathedral") {
            val game = singlePlayer(TileC, TileF, TileE, TileE)
            game.dispatch(PutTile(Position(0, 1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(PutTile(Position(1, 1), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, 1), Rotation90))
            game.dispatch(SkipPiece)  shouldContain PlayerScored(1, 7, emptySet())
        }

    }

    describe("big piece") {

        it("single should be available") {
            val game = singlePlayer(TileD, TileD)
            game.dispatch(PutTile(Position(1, 0), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(BigPiece, Knight(Up))).shouldContainPlaceTileOnly()
        }

        it("can not be placed twice before recover") {
            val game = singlePlayer(TileD, TileD, TileD)
            game.dispatch(PutTile(Position(1, 0), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(BigPiece, Knight(Up))).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(2, 0), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(BigPiece, Knight(Up))) shouldContain NoMappleAvailable(BigPiece)
        }

        it("wins with small piece") {
            val game = multiPlayer(TileD, TileM, TileM)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(BigPiece, Knight(Up)))
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            val events = game.dispatch(PutTile(Position(1, 1), Rotation180))
            events shouldContain PlayerScored(1, 12, listOf(BigPiece))
            events shouldNotContain PlayerScored(2, 12, listOf(SmallPiece))
            events shouldContain OccupiedAreaCompleted(2, listOf(SmallPiece))
        }

        it("draws with 2 small pieces") {
            val game = multiPlayer(TileD, TileR, TileD, TileM, TileM)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Up)))
            game.dispatch(PutTile(Position(0, 1), Rotation180))
            game.dispatch(PutPiece(BigPiece, Knight(Down)))
            game.dispatch(PutTile(Position(-1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Up)))
            game.dispatch(PutTile(Position(1, 1), Rotation180))
            game.dispatch(SkipPiece)
            val events = game.dispatch(PutTile(Position(-1, 1), Rotation90))
            events shouldContain PlayerScored(1, 16, listOf(SmallPiece, SmallPiece))
            events shouldContain PlayerScored(2, 16, listOf(BigPiece))
        }

    }

    describe("should score 2 points for road tile if there is one inn near road") {

        it ("small road") {
            val game = singlePlayer(TileEC, TileEF)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Brigand(Left)))
            val events = game.dispatch(PutTile(Position(-1, 0), NoRotation))
            events shouldContain PlayerScored(1, 6, listOf(SmallPiece))
        }

    }

    describe("should score 1 points for road tile if there is no inn near road") {

        it("small road") {
            val game = singlePlayer(TileW, TileEF)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Brigand(Left)))
            val events = game.dispatch(PutTile(Position(-1, 0), NoRotation))
            events shouldContain PlayerScored(1, 3, listOf(SmallPiece))
        }

    }

    describe("should score 0 points for road tile if there is one inn near incomplete road") {

        it ("small road") {
            val game = singlePlayer(TileEC)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            val events = game.dispatch(PutPiece(SmallPiece, Brigand(Left)))
            events shouldEqual emptyList()
        }

    }

    describe("should score 1 points for road tile if there is no inn near incomplete road") {

        it ("small road") {
            val game = singlePlayer(TileW)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            val events = game.dispatch(PutPiece(SmallPiece, Brigand(Left)))
            events shouldContain PlayerScored(1, 2, emptySet())
        }

    }

})
