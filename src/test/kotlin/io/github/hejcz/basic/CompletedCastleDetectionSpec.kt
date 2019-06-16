package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.*
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object CompletedCastleDetectionSpec : Spek({

    describe("A castle detector") {

        fun singlePlayer(vararg tiles: Tile) = Game(
            Players.singlePlayer(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        )

        fun multiPlayer(vararg tiles: Tile) = Game(
            Players.twoPlayers(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        )

        it("should not detect incomplete castle with knight") {
            val game = singlePlayer(TileD, TileB)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Up))).shouldContainPlaceTileOnly()
        }

        it("should not detect incomplete castle without knight") {
            val game = singlePlayer(TileD, TileB)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
        }

        it("should detect XS completed castle") {
            val game = singlePlayer(TileD)
            game.dispatch(PutTile(Position(0, 1), Rotation180))
            game.dispatch(PutPiece(SmallPiece, Knight(Down))) shouldContain PlayerScored(1, 4, setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down))))
        }

        it("should detect S completed castle") {
            val game = singlePlayer(TileG, TileD)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(PutPiece(SmallPiece, Knight(Down))) shouldContain PlayerScored(1, 6, setOf(PieceOnBoard(Position(0, 2), SmallPiece, Knight(Down))))
        }

        it("should not detect S incomplete castle") {
            val game = singlePlayer(TileR, TileD, TileD)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(PutPiece(SmallPiece, Knight(Down))).shouldContainPlaceTileOnly()
        }

        it("should detect M incomplete castle") {
            val game = singlePlayer(TileR, TileD, TileD)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 1), Rotation270))
            game.dispatch(PutPiece(SmallPiece, Knight(Left))) shouldContain PlayerScored(1, 8, setOf(PieceOnBoard(Position(1, 1), SmallPiece, Knight(Left))))
        }

        it("should detect mapples placed before castle completion") {
            val game = singlePlayer(TileR, TileD, TileD)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 1), Rotation270)) shouldContain PlayerScored(1, 8, setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down))))
        }

        it("should return all mapples placed on completed castle") {
            val game = singlePlayer(TileR, TileD, TileD, TileN)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 2), Rotation180))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(PutTile(Position(1, 1), Rotation270)) shouldContain PlayerScored(1, 10, setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down)), PieceOnBoard(Position(1, 2), SmallPiece, Knight(Down))))
        }

        it("should not give any reward if no handlers were on completed castle") {
            val game = singlePlayer(TileR, TileD, TileD, TileN, TileD)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 2), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 1), Rotation270)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
        }

        it("should modify score if emblems are available on castle") {
            val game = singlePlayer(TileF, TileM, TileD)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(PutTile(Position(0, 2), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 2), Rotation270)) shouldContain PlayerScored(1, 12, setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down))))
        }

        it("should reward many players if they have the same amount of handlers in a castle") {
            val game = multiPlayer(TileR, TileD, TileA, TileD, TileN)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 0), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 2), Rotation180))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            val events = game.dispatch(PutTile(Position(1, 1), Rotation270))
            events shouldContain PlayerScored(1, 10, setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down))))
            events shouldContain PlayerScored(2, 10, setOf(PieceOnBoard(Position(1, 2), SmallPiece, Knight(Down))))
        }

        it("should reward only one player if he has advantage of handlers in castle") {
            val game = multiPlayer(TileR, TileD, TileD, TileD, TileR)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Up)))
            game.dispatch(PutTile(Position(1, 2), Rotation180))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            val events = game.dispatch(PutTile(Position(1, 1), Rotation270))
            events shouldContain PlayerScored(1, 12, setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down)), PieceOnBoard(Position(1, 0), SmallPiece, Knight(Up))))
            events shouldContain OccupiedAreaCompleted(2, setOf(PieceOnBoard(Position(1, 2), SmallPiece, Knight(Down))))
        }

        it("should detect that multiple castles were finished with single tile") {
            val game = multiPlayer(TileN, TileD, TileI)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Up)))
            val events = game.dispatch(PutTile(Position(1, 1), Rotation270))
            events shouldContain PlayerScored(1, 6, setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down))))
            events shouldContain PlayerScored(2, 4, setOf(PieceOnBoard(Position(1, 0), SmallPiece, Knight(Up))))
        }

        it("should return single score event if the same castle is on multiple tiles directions") {
            val game = singlePlayer(TileD, TileN, TileN)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Up)))
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(SkipPiece)
            val events = game.dispatch(PutTile(Position(1, 1), Rotation180))
            events.size shouldEqual 2
            events shouldContain PlayerScored(1, 8, setOf(PieceOnBoard(Position(1, 0), SmallPiece, Knight(Up))))
            events shouldContain SelectPiece
        }
    }
})
