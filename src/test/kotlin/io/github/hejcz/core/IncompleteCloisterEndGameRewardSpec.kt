package io.github.hejcz.core

import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object IncompleteCloisterEndGameRewardSpec : Spek({

    describe("Incomplete cloister detector") {

        fun singlePlayer(vararg tiles: Tile) = Game(
            Players.singlePlayer(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        ).dispatch(BeginCmd)

        fun multiPlayer(vararg tiles: Tile) = Game(
            Players.twoPlayers(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        ).dispatch(BeginCmd)

        it("should detect cloister with 7/8 surrounding tiles") {
            GameScenario(singlePlayer(TileD, TileD, TileB, TileB, TileB, TileB, TileB))
                .then(TileCmd(Position(1, 0), NoRotation))
                .thenShouldNotReceiveEvent(ScoreEvent(1, 9, emptySet()))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 0), NoRotation))
                .thenShouldNotReceiveEvent(ScoreEvent(1, 9, emptySet()))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, -1), NoRotation))
                .thenShouldNotReceiveEvent(ScoreEvent(1, 9, emptySet()))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, -1), NoRotation))
                .thenShouldNotReceiveEvent(ScoreEvent(1, 9, emptySet()))
                .then(PieceCmd(SmallPiece, Monk))
                .then(TileCmd(Position(-1, -1), NoRotation))
                .thenShouldNotReceiveEvent(ScoreEvent(1, 9, emptySet()))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, -2), NoRotation))
                .thenShouldNotReceiveEvent(ScoreEvent(1, 9, emptySet()))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, -2), NoRotation))
                .thenShouldNotReceiveEvent(ScoreEvent(1, 9, emptySet()))
                .then(SkipPieceCmd).thenReceivedEventShouldBe(ScoreEvent(1, 8, emptySet()))
        }

        it("should detect cloister with 6/8 surrounding tiles") {
            GameScenario(singlePlayer(TileD, TileD, TileB, TileB, TileB, TileB))
                .then(TileCmd(Position(1, 0), NoRotation))
                .thenShouldNotReceiveEvent(ScoreEvent(1, 9, emptySet()))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 0), NoRotation))
                .thenShouldNotReceiveEvent(ScoreEvent(1, 9, emptySet()))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, -1), NoRotation))
                .thenShouldNotReceiveEvent(ScoreEvent(1, 9, emptySet()))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, -1), NoRotation))
                .thenShouldNotReceiveEvent(ScoreEvent(1, 9, emptySet()))
                .then(PieceCmd(SmallPiece, Monk))
                .then(TileCmd(Position(-1, -1), NoRotation))
                .thenShouldNotReceiveEvent(ScoreEvent(1, 9, emptySet()))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, -2), NoRotation))
                .thenShouldNotReceiveEvent(ScoreEvent(1, 9, emptySet()))
                .then(SkipPieceCmd).thenReceivedEventShouldBe(ScoreEvent(1, 7, emptySet()))
        }

        it("should detect cloister with 1/8 surrounding tiles") {
            GameScenario(singlePlayer(TileB))
                .then(TileCmd(Position(0, -1), NoRotation))
                .thenShouldNotReceiveEvent(ScoreEvent(1, 9, emptySet()))
                .then(PieceCmd(SmallPiece, Monk)).thenReceivedEventShouldBe(ScoreEvent(1, 2, emptySet()))
        }

        it("should detect cloister added as a last tile") {
            GameScenario(singlePlayer(TileD, TileD, TileB, TileB, TileB, TileB, TileB))
                .then(TileCmd(Position(1, 0), NoRotation))
                .thenShouldNotReceiveEvent(ScoreEvent(1, 9, emptySet()))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 0), NoRotation))
                .thenShouldNotReceiveEvent(ScoreEvent(1, 9, emptySet()))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, -1), NoRotation))
                .thenShouldNotReceiveEvent(ScoreEvent(1, 9, emptySet()))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, -1), NoRotation))
                .thenShouldNotReceiveEvent(ScoreEvent(1, 9, emptySet()))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, -2), NoRotation))
                .thenShouldNotReceiveEvent(ScoreEvent(1, 9, emptySet()))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, -2), NoRotation))
                .thenShouldNotReceiveEvent(ScoreEvent(1, 9, emptySet()))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, -1), NoRotation))
                .thenShouldNotReceiveEvent(ScoreEvent(1, 9, emptySet()))
                .then(PieceCmd(SmallPiece, Monk)).thenReceivedEventShouldBe(ScoreEvent(1, 8, emptySet()))
        }

        it("should detect two incomplete cloisters of different players with 7/8 and 6/8 tiles surrounding tiles respectively") {
            GameScenario(multiPlayer(TileD, TileW, TileV, TileE, TileE, TileB, TileB, TileH, TileK))
                .then(TileCmd(Position(1, 0), NoRotation)).thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd).thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(-1, 0), Rotation180)).thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd).thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(2, 0), Rotation90)).thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd).thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(-1, -1), Rotation270)).thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd).thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(2, -1), Rotation90)).thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd).thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, -1), NoRotation)).thenReceivedEventShouldBe(PieceEvent)
                .then(PieceCmd(SmallPiece, Monk)).thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(1, -1), NoRotation)).thenReceivedEventShouldBe(PieceEvent)
                .then(PieceCmd(SmallPiece, Monk)).thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(-1, -2), NoRotation)).thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd).thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, -2), Rotation270)).thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBe(ScoreEvent(1, 7, emptySet()))
                .thenReceivedEventShouldBe(ScoreEvent(2, 8, emptySet()))
        }

        it("should detect two incomplete cloisters of the same player") {
            GameScenario(multiPlayer(TileD, TileW, TileV, TileE, TileB, TileE, TileB, TileH, TileH, TileK))
                .then(TileCmd(Position(1, 0), NoRotation)).thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd).thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(-1, 0), Rotation180)).thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd).thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(2, 0), Rotation90)).thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd).thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(-1, -1), Rotation270)).thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd).thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, -1), NoRotation)).thenReceivedEventShouldBe(PieceEvent)
                .then(PieceCmd(SmallPiece, Monk)).thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(2, -1), Rotation90)).thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd).thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(1, -1), NoRotation)).thenReceivedEventShouldBe(PieceEvent)
                .then(PieceCmd(SmallPiece, Monk)).thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(-1, -2), NoRotation)).thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd).thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(2, -2), NoRotation)).thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd).thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, -2), Rotation270)).thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd)
                .thenReceivedEventsShouldBe(listOf(ScoreEvent(1, 8, emptySet()), ScoreEvent(1, 8, emptySet())))
        }
    }
})
