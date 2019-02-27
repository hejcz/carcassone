package io.github.hejcz.basic

import io.github.hejcz.*
import io.github.hejcz.engine.Game
import io.github.hejcz.helper.Players
import io.github.hejcz.helper.TestBasicRemainingTiles
import io.github.hejcz.helper.TestGameSetup
import io.github.hejcz.helper.shouldContainSelectPieceOnly
import io.github.hejcz.mapples.Knight
import io.github.hejcz.mapples.Mapple
import io.github.hejcz.placement.*
import io.github.hejcz.rules.basic.CastleCompletedRule
import io.github.hejcz.tiles.basic.*
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object CompletedCastleDetectionSpec : Spek({

    describe("A castle detector") {

        fun singlePlayer(vararg tiles: Tile) = Game(
            setOf(CastleCompletedRule),
            emptySet(),
            Players.singlePlayer(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        )

        fun multiPlayer(vararg tiles: Tile) = Game(
            setOf(CastleCompletedRule),
            emptySet(),
            Players.twoPlayers(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        )

        it("should not detect incomplete castle with knight") {
            val game = singlePlayer(TileD)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Knight(Up))) shouldEqual emptyList()
        }

        it("should not detect incomplete castle without knight") {
            val game = singlePlayer(TileD)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(SkipPiece) shouldEqual emptyList()
        }

        it("should detect XS completed castle") {
            val game = singlePlayer(TileD)
            game.dispatch(PutTile(Position(0, 1), Rotation180))
            game.dispatch(PutPiece(Mapple, Knight(Down))) shouldContain PlayerScored(1, 4, listOf(Mapple))
        }

        it("should detect S completed castle") {
            val game = singlePlayer(TileG, TileD)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(PutPiece(Mapple, Knight(Down))) shouldContain PlayerScored(1, 6, listOf(Mapple))
        }

        it("should not detect S incomplete castle") {
            val game = singlePlayer(TileR, TileD)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(PutPiece(Mapple, Knight(Down))) shouldEqual emptyList()
        }

        it("should detect M incomplete castle") {
            val game = singlePlayer(TileR, TileD, TileD)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 1), Rotation270))
            game.dispatch(PutPiece(Mapple, Knight(Left))) shouldContain PlayerScored(1, 8, listOf(Mapple))
        }

        it("should detect mapples placed before castle completion") {
            val game = singlePlayer(TileR, TileD, TileD)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(Mapple, Knight(Down)))
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 1), Rotation270)) shouldContain PlayerScored(1, 8, listOf(Mapple))
        }

        it("should return all mapples placed on completed castle") {
            val game = singlePlayer(TileR, TileD, TileD, TileN)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(Mapple, Knight(Down)))
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 2), Rotation180))
            game.dispatch(PutPiece(Mapple, Knight(Down)))
            game.dispatch(PutTile(Position(1, 1), Rotation270)) shouldContain PlayerScored(1, 10, listOf(Mapple, Mapple))
        }

        it("should not give any reward if no pieces were on completed castle") {
            val game = singlePlayer(TileR, TileD, TileD, TileN)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 2), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 1), Rotation270)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece) shouldEqual emptyList()
        }

        it("should modify score if emblems are available on castle") {
            val game = singlePlayer(TileF, TileM, TileD)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(Mapple, Knight(Down)))
            game.dispatch(PutTile(Position(0, 2), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 2), Rotation270)) shouldContain PlayerScored(1, 12, listOf(Mapple))
        }

        it("should reward many players if they have the same amount of pieces in a castle") {
            val game = multiPlayer(TileR, TileD, TileA, TileD, TileN)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(Mapple, Knight(Down)))
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 0), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 2), Rotation180))
            game.dispatch(PutPiece(Mapple, Knight(Down)))
            val events = game.dispatch(PutTile(Position(1, 1), Rotation270))
            events shouldContain PlayerScored(1, 10, listOf(Mapple))
            events shouldContain PlayerScored(2, 10, listOf(Mapple))
        }

        it("should reward only one player if he has advantage of pieces in castle") {
            val game = multiPlayer(TileR, TileD, TileD, TileD, TileR)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(Mapple, Knight(Down)))
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Knight(Up)))
            game.dispatch(PutTile(Position(1, 2), Rotation180))
            game.dispatch(PutPiece(Mapple, Knight(Down)))
            val events = game.dispatch(PutTile(Position(1, 1), Rotation270))
            events shouldContain PlayerScored(1, 12, listOf(Mapple, Mapple))
            events shouldContain OccupiedAreaCompleted(2, listOf(Mapple))
        }

        it("should detect that multiple castles were finished with single tile") {
            val game = multiPlayer(TileN, TileD, TileI)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(Mapple, Knight(Down)))
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Knight(Up)))
            val events = game.dispatch(PutTile(Position(1, 1), Rotation270))
            events shouldContain PlayerScored(1, 6, listOf(Mapple))
            events shouldContain PlayerScored(2, 4, listOf(Mapple))
        }

    }

})
