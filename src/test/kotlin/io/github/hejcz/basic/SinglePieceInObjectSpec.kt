package io.github.hejcz.basic

import io.github.hejcz.basic.tiles.Tile
import io.github.hejcz.basic.tiles.TileD
import io.github.hejcz.basic.tiles.TileF
import io.github.hejcz.basic.tiles.TileV
import io.github.hejcz.core.*
import io.github.hejcz.helper.Players
import io.github.hejcz.helper.TestBasicRemainingTiles
import io.github.hejcz.helper.TestGameSetup
import org.amshove.kluent.shouldContain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object SinglePieceInObjectSpec : Spek({

    // TODO fix tests names
    describe("Putting handlers in taken object") {

        fun singlePlayer(vararg tiles: Tile) =
            Game(Players.singlePlayer(), TestGameSetup(TestBasicRemainingTiles(*tiles)))

        fun multiPlayer(vararg tiles: Tile) =
            Game(Players.twoPlayers(), TestGameSetup(TestBasicRemainingTiles(*tiles)))

        it("knights") {
            val game = singlePlayer(TileF, TileD)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(PutPiece(SmallPiece, Knight(Down))) shouldContain PiecePlacedInInvalidPlace
        }

        it("knights two players") {
            val game = multiPlayer(TileF, TileD)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(PutPiece(SmallPiece, Knight(Down))) shouldContain PiecePlacedInInvalidPlace
        }

        it("brigands") {
            val game = singlePlayer(TileD, TileV)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Brigand(Right)))
            game.dispatch(PutTile(Position(2, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Brigand(Down))) shouldContain PiecePlacedInInvalidPlace
        }

        it("brigands two players") {
            val game = multiPlayer(TileD, TileV)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Brigand(Right)))
            game.dispatch(PutTile(Position(2, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Brigand(Down))) shouldContain PiecePlacedInInvalidPlace
        }

        it("peasants") {
            val game = singlePlayer(TileD, TileV)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Peasant(Location(Right, LeftSide))))
            game.dispatch(PutTile(Position(2, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Peasant(Location(Right)))) shouldContain PiecePlacedInInvalidPlace
        }

        it("peasants two players") {
            val game = multiPlayer(TileD, TileV)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Peasant(Location(Right, LeftSide))))
            game.dispatch(PutTile(Position(2, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Peasant(Location(Right)))) shouldContain PiecePlacedInInvalidPlace
        }

    }

})
