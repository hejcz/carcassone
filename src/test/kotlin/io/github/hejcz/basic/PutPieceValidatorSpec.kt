package io.github.hejcz.basic

import io.github.hejcz.PiecePlacedInInvalidPlace
import io.github.hejcz.PutPiece
import io.github.hejcz.PutTile
import io.github.hejcz.engine.Game
import io.github.hejcz.helper.Players
import io.github.hejcz.helper.TestBasicRemainingTiles
import io.github.hejcz.helper.TestGameSetup
import io.github.hejcz.mapples.*
import io.github.hejcz.placement.*
import io.github.hejcz.tiles.basic.TileD
import org.amshove.kluent.shouldContain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object PutPieceValidatorSpec : Spek({

    // TODO fix tests names
    describe("Putting pieces in invalid places") {

        fun game() = Game(
            emptySet(),
            emptySet(),
            Players.singlePlayer(),
            TestGameSetup(TestBasicRemainingTiles(TileD))
        )

        it("monk on tile without cloister") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Monk)) shouldContain PiecePlacedInInvalidPlace
        }

        it("knight on road") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Knight(Right))) shouldContain PiecePlacedInInvalidPlace
        }

        it("knight on road 2") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Knight(Left))) shouldContain PiecePlacedInInvalidPlace
        }

        it("knight on green field") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Knight(Down))) shouldContain PiecePlacedInInvalidPlace
        }

        it("knight on green field") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Knight(Down))) shouldContain PiecePlacedInInvalidPlace
        }

        it("brigand in castle") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Brigand(Up))) shouldContain PiecePlacedInInvalidPlace
        }

        it("brigand on green field") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Brigand(Down))) shouldContain PiecePlacedInInvalidPlace
        }

        it("peasant in castle") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Peasant(Location(Up)))) shouldContain PiecePlacedInInvalidPlace
        }

        it("peasant on road") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Peasant(Location(Right)))) shouldContain PiecePlacedInInvalidPlace
        }

        it("peasant on road 2") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Peasant(Location(Left)))) shouldContain PiecePlacedInInvalidPlace

        }

    }

})
