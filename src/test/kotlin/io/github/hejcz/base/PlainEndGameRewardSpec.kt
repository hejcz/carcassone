package io.github.hejcz.base

import io.github.hejcz.api.*
import io.github.hejcz.base.tile.*
import io.github.hejcz.engine.Game
import io.github.hejcz.helper.GameScenario
import io.github.hejcz.helper.Players
import io.github.hejcz.helper.TestBasicRemainingTiles
import io.github.hejcz.helper.TestGameSetup
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object PlainEndGameRewardSpec : Spek({

    describe("Green field") {

        fun singlePlayer(vararg tiles: BasicTile) =
            Game(
                Players.singlePlayer(), TestGameSetup(TestBasicRemainingTiles(*tiles)), true
            ).dispatch(BeginCmd)

        fun multiPlayer(vararg tiles: BasicTile) =
            Game(
                Players.twoPlayers(), TestGameSetup(TestBasicRemainingTiles(*tiles)), true
            ).dispatch(BeginCmd)

        it("should be scored") {
            GameScenario(singlePlayer(TileE))
                .then(TileCmd(Position(0, 1), Rotation180))
                .then(PieceCmd(SmallPiece, Peasant(
                    Location(Left)
                )))
                .thenReceivedEventShouldBe(ScoreEvent(1, 3, emptySet()))
        }

        it("Simple case without scoring") {
            GameScenario(singlePlayer(TileE))
                .then(TileCmd(Position(0, 1), Rotation180))
                .then(SkipPieceCmd)
                .thenNoEventsShouldBePublished()
        }

        it("Scoring for two castles") {
            GameScenario(singlePlayer(TileI, TileE))
                .then(TileCmd(Position(0, 1), Rotation270))
                .then(PieceCmd(SmallPiece, Peasant(
                    Location(Right)
                )))
                .then(TileCmd(Position(-1, 1), Rotation90))
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBe(ScoreEvent(1, 6, emptySet()))
        }

        it("Wrong side of road") {
            GameScenario(singlePlayer(TileJ))
                .then(TileCmd(Position(0, 1), Rotation180))
                .then(PieceCmd(SmallPiece, Peasant(
                    Location(
                        Up, LeftSide
                    )
                )))
                .thenNoEventsShouldBePublished()
        }

        it("Right side of road") {
            GameScenario(singlePlayer(TileJ))
                .then(TileCmd(Position(0, 1), Rotation180))
                .then(PieceCmd(SmallPiece, Peasant(
                    Location(
                        Up, RightSide
                    )
                )))
                .thenReceivedEventShouldBe(ScoreEvent(1, 3, emptySet()))
        }

        it("Multiple players disconnected green fields") {
            GameScenario(multiPlayer(TileF, TileE))
                .then(TileCmd(Position(0, 1), Rotation90))
                .then(PieceCmd(SmallPiece, Peasant(
                    Location(Right)
                )))
                .then(TileCmd(Position(0, 2), Rotation180))
                .then(PieceCmd(SmallPiece, Peasant(
                    Location(Right)
                )))
                .thenReceivedEventShouldBe(ScoreEvent(1, 3, emptySet()))
                .thenReceivedEventShouldBe(ScoreEvent(2, 3, emptySet()))
        }

        it("Multiple players dominance of one") {
            GameScenario(multiPlayer(TileF, TileF, TileE, TileA, TileA, TileA))
                .then(TileCmd(Position(0, 1), Rotation90))
                .then(PieceCmd(SmallPiece, Peasant(
                    Location(Right)
                )))
                .then(TileCmd(Position(0, 2), Rotation90))
                .then(PieceCmd(SmallPiece, Peasant(
                    Location(Right)
                )))
                .then(TileCmd(Position(0, 3), Rotation180))
                .then(PieceCmd(SmallPiece, Peasant(
                    Location(Right)
                )))
                .then(TileCmd(Position(1, 1), Rotation270))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 2), Rotation270))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 3), Rotation270))
                .then(SkipPieceCmd)
                .thenShouldNotReceiveEvent(ScoreEvent(2, 3, emptySet()))
                .thenReceivedEventShouldBe(ScoreEvent(1, 3, emptySet()))
        }
    }
})
