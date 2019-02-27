package io.github.hejcz.basic

import io.github.hejcz.NoMappleAvailable
import io.github.hejcz.PutPiece
import io.github.hejcz.PutTile
import io.github.hejcz.SkipPiece
import io.github.hejcz.engine.Game
import io.github.hejcz.engine.Player
import io.github.hejcz.helper.TestBasicRemainingTiles
import io.github.hejcz.helper.TestGameSetup
import io.github.hejcz.helper.shouldContainPlaceTileOnly
import io.github.hejcz.mapples.Brigand
import io.github.hejcz.mapples.Knight
import io.github.hejcz.mapples.Mapple
import io.github.hejcz.mapples.Peasant
import io.github.hejcz.placement.*
import io.github.hejcz.rules.basic.CastleCompletedRule
import io.github.hejcz.tiles.basic.*
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import org.spekframework.spek2.style.specification.describe

object MappleAvailabilityValidatorSpec : Spek({

    describe("Game") {

        fun playerWithSinglePiece() = Player(id = 1, order = 1, mapples = listOf(Mapple))

        fun playerWithTwoPieces() = Player(id = 1, order = 1, mapples = listOf(Mapple, Mapple))

        fun game(player: Player, vararg tiles: Tile) = Game(
            setOf(CastleCompletedRule),
            emptySet(),
            setOf(player),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        )

        it("should not allow to put piece player does not have available") {
            val game = game(playerWithSinglePiece(), TileD, TileD)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Knight(Up)))
            game.dispatch(PutTile(Position(2, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Brigand(Left))) shouldContain NoMappleAvailable(Mapple)
        }

        it("should allow to put piece player received from completed object") {
            val game = game(playerWithSinglePiece(), TileD, TileD, TileD)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Knight(Up)))
            game.dispatch(PutTile(Position(1, 1), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(2, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Brigand(Left))) shouldEqual emptyList()
        }

        it("should restore all pieces player placed on object") {
            val game = game(playerWithTwoPieces(), TileG, TileV, TileE, TileM, TileU, TileV, TileD)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(Mapple, Knight(Up)))
            game.dispatch(PutTile(Position(1, 1), Rotation270))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 2), Rotation270))
            game.dispatch(PutPiece(Mapple, Knight(Left)))
            game.dispatch(PutTile(Position(0, 2), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(2, 1), Rotation90))
            game.dispatch(PutPiece(Mapple, Brigand(Left))).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(Mapple, Peasant(Location(Right, RightSide)))).shouldContainPlaceTileOnly()
        }

    }

})
