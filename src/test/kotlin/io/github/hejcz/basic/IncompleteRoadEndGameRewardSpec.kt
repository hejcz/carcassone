package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object IncompleteRoadEndGameRewardSpec : Spek({

    describe("Incomplete road scoring") {

        fun singlePlayer(vararg tiles: Tile) = Game(
            Players.singlePlayer(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        ).apply { dispatch(Begin) }

        fun multiPlayer(vararg tiles: Tile) = Game(
            Players.twoPlayers(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        ).apply { dispatch(Begin) }

        it("should detect simple incomplete road") {
            GameScenario(singlePlayer(TileK))
                .then(PutTile(Position(1, 0), NoRotation))
                .then(PutPiece(SmallPiece, Brigand(Left)))
                .thenReceivedEventShouldBe(PlayerScored(1, 2, emptySet()))
        }

        it("should not be triggered if there are still some tiles in the deck") {
            GameScenario(singlePlayer(TileK, TileA))
                .then(PutTile(Position(1, 0), NoRotation))
                .then(PutPiece(SmallPiece, Brigand(Left))).thenReceivedEventShouldBeOnlyPlaceTile()
        }

        it("should detect simple road made out of 3 tiles") {
            GameScenario(singlePlayer(TileK, TileL))
                .then(PutTile(Position(1, 0), NoRotation))
                .then(PutPiece(SmallPiece, Brigand(Down))).thenReceivedEventShouldBeOnlyPlaceTile()
                .then(PutTile(Position(1, -1), Rotation90))
                .then(PutPiece(SmallPiece, Brigand(Down)))
                .thenReceivedEventShouldBe(PlayerScored(1, 3, emptySet()))
        }

        it("should reward multiple players if they share road with equal number of mapples") {
            GameScenario(multiPlayer(TileK, TileU, TileJ, TileK))
                .then(PutTile(Position(1, 0), NoRotation))
                .then(PutPiece(SmallPiece, Brigand(Down))).thenReceivedEventShouldBeOnlyPlaceTile()
                .then(PutTile(Position(2, 0), NoRotation))
                .then(PutPiece(SmallPiece, Brigand(Down))).thenReceivedEventShouldBeOnlyPlaceTile()
                .then(PutTile(Position(1, -1), Rotation270))
                .then(SkipPiece).thenReceivedEventShouldBeOnlyPlaceTile()
                .then(PutTile(Position(2, -1), Rotation90))
                .then(SkipPiece)
                .thenReceivedEventShouldBe(PlayerScored(1, 5, emptySet()))
                .thenReceivedEventShouldBe(PlayerScored(2, 5, emptySet()))
        }

        it("should reward single player if he has advantage of mapples over his opponent") {
            GameScenario(multiPlayer(TileV, TileV, TileB, TileU, TileV, TileV, TileV))
                        .then(PutTile(Position(1, 0), NoRotation)).thenReceivedEventShouldBe(SelectPiece)
                        .then(PutPiece(SmallPiece, Brigand(Down))).thenReceivedEventShouldBeOnlyPlaceTile()
                        .then(PutTile(Position(2, 0), Rotation270)).thenReceivedEventShouldBe(SelectPiece)
                        .then(PutPiece(SmallPiece, Brigand(Down))).thenReceivedEventShouldBeOnlyPlaceTile()
                        .then(PutTile(Position(2, 1), NoRotation)).thenReceivedEventShouldBe(SelectPiece)
                        .then(SkipPiece).thenReceivedEventShouldBeOnlyPlaceTile()
                        .then(PutTile(Position(3, 1), NoRotation)).thenReceivedEventShouldBe(SelectPiece)
                        .then(PutPiece(SmallPiece, Brigand(Down))).thenReceivedEventShouldBeOnlyPlaceTile()
                        .then(PutTile(Position(3, 0), Rotation90)).thenReceivedEventShouldBe(SelectPiece)
                        .then(SkipPiece).thenReceivedEventShouldBeOnlyPlaceTile()
                        .then(PutTile(Position(1, -1), Rotation180)).thenReceivedEventShouldBe(SelectPiece)
                        .then(SkipPiece).thenReceivedEventShouldBeOnlyPlaceTile()
                        .then(PutTile(Position(2, -1), Rotation90)).thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPiece)
                .thenReceivedEventShouldBe(PlayerScored(2, 7, emptySet()))
                .thenShouldNotReceiveEvent(PlayerScored(1, 7, emptySet()))
        }
    }
})
