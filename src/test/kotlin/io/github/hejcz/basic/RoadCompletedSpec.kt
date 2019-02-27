package io.github.hejcz.basic

import io.github.hejcz.basic.tiles.Tile
import io.github.hejcz.basic.tiles.TileS
import io.github.hejcz.basic.tiles.TileV
import io.github.hejcz.core.*
import io.github.hejcz.helper.Players
import io.github.hejcz.helper.TestBasicRemainingTiles
import io.github.hejcz.helper.TestGameSetup
import io.github.hejcz.helper.shouldContainSelectPieceOnly
import org.amshove.kluent.shouldContain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object RoadCompletedSpec : Spek({

    // TODO fix tests names
    describe("Castle completed rule") {

        fun singlePlayer(vararg tiles: Tile) =
            Game(Players.singlePlayer(), TestGameSetup(TestBasicRemainingTiles(*tiles)))

        it("Simple road example") {
            val game = singlePlayer(TileS, TileS)
            game.dispatch(PutTile(Position(1, 0), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Brigand(Left)))
            game.dispatch(PutTile(Position(-1, 0), Rotation270)) shouldContain PlayerScored(1, 3, listOf(SmallPiece))
        }

        it("Simple road example with piece placed after tile") {
            val game = singlePlayer(TileS, TileS)
            game.dispatch(PutTile(Position(1, 0), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, 0), Rotation270)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(SmallPiece, Brigand(Right))) shouldContain PlayerScored(1, 3, listOf(SmallPiece))
        }

        it("Simple road 3") {
            val game = singlePlayer(TileS, TileV, TileS)
            game.dispatch(PutTile(Position(-1, 0), Rotation270))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Brigand(Left)))
            game.dispatch(PutTile(Position(1, -1), Rotation180)) shouldContain PlayerScored(1, 4, listOf(SmallPiece))
        }

    }

})
