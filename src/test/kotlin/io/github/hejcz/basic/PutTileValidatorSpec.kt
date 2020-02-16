package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object PutTileValidatorSpec : Spek({

    describe("Putting tile in invalid places") {

        fun singlePlayer(vararg tiles: Tile) =
            Game(Players.singlePlayer(), TestGameSetup(TestBasicRemainingTiles(*tiles))).dispatch(BeginCmd)

        it("roads validation") {
            GameScenario(singlePlayer(TileA))
                .then(TileCmd(Position(1, 0), NoRotation))
                .thenReceivedEventShouldBe(InvalidTileLocation)
        }

        it("roads validation") {
            GameScenario(singlePlayer(TileA))
                .then(TileCmd(Position(1, 0), Rotation180))
                .thenReceivedEventShouldBe(InvalidTileLocation)
        }

        it("roads validation") {
            GameScenario(singlePlayer(TileA))
                .then(TileCmd(Position(1, 0), Rotation270))
                .thenReceivedEventShouldBe(InvalidTileLocation)
        }

        it("roads validation") {
            GameScenario(singlePlayer(TileA))
                .then(TileCmd(Position(1, 0), Rotation90)).thenReceivedEventShouldBe(SelectPiece)
        }

        it("Connecting tiles with bounded object") {
            GameScenario(singlePlayer(TileI, TileI))
                .then(TileCmd(Position(0, -1), Rotation180))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, -2), Rotation180))
                .thenReceivedEventShouldBe(InvalidTileLocation)
        }
    }
})
