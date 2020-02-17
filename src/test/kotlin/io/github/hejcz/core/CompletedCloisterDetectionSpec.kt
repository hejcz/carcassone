package io.github.hejcz.core

import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.GameScenario
import io.github.hejcz.helper.Players
import io.github.hejcz.helper.TestBasicRemainingTiles
import io.github.hejcz.helper.TestGameSetup
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object CompletedCloisterDetectionSpec : Spek({

    describe("A cloister detector") {

        fun singlePlayer(vararg tiles: Tile) = Game(
            Players.singlePlayer(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        ).dispatch(BeginCmd)

        fun multiPlayer(vararg tiles: Tile) = Game(
            Players.twoPlayers(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        ).dispatch(BeginCmd)

        it("should detect completed cloister") {
            GameScenario(singlePlayer(TileD, TileD, TileB, TileB, TileB, TileB, TileB, TileB))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 0), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, -1), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, -1), NoRotation))
                .then(PieceCmd(SmallPiece, Monk))
                .then(TileCmd(Position(-1, -1), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, -2), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, -2), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, -2), NoRotation))
                .thenReceivedEventShouldBe(ScoreEvent(1, 9, setOf(PieceOnBoard(Position(0, -1), SmallPiece, Monk))))
        }

        it("should detect completed cloister filling the hole") {
            GameScenario(singlePlayer(TileD, TileD, TileB, TileB, TileB, TileB, TileB, TileB))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 0), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, -1), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, -1), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, -2), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, -2), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, -2), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, -1), NoRotation))
                .then(PieceCmd(SmallPiece, Monk))
                .thenReceivedEventShouldBe(ScoreEvent(1, 9, setOf(PieceOnBoard(Position(0, -1), SmallPiece, Monk))))
        }

        it("should detect two cloisters of diferent players completed in single move") {
            GameScenario(multiPlayer(TileD, TileW, TileV, TileE, TileE, TileB, TileB, TileH, TileH, TileK, TileJ))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 0), Rotation180))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(2, 0), Rotation90))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, -1), Rotation270))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(2, -1), Rotation90))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, -1), NoRotation))
                .then(PieceCmd(SmallPiece, Monk))
                .then(TileCmd(Position(1, -1), NoRotation))
                .then(PieceCmd(SmallPiece, Monk))
                .then(TileCmd(Position(-1, -2), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(2, -2), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, -2), Rotation270))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, -2), Rotation90))
                .thenReceivedEventShouldBe(ScoreEvent(1, 9, setOf(PieceOnBoard(Position(1, -1), SmallPiece, Monk))))
                .thenReceivedEventShouldBe(ScoreEvent(2, 9, setOf(PieceOnBoard(Position(0, -1), SmallPiece, Monk))))
        }

        it("should detect two cloisters of same player completed in single move") {
            GameScenario(multiPlayer(TileD, TileW, TileV, TileE, TileB, TileE, TileB, TileH, TileH, TileK, TileJ))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 0), Rotation180))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(2, 0), Rotation90))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, -1), Rotation270))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, -1), NoRotation))
                .then(PieceCmd(SmallPiece, Monk))
                .then(TileCmd(Position(2, -1), Rotation90))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, -1), NoRotation))
                .then(PieceCmd(SmallPiece, Monk))
                .then(TileCmd(Position(-1, -2), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(2, -2), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, -2), Rotation270))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, -2), Rotation90))
                .thenReceivedEventShouldBe(PieceEvent)
                .thenReceivedEventShouldBe(ScoreEvent(1, 9, setOf(PieceOnBoard(Position(0, -1), SmallPiece, Monk))))
                .thenReceivedEventShouldBe(ScoreEvent(1, 9, setOf(PieceOnBoard(Position(1, -1), SmallPiece, Monk))))
        }
    }
})
