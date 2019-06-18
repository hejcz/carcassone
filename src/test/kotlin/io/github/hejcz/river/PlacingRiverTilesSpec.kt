package io.github.hejcz.river

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.*
import io.github.hejcz.river.tiles.*
import io.github.hejcz.setup.TilesSetup
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object PlacingRiverTilesSpec : Spek({

    describe("River tile") {

        fun singlePlayer(vararg tiles: Tile) =
            Game(Players.singlePlayer(), RiverTestGameSetup(TestRiverRemainingTiles(*tiles)))
                .apply { dispatch(Begin) }

        it("Placing river tile") {
            val game = singlePlayer(TileBB6F6)
            game.dispatch(PutTile(Position(0, -1), NoRotation)).shouldContainSelectPieceOnly()
        }

        it("Placing river tile in invalid place") {
            val game = singlePlayer(TileD)
            game.dispatch(PutTile(Position(1, 0), NoRotation)) shouldContain InvalidTileLocation
        }

        it("Placing river tile in invalid place") {
            val game = singlePlayer(TileD)
            game.dispatch(PutTile(Position(-1, 0), Rotation90)) shouldContain InvalidTileLocation
        }

        it("Placing river tile in invalid place") {
            val game = singlePlayer(TileD)
            game.dispatch(PutTile(Position(0, 1), Rotation180)) shouldContain InvalidTileLocation
        }

        it("Connecting river tiles 1") {
            val game = singlePlayer(TileBB6F2)
            game.dispatch(PutTile(Position(0, -1), Rotation90)).shouldContainSelectPieceOnly()
        }

        it("Connecting river tiles 2") {
            val game = singlePlayer(TileBB6F2)
            game.dispatch(PutTile(Position(0, -1), Rotation270)).shouldContainSelectPieceOnly()
        }

        it("Connecting river tiles invalid") {
            val game = singlePlayer(TileBB6F2)
            game.dispatch(PutTile(Position(0, -1), Rotation180)) shouldContain InvalidTileLocation
        }

        it("Connecting river tiles invalid") {
            val game = singlePlayer(TileBB6F2)
            game.dispatch(PutTile(Position(0, -1), NoRotation)) shouldContain InvalidTileLocation
        }

        it("Two consecutive river tiles can't turn in same direction") {
            val game = singlePlayer(TileBB6F9, TileBB6F10)
            game.dispatch(PutTile(Position(0, -1), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, -1), Rotation270)) shouldContain InvalidTileLocation
        }

        it("Two consecutive river tiles can turn in same direction if are not consecutive") {
            val game = singlePlayer(TileBB6F9, TileBB6F6, TileBB6F10)
            game.dispatch(PutTile(Position(0, -1), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, -1), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-2, -1), Rotation270)).shouldContainSelectPieceOnly()
        }

        it("River must be connected with previous river - it can't be adjacent by field e.g.") {
            val game = singlePlayer(TileBB6F7)
            game.dispatch(PutTile(Position(0, 1), NoRotation)) shouldContain InvalidTileLocation
        }

        it("River must be connected with previous river - it can't be adjacent by field e.g.") {
            val game = singlePlayer(TileBB6F7)
            game.dispatch(PutTile(Position(-1, 0), NoRotation)) shouldContain InvalidTileLocation
        }

        it("River must be connected with previous river - it can't be adjacent by field e.g.") {
            val game = singlePlayer(TileBB6F7)
            game.dispatch(PutTile(Position(1, 0), Rotation90)) shouldContain InvalidTileLocation
        }

        it("Can place knight in castle on river tile") {
            val game = singlePlayer(TileBB6F2, TileBB6F3)
            game.dispatch(PutTile(Position(0, -1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Knight(Right))).shouldContainPlaceTileOnly()
        }

        it("Can place brigand on a road on river tile") {
            val game = singlePlayer(TileBB6F2, TileBB6F3)
            game.dispatch(PutTile(Position(0, -1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Brigand(Left))).shouldContainPlaceTileOnly()
        }

        it("Can place peasant on a field on river tile") {
            val game = singlePlayer(TileBB6F2, TileBB6F3)
            game.dispatch(PutTile(Position(0, -1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Peasant(Location(Left, RightSide)))).shouldContainPlaceTileOnly()
        }

        it("Can place monk on a field on river tile") {
            val game = singlePlayer(TileBB6F8, TileBB6F3)
            game.dispatch(PutTile(Position(0, -1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Monk)).shouldContainPlaceTileOnly()
        }

        it("Can't place a knight on river") {
            val game = singlePlayer(TileBB6F2)
            game.dispatch(PutTile(Position(0, -1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Knight(Down))) shouldContain InvalidPieceLocation
        }

        it("Can't place a brigand on river") {
            val game = singlePlayer(TileBB6F2)
            game.dispatch(PutTile(Position(0, -1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Brigand(Down))) shouldContain InvalidPieceLocation
        }

        it("Can't place a peasant on river") {
            val game = singlePlayer(TileBB6F2)
            game.dispatch(PutTile(Position(0, -1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Peasant(Location(Down)))) shouldContain InvalidPieceLocation
        }

        it("If river enabled source should be first") {
            val deck = TilesSetup().withExtensions(RiverExtension)
            deck.remainingTiles().next() shouldEqual TileBB6F1
        }

        it("Amount of tiles in basic deck") {
            val deck = TilesSetup()
            deck.remainingTiles().size() shouldEqual 72
        }

        it("Amount of tiles in game with river extension") {
            val deck = TilesSetup().withExtensions(RiverExtension)
            deck.remainingTiles().size() shouldEqual 84
        }

        it("If river enabled estuary should be last of river tiles") {
            val deck = TilesSetup().withExtensions(RiverExtension)
            val remainingTiles = deck.remainingTiles()
            (1..11).forEach { _ -> remainingTiles.next() }
            remainingTiles.next() shouldEqual TileBB6F12
        }
    }
})
