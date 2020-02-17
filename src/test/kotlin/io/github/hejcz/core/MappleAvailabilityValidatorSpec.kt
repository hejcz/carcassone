package io.github.hejcz.core

import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object MappleAvailabilityValidatorSpec : Spek({

    fun playerWithSinglePiece() = Player(id = 1, order = 1, initialPieces = listOf(SmallPiece))

    fun playerWithTwoPieces() = Player(id = 1, order = 1, initialPieces = listOf(SmallPiece, SmallPiece))

    fun game(player: Player, vararg tiles: Tile) =
        Game(setOf(player), TestGameSetup(TestBasicRemainingTiles(*tiles))).dispatch(BeginCmd)

    describe("Game") {

        it("should not allow to put piece player does not have available") {
            GameScenario(game(playerWithSinglePiece(), TileD, TileD))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(2, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Brigand(Left)))
                .thenReceivedEventShouldBe(NoMappleEvent(SmallPiece))
        }

        it("should allow to put piece player received from completed object") {
            GameScenario(game(playerWithSinglePiece(), TileD, TileD, TileD))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(1, 1), Rotation180))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(2, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Brigand(Left)))
                .thenReceivedEventShouldBe(ScoreEvent(1, 3, emptySet()))
        }

        it("should restore all handlers player placed on object") {
            GameScenario(game(playerWithTwoPieces(), TileG, TileV, TileE, TileM, TileU, TileV, TileD))
                .then(TileCmd(Position(0, 1), Rotation90))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(1, 1), Rotation270))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 2), Rotation270))
                .then(PieceCmd(SmallPiece, Knight(Left)))
                .then(TileCmd(Position(0, 2), Rotation90))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(2, 1), Rotation90))
                .then(PieceCmd(SmallPiece, Brigand(Left))).thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(1, 0), Rotation90))
                .then(PieceCmd(SmallPiece, Peasant(Location(Right))))
                .thenReceivedEventShouldBeOnlyPlaceTile()
        }
    }
})
