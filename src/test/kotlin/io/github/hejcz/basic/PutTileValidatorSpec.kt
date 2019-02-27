package io.github.hejcz.basic

import io.github.hejcz.PutTile
import io.github.hejcz.SkipPiece
import io.github.hejcz.TilePlacedInInvalidPlace
import io.github.hejcz.engine.Game
import io.github.hejcz.helper.Players
import io.github.hejcz.helper.TestBasicRemainingTiles
import io.github.hejcz.helper.TestGameSetup
import io.github.hejcz.helper.shouldContainSelectPieceOnly
import io.github.hejcz.placement.*
import io.github.hejcz.rules.basic.CastleCompletedRule
import io.github.hejcz.rules.basic.PlainCompletedRule
import io.github.hejcz.tiles.basic.Tile
import io.github.hejcz.tiles.basic.TileA
import io.github.hejcz.tiles.basic.TileI
import org.amshove.kluent.shouldContain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import org.spekframework.spek2.style.specification.describe

object PutTileValidatorSpec : Spek({

    // TODO fix tests names
    describe("Putting tile in invalid places") {

        fun singlePlayer(vararg tiles: Tile) = Game(
            emptySet(),
            emptySet(),
            Players.singlePlayer(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        )

        it("roads validation") {
            val game = singlePlayer(TileA)
            game.dispatch(PutTile(Position(1, 0), NoRotation)) shouldContain TilePlacedInInvalidPlace
            game.dispatch(PutTile(Position(1, 0), Rotation180)) shouldContain TilePlacedInInvalidPlace
            game.dispatch(PutTile(Position(1, 0), Rotation270)) shouldContain TilePlacedInInvalidPlace
            game.dispatch(PutTile(Position(1, 0), Rotation90)).shouldContainSelectPieceOnly()
        }

        it("Connecting tiles with bounded object") {
            val game = singlePlayer(TileI, TileI)
            game.dispatch(PutTile(Position(0, -1), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, -2), Rotation180)) shouldContain TilePlacedInInvalidPlace
        }

    }

})
