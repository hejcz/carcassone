package io.github.hejcz.expansion.river

import io.github.hejcz.core.*
import io.github.hejcz.setup.TilesSetup
import io.github.hejcz.core.tile.Tile
import io.github.hejcz.core.tile.TileD
import io.github.hejcz.engine.Game
import io.github.hejcz.expansion.river.tiles.*
import io.github.hejcz.helper.GameScenario
import io.github.hejcz.helper.Players
import io.github.hejcz.helper.RiverTestGameSetup
import io.github.hejcz.helper.TestRiverRemainingTiles
import org.amshove.kluent.shouldEqual
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object PlacingRiverTilesSpec : Spek({

    describe("River tile") {

        fun singlePlayer(vararg tiles: Tile) =
            Game(
                Players.singlePlayer(), RiverTestGameSetup(TestRiverRemainingTiles(*tiles))
            )
                .dispatch(BeginCmd)

        it("Placing river tile") {
            GameScenario(singlePlayer(TileBB6F6))
                .then(TileCmd(Position(0, -1), NoRotation)).thenReceivedEventShouldBe(PieceEvent)
        }

        it("Placing river tile in invalid place") {
            GameScenario(singlePlayer(TileD))
                .then(TileCmd(Position(1, 0), NoRotation))
                .thenReceivedEventShouldBe(InvalidTileLocationEvent)
        }

        it("Placing river tile in invalid place") {
            GameScenario(singlePlayer(TileD))
                .then(TileCmd(Position(-1, 0), Rotation90))
                .thenReceivedEventShouldBe(InvalidTileLocationEvent)
        }

        it("Placing river tile in invalid place") {
            GameScenario(singlePlayer(TileD))
                .then(TileCmd(Position(0, 1), Rotation180))
                .thenReceivedEventShouldBe(InvalidTileLocationEvent)
        }

        it("Connecting river tiles 1") {
            GameScenario(singlePlayer(TileBB6F2))
                .then(TileCmd(Position(0, -1), Rotation90)).thenReceivedEventShouldBe(PieceEvent)
        }

        it("Connecting river tiles 2") {
            GameScenario(singlePlayer(TileBB6F2))
                .then(TileCmd(Position(0, -1), Rotation270)).thenReceivedEventShouldBe(PieceEvent)
        }

        it("Connecting river tiles invalid") {
            GameScenario(singlePlayer(TileBB6F2))
                .then(TileCmd(Position(0, -1), Rotation180))
                .thenReceivedEventShouldBe(InvalidTileLocationEvent)
        }

        it("Connecting river tiles invalid") {
            GameScenario(singlePlayer(TileBB6F2))
                .then(TileCmd(Position(0, -1), NoRotation))
                .thenReceivedEventShouldBe(InvalidTileLocationEvent)
        }

        it("Two consecutive river tiles can't turn in same direction") {
            GameScenario(singlePlayer(TileBB6F9, TileBB6F10))
                .then(TileCmd(Position(0, -1), Rotation180))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, -1), Rotation270))
                .thenReceivedEventShouldBe(InvalidTileLocationEvent)
        }

        it("Two consecutive river tiles can turn in same direction if are not consecutive") {
            GameScenario(singlePlayer(TileBB6F9, TileBB6F6, TileBB6F10))
                .then(TileCmd(Position(0, -1), Rotation180))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, -1), Rotation90))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-2, -1), Rotation270)).thenReceivedEventShouldBe(PieceEvent)
        }

        it("River must be connected with previous river - it can't be adjacent by field e.g.") {
            GameScenario(singlePlayer(TileBB6F7))
                .then(TileCmd(Position(0, 1), NoRotation))
                .thenReceivedEventShouldBe(InvalidTileLocationEvent)
        }

        it("River must be connected with previous river - it can't be adjacent by field e.g.") {
            GameScenario(singlePlayer(TileBB6F7))
                .then(TileCmd(Position(-1, 0), NoRotation))
                .thenReceivedEventShouldBe(InvalidTileLocationEvent)
        }

        it("River must be connected with previous river - it can't be adjacent by field e.g.") {
            GameScenario(singlePlayer(TileBB6F7))
                .then(TileCmd(Position(1, 0), Rotation90))
                .thenReceivedEventShouldBe(InvalidTileLocationEvent)
        }

        it("Can place knight in castle on river tile") {
            GameScenario(singlePlayer(TileBB6F2, TileBB6F3))
                .then(TileCmd(Position(0, -1), Rotation90))
                .then(PieceCmd(SmallPiece, Knight(Right))).thenReceivedEventShouldBeOnlyPlaceTile()
        }

        it("Can place brigand on a road on river tile") {
            GameScenario(singlePlayer(TileBB6F2, TileBB6F3))
                .then(TileCmd(Position(0, -1), Rotation90))
                .then(PieceCmd(SmallPiece, Brigand(Left))).thenReceivedEventShouldBeOnlyPlaceTile()
        }

        it("Can place peasant on a field on river tile") {
            GameScenario(singlePlayer(TileBB6F2, TileBB6F3))
                .then(TileCmd(Position(0, -1), Rotation90))
                .then(PieceCmd(SmallPiece, Peasant(Location(Left, RightSide)))).thenReceivedEventShouldBeOnlyPlaceTile()
        }

        it("Can place monk on a field on river tile") {
            GameScenario(singlePlayer(TileBB6F8, TileBB6F3))
                .then(TileCmd(Position(0, -1), Rotation90))
                .then(PieceCmd(SmallPiece, Monk)).thenReceivedEventShouldBeOnlyPlaceTile()
        }

        it("Can't place a knight on river") {
            GameScenario(singlePlayer(TileBB6F2))
                .then(TileCmd(Position(0, -1), Rotation90))
                .then(PieceCmd(SmallPiece, Knight(Down)))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }

        it("Can't place a brigand on river") {
            GameScenario(singlePlayer(TileBB6F2))
                .then(TileCmd(Position(0, -1), Rotation90))
                .then(PieceCmd(SmallPiece, Brigand(Down)))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }

        it("Can't place a peasant on river") {
            GameScenario(singlePlayer(TileBB6F2))
                .then(TileCmd(Position(0, -1), Rotation90))
                .then(PieceCmd(SmallPiece, Peasant(Location(Down))))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }

        it("If river enabled source should be first") {
            val deck = TilesSetup().withExtensions(RiverExtension)
            deck.remainingTiles()[0] shouldEqual TileBB6F1
        }

        it("Amount of tiles in basic deck") {
            val deck = TilesSetup()
            deck.remainingTiles().size shouldEqual 72
        }

        it("Amount of tiles in game with river extension") {
            val deck = TilesSetup().withExtensions(RiverExtension)
            deck.remainingTiles().size shouldEqual 84
        }

        it("If river enabled estuary should be last of river tiles") {
            val deck = TilesSetup().withExtensions(RiverExtension)
            val remainingTiles = deck.remainingTiles()
            remainingTiles.drop(11)[0] shouldEqual TileBB6F12
        }
    }
})
