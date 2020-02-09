package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object IncompleteCloisterEndGameRewardSpec : Spek({

    describe("Incomplete cloister detector") {

        fun singlePlayer(vararg tiles: Tile) = Game(
            Players.singlePlayer(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        ).apply { dispatch(Begin) }

        fun multiPlayer(vararg tiles: Tile) = Game(
            Players.twoPlayers(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        ).apply { dispatch(Begin) }

        it("should detect cloister with 7/8 surrounding tiles") {
            GameScenario(singlePlayer(TileD, TileD, TileB, TileB, TileB, TileB, TileB))
                .then(PutTile(Position(1, 0), NoRotation))
                .thenShouldNotReceiveEvent(PlayerScored(1, 9, emptySet()))
                .then(SkipPiece)
                .then(PutTile(Position(-1, 0), NoRotation))
                .thenShouldNotReceiveEvent(PlayerScored(1, 9, emptySet()))
                .then(SkipPiece)
                .then(PutTile(Position(1, -1), NoRotation))
                .thenShouldNotReceiveEvent(PlayerScored(1, 9, emptySet()))
                .then(SkipPiece)
                .then(PutTile(Position(0, -1), NoRotation))
                .thenShouldNotReceiveEvent(PlayerScored(1, 9, emptySet()))
                .then(PutPiece(SmallPiece, Monk))
                .then(PutTile(Position(-1, -1), NoRotation))
                .thenShouldNotReceiveEvent(PlayerScored(1, 9, emptySet()))
                .then(SkipPiece)
                .then(PutTile(Position(-1, -2), NoRotation))
                .thenShouldNotReceiveEvent(PlayerScored(1, 9, emptySet()))
                .then(SkipPiece)
                .then(PutTile(Position(0, -2), NoRotation))
                .thenShouldNotReceiveEvent(PlayerScored(1, 9, emptySet()))
                .then(SkipPiece).thenReceivedEventShouldBe(PlayerScored(1, 8, emptySet()))
        }

        it("should detect cloister with 6/8 surrounding tiles") {
            GameScenario(singlePlayer(TileD, TileD, TileB, TileB, TileB, TileB))
                .then(PutTile(Position(1, 0), NoRotation))
                .thenShouldNotReceiveEvent(PlayerScored(1, 9, emptySet()))
                .then(SkipPiece)
                .then(PutTile(Position(-1, 0), NoRotation))
                .thenShouldNotReceiveEvent(PlayerScored(1, 9, emptySet()))
                .then(SkipPiece)
                .then(PutTile(Position(1, -1), NoRotation))
                .thenShouldNotReceiveEvent(PlayerScored(1, 9, emptySet()))
                .then(SkipPiece)
                .then(PutTile(Position(0, -1), NoRotation))
                .thenShouldNotReceiveEvent(PlayerScored(1, 9, emptySet()))
                .then(PutPiece(SmallPiece, Monk))
                .then(PutTile(Position(-1, -1), NoRotation))
                .thenShouldNotReceiveEvent(PlayerScored(1, 9, emptySet()))
                .then(SkipPiece)
                .then(PutTile(Position(0, -2), NoRotation))
                .thenShouldNotReceiveEvent(PlayerScored(1, 9, emptySet()))
                .then(SkipPiece).thenReceivedEventShouldBe(PlayerScored(1, 7, emptySet()))
        }

        it("should detect cloister with 1/8 surrounding tiles") {
            GameScenario(singlePlayer(TileB))
                .then(PutTile(Position(0, -1), NoRotation))
                .thenShouldNotReceiveEvent(PlayerScored(1, 9, emptySet()))
                .then(PutPiece(SmallPiece, Monk)).thenReceivedEventShouldBe(PlayerScored(1, 2, emptySet()))
        }

        it("should detect cloister added as a last tile") {
            GameScenario(singlePlayer(TileD, TileD, TileB, TileB, TileB, TileB, TileB))
                .then(PutTile(Position(1, 0), NoRotation))
                .thenShouldNotReceiveEvent(PlayerScored(1, 9, emptySet()))
                .then(SkipPiece)
                .then(PutTile(Position(-1, 0), NoRotation))
                .thenShouldNotReceiveEvent(PlayerScored(1, 9, emptySet()))
                .then(SkipPiece)
                .then(PutTile(Position(1, -1), NoRotation))
                .thenShouldNotReceiveEvent(PlayerScored(1, 9, emptySet()))
                .then(SkipPiece)
                .then(PutTile(Position(-1, -1), NoRotation))
                .thenShouldNotReceiveEvent(PlayerScored(1, 9, emptySet()))
                .then(SkipPiece)
                .then(PutTile(Position(-1, -2), NoRotation))
                .thenShouldNotReceiveEvent(PlayerScored(1, 9, emptySet()))
                .then(SkipPiece)
                .then(PutTile(Position(0, -2), NoRotation))
                .thenShouldNotReceiveEvent(PlayerScored(1, 9, emptySet()))
                .then(SkipPiece)
                .then(PutTile(Position(0, -1), NoRotation))
                .thenShouldNotReceiveEvent(PlayerScored(1, 9, emptySet()))
                .then(PutPiece(SmallPiece, Monk)).thenReceivedEventShouldBe(PlayerScored(1, 8, emptySet()))
        }

        it("should detect two incomplete cloisters of different players with 7/8 and 6/8 tiles surrounding tiles respectively") {
            GameScenario(multiPlayer(TileD, TileW, TileV, TileE, TileE, TileB, TileB, TileH, TileK))
                .then(PutTile(Position(1, 0), NoRotation)).shouldContainSelectPiece()
                .then(SkipPiece).shouldContainPlaceTileOnly()
                .then(PutTile(Position(-1, 0), Rotation180)).shouldContainSelectPiece()
                .then(SkipPiece).shouldContainPlaceTileOnly()
                .then(PutTile(Position(2, 0), Rotation90)).shouldContainSelectPiece()
                .then(SkipPiece).shouldContainPlaceTileOnly()
                .then(PutTile(Position(-1, -1), Rotation270)).shouldContainSelectPiece()
                .then(SkipPiece).shouldContainPlaceTileOnly()
                .then(PutTile(Position(2, -1), Rotation90)).shouldContainSelectPiece()
                .then(SkipPiece).shouldContainPlaceTileOnly()
                .then(PutTile(Position(0, -1), NoRotation)).shouldContainSelectPiece()
                .then(PutPiece(SmallPiece, Monk)).shouldContainPlaceTileOnly()
                .then(PutTile(Position(1, -1), NoRotation)).shouldContainSelectPiece()
                .then(PutPiece(SmallPiece, Monk)).shouldContainPlaceTileOnly()
                .then(PutTile(Position(-1, -2), NoRotation)).shouldContainSelectPiece()
                .then(SkipPiece).shouldContainPlaceTileOnly()
                .then(PutTile(Position(0, -2), Rotation270)).shouldContainSelectPiece()
                .then(SkipPiece)
                .thenReceivedEventShouldBe(PlayerScored(1, 7, emptySet()))
                .thenReceivedEventShouldBe(PlayerScored(2, 8, emptySet()))
        }

        it("should detect two incomplete cloisters of the same player") {
            GameScenario(multiPlayer(TileD, TileW, TileV, TileE, TileB, TileE, TileB, TileH, TileH, TileK))
                .then(PutTile(Position(1, 0), NoRotation)).shouldContainSelectPiece()
                .then(SkipPiece).shouldContainPlaceTileOnly()
                .then(PutTile(Position(-1, 0), Rotation180)).shouldContainSelectPiece()
                .then(SkipPiece).shouldContainPlaceTileOnly()
                .then(PutTile(Position(2, 0), Rotation90)).shouldContainSelectPiece()
                .then(SkipPiece).shouldContainPlaceTileOnly()
                .then(PutTile(Position(-1, -1), Rotation270)).shouldContainSelectPiece()
                .then(SkipPiece).shouldContainPlaceTileOnly()
                .then(PutTile(Position(0, -1), NoRotation)).shouldContainSelectPiece()
                .then(PutPiece(SmallPiece, Monk)).shouldContainPlaceTileOnly()
                .then(PutTile(Position(2, -1), Rotation90)).shouldContainSelectPiece()
                .then(SkipPiece).shouldContainPlaceTileOnly()
                .then(PutTile(Position(1, -1), NoRotation)).shouldContainSelectPiece()
                .then(PutPiece(SmallPiece, Monk)).shouldContainPlaceTileOnly()
                .then(PutTile(Position(-1, -2), NoRotation)).shouldContainSelectPiece()
                .then(SkipPiece).shouldContainPlaceTileOnly()
                .then(PutTile(Position(2, -2), NoRotation)).shouldContainSelectPiece()
                .then(SkipPiece).shouldContainPlaceTileOnly()
                .then(PutTile(Position(0, -2), Rotation270)).shouldContainSelectPiece()
                .then(SkipPiece)
                .thenReceivedEventsShouldBe(listOf(PlayerScored(1, 8, emptySet()), PlayerScored(1, 8, emptySet())))
        }
    }
})
