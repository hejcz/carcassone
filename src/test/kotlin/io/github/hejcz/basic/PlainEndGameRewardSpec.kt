package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.GameScenario
import io.github.hejcz.helper.Players
import io.github.hejcz.helper.TestBasicRemainingTiles
import io.github.hejcz.helper.TestGameSetup
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object PlainEndGameRewardSpec : Spek({

    describe("Green field") {

        fun singlePlayer(vararg tiles: Tile) =
            Game(Players.singlePlayer(), TestGameSetup(TestBasicRemainingTiles(*tiles)), true).dispatch(Begin)

        fun multiPlayer(vararg tiles: Tile) =
            Game(Players.twoPlayers(), TestGameSetup(TestBasicRemainingTiles(*tiles)), true).dispatch(Begin)

        it("should be scored") {
            GameScenario(singlePlayer(TileE))
                .then(PutTile(Position(0, 1), Rotation180))
                .then(PutPiece(SmallPiece, Peasant(Location(Left))))
                .thenReceivedEventShouldBe(PlayerScored(1, 3, emptySet()))
        }

        it("Simple case without scoring") {
            GameScenario(singlePlayer(TileE))
                .then(PutTile(Position(0, 1), Rotation180))
                .then(SkipPiece)
                .thenNoEventsShouldBePublished()
        }

        it("Scoring for two castles") {
            GameScenario(singlePlayer(TileI, TileE))
                .then(PutTile(Position(0, 1), Rotation270))
                .then(PutPiece(SmallPiece, Peasant(Location(Right))))
                .then(PutTile(Position(-1, 1), Rotation90))
                .then(SkipPiece)
                .thenReceivedEventShouldBe(PlayerScored(1, 6, emptySet()))
        }

        it("Wrong side of road") {
            GameScenario(singlePlayer(TileJ))
                .then(PutTile(Position(0, 1), Rotation180))
                .then(PutPiece(SmallPiece, Peasant(Location(Up, LeftSide))))
                .thenNoEventsShouldBePublished()
        }

        it("Right side of road") {
            GameScenario(singlePlayer(TileJ))
                .then(PutTile(Position(0, 1), Rotation180))
                .then(PutPiece(SmallPiece, Peasant(Location(Up, RightSide))))
                .thenReceivedEventShouldBe(PlayerScored(1, 3, emptySet()))
        }

        it("Multiple players disconnected green fields") {
            GameScenario(multiPlayer(TileF, TileE))
                .then(PutTile(Position(0, 1), Rotation90))
                .then(PutPiece(SmallPiece, Peasant(Location(Right))))
                .then(PutTile(Position(0, 2), Rotation180))
                .then(PutPiece(SmallPiece, Peasant(Location(Right))))
                .thenReceivedEventShouldBe(PlayerScored(1, 3, emptySet()))
                .thenReceivedEventShouldBe(PlayerScored(2, 3, emptySet()))
        }

        it("Multiple players dominance of one") {
            GameScenario(multiPlayer(TileF, TileF, TileE, TileA, TileA, TileA))
                .then(PutTile(Position(0, 1), Rotation90))
                .then(PutPiece(SmallPiece, Peasant(Location(Right))))
                .then(PutTile(Position(0, 2), Rotation90))
                .then(PutPiece(SmallPiece, Peasant(Location(Right))))
                .then(PutTile(Position(0, 3), Rotation180))
                .then(PutPiece(SmallPiece, Peasant(Location(Right))))
                .then(PutTile(Position(1, 1), Rotation270))
                .then(SkipPiece)
                .then(PutTile(Position(1, 2), Rotation270))
                .then(SkipPiece)
                .then(PutTile(Position(1, 3), Rotation270))
                .then(SkipPiece)
                .thenShouldNotReceiveEvent(PlayerScored(2, 3, emptySet()))
                .thenReceivedEventShouldBe(PlayerScored(1, 3, emptySet()))
        }
    }
})
