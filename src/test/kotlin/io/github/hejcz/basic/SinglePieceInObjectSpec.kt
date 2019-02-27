package io.github.hejcz.basic

import io.github.hejcz.PiecePlacedInInvalidPlace
import io.github.hejcz.PutPiece
import io.github.hejcz.PutTile
import io.github.hejcz.engine.Game
import io.github.hejcz.helper.Players
import io.github.hejcz.helper.TestBasicRemainingTiles
import io.github.hejcz.helper.TestGameSetup
import io.github.hejcz.mapples.Brigand
import io.github.hejcz.mapples.Knight
import io.github.hejcz.mapples.Mapple
import io.github.hejcz.mapples.Peasant
import io.github.hejcz.placement.*
import io.github.hejcz.rules.basic.RoadCompletedRule
import io.github.hejcz.tiles.basic.Tile
import io.github.hejcz.tiles.basic.TileD
import io.github.hejcz.tiles.basic.TileF
import io.github.hejcz.tiles.basic.TileV
import org.amshove.kluent.shouldContain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object SinglePieceInObjectSpec : Spek({

    // TODO fix tests names
    describe("Putting pieces in taken object") {

        fun singlePlayer(vararg tiles: Tile) = Game(
            emptySet(),
            emptySet(),
            Players.singlePlayer(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        )

        fun multiPlayer(vararg tiles: Tile) = Game(
            emptySet(),
            emptySet(),
            Players.twoPlayers(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        )

        it("knights") {
            val game = singlePlayer(TileF, TileD)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(Mapple, Knight(Down)))
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(PutPiece(Mapple, Knight(Down))) shouldContain PiecePlacedInInvalidPlace
        }

        it("knights two players") {
            val game = multiPlayer(TileF, TileD)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(Mapple, Knight(Down)))
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            game.dispatch(PutPiece(Mapple, Knight(Down))) shouldContain PiecePlacedInInvalidPlace
        }

        it("brigands") {
            val game = singlePlayer(TileD, TileV)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Brigand(Right)))
            game.dispatch(PutTile(Position(2, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Brigand(Down))) shouldContain PiecePlacedInInvalidPlace
        }

        it("brigands two players") {
            val game = multiPlayer(TileD, TileV)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Brigand(Right)))
            game.dispatch(PutTile(Position(2, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Brigand(Down))) shouldContain PiecePlacedInInvalidPlace
        }

        it("peasants") {
            val game = singlePlayer(TileD, TileV)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Peasant(Location(Right, LeftSide))))
            game.dispatch(PutTile(Position(2, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Peasant(Location(Right)))) shouldContain PiecePlacedInInvalidPlace
        }

        it("peasants two players") {
            val game = multiPlayer(TileD, TileV)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Peasant(Location(Right, LeftSide))))
            game.dispatch(PutTile(Position(2, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Peasant(Location(Right)))) shouldContain PiecePlacedInInvalidPlace
        }

    }

})
