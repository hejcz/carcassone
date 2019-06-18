package io.github.hejcz.abbot

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.AbbotTestGameSetup
import io.github.hejcz.helper.TestBasicRemainingTiles
import io.github.hejcz.helper.shouldContainPlaceTileOnly
import io.github.hejcz.helper.shouldContainSelectPieceOnly
import org.amshove.kluent.shouldContain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object AbbotExtensionSpec : Spek({

    describe("Place") {

        fun singlePlayer(vararg tiles: Tile) = Game(
            setOf(Player(id = 1, order = 1, initialPieces = listOf(SmallPiece, AbbotPiece))),
            AbbotTestGameSetup(TestBasicRemainingTiles(*tiles))
        ).apply { dispatch(Begin) }

        it("should not be able to place another small piece if only abbot is available") {
            val game = singlePlayer(TileG, TileEWithGarden)
            game.dispatch(PutTile(Position(0, 1), Rotation90)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(SmallPiece, Knight(Down))).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(0, -1), Rotation180)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(SmallPiece, Knight(Down))) shouldContain NoMappleAvailable(SmallPiece)
        }

        it("should be able to place abbot") {
            val game = singlePlayer(TileG, TileEWithGarden, TileR)
            game.dispatch(PutTile(Position(0, 1), Rotation90)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(SmallPiece, Knight(Down))).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(0, -1), Rotation180)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(AbbotPiece, Abbot)).shouldContainPlaceTileOnly()
        }

        it("should be able to place small piece after abbot") {
            val game = singlePlayer(TileEWithGarden, TileG, TileR)
            game.dispatch(PutTile(Position(0, -1), Rotation180)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(AbbotPiece, Abbot)).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(0, 1), Rotation90)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(SmallPiece, Knight(Down))).shouldContainPlaceTileOnly()
        }

        it("should not be able to place another abbot") {
            val game = singlePlayer(TileEWithGarden, TileEWithGarden)
            game.dispatch(PutTile(Position(0, 1), Rotation180)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(AbbotPiece, Abbot)).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(0, 2), Rotation90)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(AbbotPiece, Abbot)) shouldContain NoMappleAvailable(AbbotPiece)
        }

        it("should be able to put Abbot as a Monk in a cloister") {
            val game = singlePlayer(TileB, TileD)
            game.dispatch(PutTile(Position(0, -1), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(AbbotPiece, Monk)).shouldContainPlaceTileOnly()
        }

        it("should not be able to use Abbot as a knight") {
            val game = singlePlayer(TileG, TileB)
            game.dispatch(PutTile(Position(0, 1), Rotation90)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(AbbotPiece, Knight(Down))) shouldContain PieceMayNotBeUsedInRole(AbbotPiece, Knight(Down))
        }

        it("should not be able to use Abbot as a brigand") {
            val game = singlePlayer(TileD, TileB)
            game.dispatch(PutTile(Position(1, 0), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(AbbotPiece, Brigand(Left))) shouldContain PieceMayNotBeUsedInRole(AbbotPiece, Brigand(Left))
        }

        it("should not be able to use Abbot as a peasant") {
            val game = singlePlayer(TileB, TileD)
            game.dispatch(PutTile(Position(0, -1), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(AbbotPiece, Peasant(Location(Down)))) shouldContain
                PieceMayNotBeUsedInRole(AbbotPiece, Peasant(Location(Down)))
        }

        it("should not be able to use Small piece as an Abbot") {
            val game = singlePlayer(TileEWithGarden, TileD)
            game.dispatch(PutTile(Position(0, -1), Rotation180)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(SmallPiece, Abbot)) shouldContain PieceMayNotBeUsedInRole(SmallPiece, Abbot)
        }

        it("should be able to retrieve Abbot after finished cloister") {
            val game = singlePlayer(TileD, TileD, TileB, TileB, TileB, TileB, TileB, TileB, TileB)
            game.dispatch(PutTile(Position(-1, 0), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(1, 0), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(-1, -1), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(0, -1), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(AbbotPiece, Monk)).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(1, -1), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(-1, -2), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(0, -2), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(1, -2), NoRotation)) shouldContain PlayerScored(1, 9, setOf(PieceOnBoard(Position(0, -1), AbbotPiece, Monk)))
        }

        it("should score for Abbot after finished garden") {
            val game = singlePlayer(TileD, TileD, TileB, TileEWithGarden, TileB, TileB, TileE, TileB, TileB)
            game.dispatch(PutTile(Position(-1, 0), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(1, 0), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(-1, -1), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(0, -1), Rotation180)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(AbbotPiece, Abbot)).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(1, -1), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(-1, -2), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(0, -2), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(1, -2), NoRotation)) shouldContain PlayerScored(1, 9, setOf(PieceOnBoard(Position(0, -1), AbbotPiece, Abbot)))
        }

        it("should score for Abbot added as the last tile before garden completion") {
            val game = singlePlayer(TileD, TileD, TileB, TileB, TileB, TileE, TileB, TileEWithGarden, TileB)
            game.dispatch(PutTile(Position(-1, 0), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(1, 0), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(-1, -1), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(1, -1), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(-1, -2), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(0, -2), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(1, -2), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(0, -1), Rotation180)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(AbbotPiece, Abbot)) shouldContain PlayerScored(1, 9, setOf(PieceOnBoard(Position(0, -1), AbbotPiece, Abbot)))
        }

        it("should score for incomplete garden at the game end") {
            val game = singlePlayer(TileD, TileD, TileB, TileB, TileB, TileE, TileEWithGarden)
            game.dispatch(PutTile(Position(-1, 0), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(1, 0), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(-1, -1), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(1, -1), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(-1, -2), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(0, -2), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(0, -1), Rotation180)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(AbbotPiece, Abbot)) shouldContain PlayerScored(1, 8, emptySet())
        }

        it("should be able to pick Abbot instead of placing piece") {
            val game = singlePlayer(TileEWithGarden, TileD, TileD)
            game.dispatch(PutTile(Position(0, -1), Rotation90)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(AbbotPiece, Abbot)).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(0, -2), Rotation180)).shouldContainSelectPieceOnly()
            game.dispatch(PickUpAbbot(Position(0, -1))) shouldContain PlayerScored(1, 3, setOf(PieceOnBoard(Position(0, -1), AbbotPiece, Abbot)))
        }

        it("should be able to pick Abbot instead of placing piece") {
            val game = singlePlayer(TileEWithGarden, TileD, TileD)
            game.dispatch(PutTile(Position(0, -1), Rotation90)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(AbbotPiece, Abbot)).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(0, 1), Rotation180)).shouldContainSelectPieceOnly()
            game.dispatch(PickUpAbbot(Position(0, -1))) shouldContain PlayerScored(1, 2, setOf(PieceOnBoard(Position(0, -1), AbbotPiece, Abbot)))
        }

        it("should be able to pick Abbot instead of placing piece") {
            val game = singlePlayer(TileD, TileD, TileB, TileEWithGarden, TileB, TileB, TileE, TileB, TileB)
            game.dispatch(PutTile(Position(-1, 0), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(1, 0), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(-1, -1), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(0, -1), Rotation180)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(AbbotPiece, Abbot)).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(1, -1), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(-1, -2), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(0, -2), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(PickUpAbbot(Position(0, -1))) shouldContain PlayerScored(1, 8, setOf(PieceOnBoard(Position(0, -1), AbbotPiece, Abbot)))
        }

        it("should not be able to pick abbot from tile ha didn't put abbot on") {
            val game = singlePlayer(TileEWithGarden, TileD, TileD)
            game.dispatch(PutTile(Position(0, -1), Rotation90)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(SmallPiece, Knight(Right))).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(0, 1), Rotation180)).shouldContainSelectPieceOnly()
            game.dispatch(PickUpAbbot(Position(0, -1))) shouldContain NoAbbotToPickUp
        }

        it("should be able to pick up abbot in monk role") {
            val game = singlePlayer(TileB, TileD, TileD)
            game.dispatch(PutTile(Position(0, -1), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(AbbotPiece, Abbot)).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(1, -1), Rotation90)).shouldContainSelectPieceOnly()
            game.dispatch(PickUpAbbot(Position(0, -1))) shouldContain PlayerScored(1, 3, setOf(PieceOnBoard(Position(0, -1), AbbotPiece, Abbot)))
        }
    }
})
