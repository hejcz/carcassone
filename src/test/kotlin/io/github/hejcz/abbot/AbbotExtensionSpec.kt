package io.github.hejcz.abbot

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.*
import io.github.hejcz.helper.GameScenario
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object AbbotExtensionSpec : Spek({

    describe("Place") {

        fun singlePlayer(vararg tiles: Tile) = Game(
            setOf(Player(id = 1, order = 1, initialPieces = listOf(SmallPiece, AbbotPiece))),
            AbbotTestGameSetup(TestBasicRemainingTiles(*tiles))
        ).dispatch(BeginCmd)

        it("should not be able to place small piece if only abbot is available") {
            GameScenario(singlePlayer(TileG, TileEWithGarden))
                .then(TileCmd(Position(0, 1), Rotation90))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PieceCmd(SmallPiece, Knight(Down)))
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, -1), Rotation180))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PieceCmd(SmallPiece, Knight(Down)))
                .thenReceivedEventShouldBe(NoMappleAvailable(SmallPiece))
        }

        it("should be able to place abbot") {
            GameScenario(singlePlayer(TileG, TileEWithGarden, TileR))
                .then(TileCmd(Position(0, 1), Rotation90))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PieceCmd(SmallPiece, Knight(Down)))
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, -1), Rotation180))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PieceCmd(AbbotPiece, Abbot))
                .thenReceivedEventShouldBeOnlyPlaceTile()
        }

        it("should be able to place small piece after abbot") {
            GameScenario(singlePlayer(TileEWithGarden, TileG, TileR))
                .then(TileCmd(Position(0, -1), Rotation180))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PieceCmd(AbbotPiece, Abbot))
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, 1), Rotation90))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PieceCmd(SmallPiece, Knight(Down)))
                .thenReceivedEventShouldBeOnlyPlaceTile()
        }

        it("should not be able to place second abbot") {
            GameScenario(singlePlayer(TileEWithGarden, TileEWithGarden))
                .then(TileCmd(Position(0, 1), Rotation180))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PieceCmd(AbbotPiece, Abbot))
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, 2), Rotation90))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PieceCmd(AbbotPiece, Abbot))
                .thenReceivedEventShouldBe(NoMappleAvailable(AbbotPiece))
        }

        it("should be able to put Abbot as a Monk in a cloister") {
            GameScenario(singlePlayer(TileB, TileD))
                .then(TileCmd(Position(0, -1), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PieceCmd(AbbotPiece, Monk))
                .thenReceivedEventShouldBeOnlyPlaceTile()
        }

        it("should not be able to use Abbot as a knight") {
            GameScenario(singlePlayer(TileG, TileB))
                .then(TileCmd(Position(0, 1), Rotation90))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PieceCmd(AbbotPiece, Knight(Down)))
                .thenReceivedEventShouldBe(PieceMayNotBeUsedInRole(AbbotPiece, Knight(Down)))
        }

        it("should not be able to use Abbot as a brigand") {
            GameScenario(singlePlayer(TileD, TileB))
                .then(TileCmd(Position(1, 0), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PieceCmd(AbbotPiece, Brigand(Left)))
                .thenReceivedEventShouldBe(PieceMayNotBeUsedInRole(AbbotPiece, Brigand(Left)))
        }

        it("should not be able to use Abbot as a peasant") {
            GameScenario(singlePlayer(TileB, TileD))
                .then(TileCmd(Position(0, -1), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PieceCmd(AbbotPiece, Peasant(Location(Down))))
                .thenReceivedEventShouldBe(PieceMayNotBeUsedInRole(AbbotPiece, Peasant(Location(Down))))
        }

        it("should not be able to use Small piece as an Abbot") {
            GameScenario(singlePlayer(TileEWithGarden, TileD))
                .then(TileCmd(Position(0, -1), Rotation180))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PieceCmd(SmallPiece, Abbot))
                .thenReceivedEventShouldBe(PieceMayNotBeUsedInRole(SmallPiece, Abbot))
        }

        it("should be able to retrieve Abbot after finished cloister") {
            GameScenario(
                singlePlayer(
                    TileD,
                    TileD,
                    TileB,
                    TileB,
                    TileB,
                    TileB,
                    TileB,
                    TileB,
                    TileB
                )
            )
                .then(TileCmd(Position(-1, 0), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(1, 0), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(-1, -1), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, -1), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PieceCmd(AbbotPiece, Monk))
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(1, -1), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(-1, -2), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, -2), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(1, -2), NoRotation))
                .thenReceivedEventShouldBe(PlayerScored(1, 9, setOf(PieceOnBoard(Position(0, -1), AbbotPiece, Monk))))
        }

        it("should score for Abbot after finished garden") {
            GameScenario(
                singlePlayer(
                    TileD,
                    TileD,
                    TileB,
                    TileEWithGarden,
                    TileB,
                    TileB,
                    TileE,
                    TileB,
                    TileB
                )
            )
                .then(TileCmd(Position(-1, 0), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(1, 0), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(-1, -1), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, -1), Rotation180))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PieceCmd(AbbotPiece, Abbot))
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(1, -1), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(-1, -2), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, -2), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(1, -2), NoRotation))
                .thenReceivedEventShouldBe(PlayerScored(1, 9, setOf(PieceOnBoard(Position(0, -1), AbbotPiece, Abbot))))
        }

        it("should score for Abbot added as the last tile before garden completion") {
            GameScenario(
                singlePlayer(
                    TileD,
                    TileD,
                    TileB,
                    TileB,
                    TileB,
                    TileE,
                    TileB,
                    TileEWithGarden,
                    TileB
                )
            )
                .then(TileCmd(Position(-1, 0), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(1, 0), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(-1, -1), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(1, -1), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(-1, -2), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, -2), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(1, -2), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, -1), Rotation180))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PieceCmd(AbbotPiece, Abbot))
                .thenReceivedEventShouldBe(PlayerScored(1, 9, setOf(PieceOnBoard(Position(0, -1), AbbotPiece, Abbot))))
        }

        it("should score for incomplete garden at the game end") {
            GameScenario(
                singlePlayer(
                    TileD,
                    TileD,
                    TileB,
                    TileB,
                    TileB,
                    TileE,
                    TileEWithGarden
                )
            )
                .then(TileCmd(Position(-1, 0), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(1, 0), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(-1, -1), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(1, -1), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(-1, -2), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, -2), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, -1), Rotation180))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PieceCmd(AbbotPiece, Abbot))
                .thenReceivedEventShouldBe(PlayerScored(1, 8, emptySet()))
        }

        it("should be able to pick Abbot instead of placing piece") {
            GameScenario(singlePlayer(TileEWithGarden, TileD, TileD))
                .then(TileCmd(Position(0, -1), Rotation90))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PieceCmd(AbbotPiece, Abbot))
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, -2), Rotation180))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PickUpAbbotCmd(Position(0, -1)))
                .thenReceivedEventShouldBe(PlayerScored(1, 3, setOf(PieceOnBoard(Position(0, -1), AbbotPiece, Abbot))))
        }

        it("should be able to pick Abbot instead of placing piece") {
            GameScenario(singlePlayer(TileEWithGarden, TileD, TileD))
                .then(TileCmd(Position(0, -1), Rotation90))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PieceCmd(AbbotPiece, Abbot))
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, 1), Rotation180))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PickUpAbbotCmd(Position(0, -1)))
                .thenReceivedEventShouldBe(PlayerScored(1, 2, setOf(PieceOnBoard(Position(0, -1), AbbotPiece, Abbot))))
        }

        it("should be able to pick Abbot instead of placing piece") {
            GameScenario(
                singlePlayer(
                    TileD,
                    TileD,
                    TileB,
                    TileEWithGarden,
                    TileB,
                    TileB,
                    TileE,
                    TileB,
                    TileB
                )
            )
                .then(TileCmd(Position(-1, 0), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(1, 0), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(-1, -1), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, -1), Rotation180))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PieceCmd(AbbotPiece, Abbot))
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(1, -1), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(-1, -2), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, -2), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PickUpAbbotCmd(Position(0, -1)))
                .thenReceivedEventShouldBe(PlayerScored(1, 8, setOf(PieceOnBoard(Position(0, -1), AbbotPiece, Abbot))))
        }

        it("should not be able to pick abbot from tile ha didn't put abbot on") {
            GameScenario(singlePlayer(TileEWithGarden, TileD, TileD))
                .then(TileCmd(Position(0, -1), Rotation90))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PieceCmd(SmallPiece, Knight(Right)))
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, 1), Rotation180))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PickUpAbbotCmd(Position(0, -1)))
                .thenReceivedEventShouldBe(NoAbbotToPickUp)
        }

        it("should be able to pick up abbot in monk role") {
            GameScenario(singlePlayer(TileB, TileD, TileD))
                .then(TileCmd(Position(0, -1), NoRotation))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PieceCmd(AbbotPiece, Abbot))
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(1, -1), Rotation90))
                .thenReceivedEventShouldBe(SelectPiece)
                .then(PickUpAbbotCmd(Position(0, -1)))
                .thenReceivedEventShouldBe(PlayerScored(1, 3, setOf(PieceOnBoard(Position(0, -1), AbbotPiece, Abbot))))
        }
    }
})
