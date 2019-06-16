package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.Players
import io.github.hejcz.helper.TestBasicRemainingTiles
import io.github.hejcz.helper.TestGameSetup
import org.amshove.kluent.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object IncompleteCastleEndGameRewardSpec : Spek({

    describe("Incomplete castle rule") {

        fun singlePlayer(vararg tiles: Tile) = Game(
            Players.singlePlayer(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        )

        it("should give reward for incomplete castle") {
            val game = singlePlayer(TileG)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Knight(Down))) shouldContain PlayerScored(1, 2, emptySet())
        }

        it("should give reward for incomplete castle with emblem") {
            val game = singlePlayer(TileF)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Knight(Down))) shouldContain PlayerScored(1, 3, emptySet())
        }

        it("should not reward knight who just got rewarded for finishing castle") {
            val game = singlePlayer(TileD)
            game.dispatch(PutTile(Position(0, 1), Rotation180))
            val events = game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            events.size shouldBe 1
            events shouldContain PlayerScored(1, 4, setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down))))
        }
    }
})
