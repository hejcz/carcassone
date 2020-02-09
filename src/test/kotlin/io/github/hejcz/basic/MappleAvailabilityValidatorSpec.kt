package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object MappleAvailabilityValidatorSpec : Spek({

    describe("Game") {

        fun playerWithSinglePiece() = Player(id = 1, order = 1, initialPieces = listOf(SmallPiece))

        fun playerWithTwoPieces() = Player(id = 1, order = 1, initialPieces = listOf(SmallPiece, SmallPiece))

        fun game(player: Player, vararg tiles: Tile) =
            Game(setOf(player), TestGameSetup(TestBasicRemainingTiles(*tiles))).apply { dispatch(Begin) }

        it("should not allow to put piece player does not have available") {
            GameScenario(game(playerWithSinglePiece(), TileD, TileD))
                .then(PutTile(Position(1, 0), NoRotation))
                .then(PutPiece(SmallPiece, Knight(Up)))
                .then(PutTile(Position(2, 0), NoRotation))
                .then(PutPiece(SmallPiece, Brigand(Left)))
                .thenReceivedEventShouldBe(NoMappleAvailable(SmallPiece))
        }

        it("should allow to put piece player received from completed object") {
            GameScenario(game(playerWithSinglePiece(), TileD, TileD, TileD))
                .then(PutTile(Position(1, 0), NoRotation))
                .then(PutPiece(SmallPiece, Knight(Up)))
                .then(PutTile(Position(1, 1), Rotation180))
                .then(SkipPiece)
                .then(PutTile(Position(2, 0), NoRotation))
                .then(PutPiece(SmallPiece, Brigand(Left)))
                .thenReceivedEventShouldBe(PlayerScored(1, 3, emptySet()))
        }

        it("should restore all handlers player placed on object") {
            GameScenario(game(playerWithTwoPieces(), TileG, TileV, TileE, TileM, TileU, TileV, TileD))
                .then(PutTile(Position(0, 1), Rotation90))
                .then(PutPiece(SmallPiece, Knight(Up)))
                .then(PutTile(Position(1, 1), Rotation270))
                .then(SkipPiece)
                .then(PutTile(Position(1, 2), Rotation270))
                .then(PutPiece(SmallPiece, Knight(Left)))
                .then(PutTile(Position(0, 2), Rotation90))
                .then(SkipPiece)
                .then(PutTile(Position(2, 1), Rotation90))
                .then(PutPiece(SmallPiece, Brigand(Left))).shouldContainPlaceTileOnly()
                .then(PutTile(Position(0, 1), Rotation90))
                .then(PutPiece(SmallPiece, Peasant(Location(Right, RightSide)))).shouldContainPlaceTileOnly()
        }
    }
})
