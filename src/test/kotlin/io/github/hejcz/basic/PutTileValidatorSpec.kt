package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.Players
import io.github.hejcz.helper.TestBasicRemainingTiles
import io.github.hejcz.helper.TestGameSetup
import io.github.hejcz.helper.shouldContainSelectPieceOnly
import org.amshove.kluent.shouldContain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object PutTileValidatorSpec : Spek({

    describe("Putting tile in invalid places") {

        fun singlePlayer(vararg tiles: Tile) =
            Game(Players.singlePlayer(), TestGameSetup(TestBasicRemainingTiles(*tiles))).apply { dispatch(Begin) }

        it("roads validation") {
            val game = singlePlayer(TileA)
            game.dispatch(PutTile(Position(1, 0), NoRotation)) shouldContain InvalidTileLocation
        }

        it("roads validation") {
            val game = singlePlayer(TileA)
            game.dispatch(PutTile(Position(1, 0), Rotation180)) shouldContain InvalidTileLocation
        }

        it("roads validation") {
            val game = singlePlayer(TileA)
            game.dispatch(PutTile(Position(1, 0), Rotation270)) shouldContain InvalidTileLocation
        }

        it("roads validation") {
            val game = singlePlayer(TileA)
            game.dispatch(PutTile(Position(1, 0), Rotation90)).shouldContainSelectPieceOnly()
        }

        it("Connecting tiles with bounded object") {
            val game = singlePlayer(TileI, TileI)
            game.dispatch(PutTile(Position(0, -1), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, -2), Rotation180)) shouldContain InvalidTileLocation
        }
    }
})
