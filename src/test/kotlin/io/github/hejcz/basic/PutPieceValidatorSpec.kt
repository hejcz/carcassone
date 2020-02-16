package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object PutPieceValidatorSpec : Spek({

    describe("Putting handlers in invalid places") {

        fun game() =
            Game(Players.singlePlayer(), TestGameSetup(TestBasicRemainingTiles(TileD))).dispatch(BeginCmd)

        it("monk on tile without cloister") {
            GameScenario(game())
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Monk))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }

        it("knight on road") {
            GameScenario(game())
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Right)))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }

        it("knight on road 2") {
            GameScenario(game())
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Left)))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }

        it("knight on green field") {
            GameScenario(game())
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Down)))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }

        it("knight on green field") {
            GameScenario(game())
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Down)))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }

        it("brigand in castle") {
            GameScenario(game())
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Brigand(Up)))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }

        it("brigand on green field") {
            GameScenario(game())
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Brigand(Down)))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }

        it("peasant in castle") {
            GameScenario(game())
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Peasant(Location(Up))))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }

        it("peasant on road") {
            GameScenario(game())
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Peasant(Location(Right))))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }

        it("peasant on road 2") {
            GameScenario(game())
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Peasant(Location(Left))))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }
    }
})
