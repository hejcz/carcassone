package io.github.hejcz.river

import io.github.hejcz.*
import io.github.hejcz.engine.DefaultDeck
import io.github.hejcz.engine.Game
import io.github.hejcz.engine.River
import io.github.hejcz.helper.*
import io.github.hejcz.mapples.*
import io.github.hejcz.placement.*
import io.github.hejcz.rules.basic.CastleCompletedRule
import io.github.hejcz.tiles.basic.Tile
import io.github.hejcz.tiles.basic.TileD
import io.github.hejcz.tiles.river.*
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object PlacingRiverTilesSpec : Spek({

    // TODO fix tests names
    describe("River tile") {

        fun singlePlayer(vararg tiles: Tile) = Game(
            setOf(CastleCompletedRule),
            emptySet(),
            Players.singlePlayer(),
            RiverTestGameSetup(TestRiverRemainingTiles(*tiles))
        )

        it("Placing river tile") {
            val game = singlePlayer(TileBB6F6)
            game.dispatch(PutTile(Position(0, -1), NoRotation)).shouldContainSelectPieceOnly()
        }

        it("Placing river tile in invalid place") {
            val game = singlePlayer(TileD)
            game.dispatch(PutTile(Position(1, 0), NoRotation)) shouldContain TilePlacedInInvalidPlace
            game.dispatch(PutTile(Position(-1, 0), Rotation90)) shouldContain TilePlacedInInvalidPlace
            game.dispatch(PutTile(Position(0, 1), Rotation180)) shouldContain TilePlacedInInvalidPlace
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
            game.dispatch(PutTile(Position(0, -1), NoRotation)) shouldContain TilePlacedInInvalidPlace
            game.dispatch(PutTile(Position(0, -1), Rotation180)) shouldContain TilePlacedInInvalidPlace
        }

        it("Two consecutive river tiles can't turn in same direction") {
            val game = singlePlayer(TileBB6F9, TileBB6F10)
            game.dispatch(PutTile(Position(0, -1), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, -1), Rotation270)) shouldContain TilePlacedInInvalidPlace
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
            game.dispatch(PutTile(Position(0, 1), NoRotation)) shouldContain TilePlacedInInvalidPlace
            game.dispatch(PutTile(Position(-1, 0), NoRotation)) shouldContain TilePlacedInInvalidPlace
            game.dispatch(PutTile(Position(1, 0), Rotation90)) shouldContain TilePlacedInInvalidPlace
        }

        it("Can place knight in castle on river tile") {
            val game = singlePlayer(TileBB6F2)
            game.dispatch(PutTile(Position(0, -1), Rotation90))
            game.dispatch(PutPiece(Mapple, Knight(Right))) shouldEqual emptyList()
        }

        it("Can place brigand on a road on river tile") {
            val game = singlePlayer(TileBB6F2)
            game.dispatch(PutTile(Position(0, -1), Rotation90))
            game.dispatch(PutPiece(Mapple, Brigand(Left))) shouldEqual emptyList()
        }

        it("Can place peasant on a field on river tile") {
            val game = singlePlayer(TileBB6F2)
            game.dispatch(PutTile(Position(0, -1), Rotation90))
            game.dispatch(PutPiece(Mapple, Peasant(Location(Left, RightSide)))) shouldEqual emptyList()
        }

        it("Can place monk on a field on river tile") {
            val game = singlePlayer(TileBB6F8)
            game.dispatch(PutTile(Position(0, -1), Rotation90))
            game.dispatch(PutPiece(Mapple, Monk)) shouldEqual emptyList()
        }

        it("Can't place a knight on river") {
            val game = singlePlayer(TileBB6F2)
            game.dispatch(PutTile(Position(0, -1), Rotation90))
            game.dispatch(PutPiece(Mapple, Knight(Down))) shouldContain PiecePlacedInInvalidPlace
        }

        it("Can't place a brigand on river") {
            val game = singlePlayer(TileBB6F2)
            game.dispatch(PutTile(Position(0, -1), Rotation90))
            game.dispatch(PutPiece(Mapple, Brigand(Down))) shouldContain PiecePlacedInInvalidPlace
        }

        it("Can't place a peasant on river") {
            val game = singlePlayer(TileBB6F2)
            game.dispatch(PutTile(Position(0, -1), Rotation90))
            game.dispatch(PutPiece(Mapple, Peasant(Location(Down)))) shouldContain PiecePlacedInInvalidPlace
        }

        it("If river enabled source should be first") {
            val deck = DefaultDeck().withExtensions(River)
            deck.remainingTiles().next() shouldEqual TileBB6F1
        }

        it("Amount of tiles in basic deck") {
            val deck = DefaultDeck()
            deck.remainingTiles().size() shouldEqual 72
        }

        it("Amount of tiles in game with river extension") {
            val deck = DefaultDeck().withExtensions(River)
            deck.remainingTiles().size() shouldEqual 84
        }

        it("If river enabled estuary should be last of river tiles") {
            val deck = DefaultDeck().withExtensions(River)
            val remainingTiles = deck.remainingTiles()
            (1..11).forEach { _ -> remainingTiles.next() }
            remainingTiles.next() shouldEqual TileBB6F12
        }

    }
})
