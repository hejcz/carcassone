package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.TestBasicRemainingTiles
import io.github.hejcz.helper.TestGameSetup
import io.github.hejcz.helper.shouldContainPlaceTileOnly
import org.amshove.kluent.shouldContain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object MappleAvailabilityValidatorSpec : Spek({

    describe("Game") {

        fun playerWithSinglePiece() = Player(id = 1, order = 1, initialPieces = listOf(SmallPiece))

        fun playerWithTwoPieces() = Player(id = 1, order = 1, initialPieces = listOf(SmallPiece, SmallPiece))

        fun game(player: Player, vararg tiles: Tile) =
            Game(setOf(player), TestGameSetup(TestBasicRemainingTiles(*tiles)))

        it("should not allow to put piece player does not have available") {
            val game = game(playerWithSinglePiece(), TileD, TileD)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Up)))
            game.dispatch(PutTile(Position(2, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Brigand(Left))) shouldContain NoMappleAvailable(SmallPiece)
        }

        it("should allow to put piece player received from completed object") {
            val game = game(playerWithSinglePiece(), TileD, TileD, TileD)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Up)))
            game.dispatch(PutTile(Position(1, 1), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(2, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Brigand(Left))) shouldContain PlayerScored(1, 3, emptySet())
        }

        it("should restore all handlers player placed on object") {
            val game = game(playerWithTwoPieces(), TileG, TileV, TileE, TileM, TileU, TileV, TileD)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Knight(Up)))
            game.dispatch(PutTile(Position(1, 1), Rotation270))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 2), Rotation270))
            game.dispatch(PutPiece(SmallPiece, Knight(Left)))
            game.dispatch(PutTile(Position(0, 2), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(2, 1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Brigand(Left))).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Peasant(Location(Right, RightSide)))).shouldContainPlaceTileOnly()
        }
    }
})
