package io.github.hejcz.core

import io.github.hejcz.core.tile.Tile
import io.github.hejcz.core.tile.TileD
import io.github.hejcz.core.tile.TileF
import io.github.hejcz.core.tile.TileG
import io.github.hejcz.engine.Game
import io.github.hejcz.helper.GameScenario
import io.github.hejcz.helper.Players
import io.github.hejcz.helper.TestBasicRemainingTiles
import io.github.hejcz.helper.TestGameSetup
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object IncompleteCastleEndGameRewardSpec : Spek({

    describe("Incomplete castle rule") {

        fun singlePlayer(vararg tiles: Tile) = Game(
            Players.singlePlayer(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        ).dispatch(BeginCmd)

        it("should give reward for incomplete castle") {
            GameScenario(singlePlayer(TileG))
                .then(TileCmd(Position(0, 1), Rotation90))
                .then(PieceCmd(SmallPiece, Knight(Down)))
                .thenReceivedEventShouldBe(ScoreEvent(1, 2, emptySet()))
        }

        it("should give reward for incomplete castle with emblem") {
            GameScenario(singlePlayer(TileF))
                .then(TileCmd(Position(0, 1), Rotation90))
                .then(PieceCmd(SmallPiece, Knight(Down)))
                .thenReceivedEventShouldBe(ScoreEvent(1, 3, emptySet()))
        }

        it("should not reward knight who just got rewarded for finishing castle") {
            GameScenario(singlePlayer(TileD))
                .then(TileCmd(Position(0, 1), Rotation180))
                .then(PieceCmd(SmallPiece, Knight(Down)))
                .thenReceivedEventShouldBe(
                    ScoreEvent(1, 4, setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down))))
                )
        }
    }
})
