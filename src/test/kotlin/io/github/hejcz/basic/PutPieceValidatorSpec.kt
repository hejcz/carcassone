package io.github.hejcz.basic

import io.github.hejcz.basic.tiles.*
import io.github.hejcz.core.*
import io.github.hejcz.helper.Players
import io.github.hejcz.helper.TestBasicRemainingTiles
import io.github.hejcz.helper.TestGameSetup
import org.amshove.kluent.shouldContain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object PutPieceValidatorSpec : Spek({

    // TODO fix tests names
    describe("Putting handlers in invalid places") {

        fun game() = Game(Players.singlePlayer(), TestGameSetup(TestBasicRemainingTiles(TileD)))

        it("monk on tile without cloister") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Monk)) shouldContain PiecePlacedInInvalidPlace
        }

        it("knight on road") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Right))) shouldContain PiecePlacedInInvalidPlace
        }

        it("knight on road 2") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Left))) shouldContain PiecePlacedInInvalidPlace
        }

        it("knight on green field") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Down))) shouldContain PiecePlacedInInvalidPlace
        }

        it("knight on green field") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Down))) shouldContain PiecePlacedInInvalidPlace
        }

        it("brigand in castle") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Brigand(Up))) shouldContain PiecePlacedInInvalidPlace
        }

        it("brigand on green field") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Brigand(Down))) shouldContain PiecePlacedInInvalidPlace
        }

        it("peasant in castle") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Peasant(Location(Up)))) shouldContain PiecePlacedInInvalidPlace
        }

        it("peasant on road") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Peasant(Location(Right)))) shouldContain PiecePlacedInInvalidPlace
        }

        it("peasant on road 2") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Peasant(Location(Left)))) shouldContain PiecePlacedInInvalidPlace

        }

    }

})
