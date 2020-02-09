package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object SinglePieceInObjectSpec : Spek({

    describe("Putting handlers in taken object") {

        fun singlePlayer(vararg tiles: Tile) =
            Game(Players.singlePlayer(), TestGameSetup(TestBasicRemainingTiles(*tiles))).apply { dispatch(Begin) }

        fun multiPlayer(vararg tiles: Tile) =
            Game(Players.twoPlayers(), TestGameSetup(TestBasicRemainingTiles(*tiles))).apply { dispatch(Begin) }

        it("knights") {
            GameScenario(singlePlayer(TileF, TileF))
                .then(PutTile(Position(0, 1), Rotation90))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .then(PutTile(Position(0, 2), Rotation180))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .thenReceivedEventShouldBe(InvalidPieceLocation)
        }

        it("knights two players") {
            GameScenario(multiPlayer(TileF, TileF))
                .then(PutTile(Position(0, 1), Rotation90))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .then(PutTile(Position(0, 2), Rotation180))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .thenReceivedEventShouldBe(InvalidPieceLocation)
        }

        it("brigands") {
            GameScenario(singlePlayer(TileD, TileV))
                .then(PutTile(Position(1, 0), NoRotation))
                .then(PutPiece(SmallPiece, Brigand(Right)))
                .then(PutTile(Position(2, 0), NoRotation))
                .then(PutPiece(SmallPiece, Brigand(Down)))
                .thenReceivedEventShouldBe(InvalidPieceLocation)
        }

        it("brigands two players") {
            GameScenario(multiPlayer(TileD, TileV))
                .then(PutTile(Position(1, 0), NoRotation))
                .then(PutPiece(SmallPiece, Brigand(Right)))
                .then(PutTile(Position(2, 0), NoRotation))
                .then(PutPiece(SmallPiece, Brigand(Down)))
                .thenReceivedEventShouldBe(InvalidPieceLocation)
        }

        it("peasants") {
            GameScenario(singlePlayer(TileD, TileV))
                .then(PutTile(Position(1, 0), NoRotation))
                .then(PutPiece(SmallPiece, Peasant(Location(Right, LeftSide))))
                .then(PutTile(Position(2, 0), NoRotation))
                .then(PutPiece(SmallPiece, Peasant(Location(Right))))
                .thenReceivedEventShouldBe(InvalidPieceLocation)
        }

        it("peasants two players") {
            GameScenario(multiPlayer(TileD, TileV))
                .then(PutTile(Position(1, 0), NoRotation))
                .then(PutPiece(SmallPiece, Peasant(Location(Right, LeftSide))))
                .then(PutTile(Position(2, 0), NoRotation))
                .then(PutPiece(SmallPiece, Peasant(Location(Right))))
                .thenReceivedEventShouldBe(InvalidPieceLocation)
        }
    }
})
