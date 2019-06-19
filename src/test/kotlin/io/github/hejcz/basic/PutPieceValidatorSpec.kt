package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.Players
import io.github.hejcz.helper.TestBasicRemainingTiles
import io.github.hejcz.helper.TestGameSetup
import org.amshove.kluent.shouldContain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object PutPieceValidatorSpec : Spek({

    describe("Putting handlers in invalid places") {

        fun game() = Game(Players.singlePlayer(), TestGameSetup(TestBasicRemainingTiles(TileD))).apply { dispatch(Begin) }

        it("monk on tile without cloister") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Monk)) shouldContain InvalidPieceLocation
        }

        it("knight on road") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Right))) shouldContain InvalidPieceLocation
        }

        it("knight on road 2") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Left))) shouldContain InvalidPieceLocation
        }

        it("knight on green field") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Down))) shouldContain InvalidPieceLocation
        }

        it("knight on green field") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Down))) shouldContain InvalidPieceLocation
        }

        it("brigand in castle") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Brigand(Up))) shouldContain InvalidPieceLocation
        }

        it("brigand on green field") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Brigand(Down))) shouldContain InvalidPieceLocation
        }

        it("peasant in castle") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Peasant(Location(Up)))) shouldContain InvalidPieceLocation
        }

        it("peasant on road") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Peasant(Location(Right)))) shouldContain InvalidPieceLocation
        }

        it("peasant on road 2") {
            val game = game()
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Peasant(Location(Left)))) shouldContain InvalidPieceLocation
        }
    }
})
