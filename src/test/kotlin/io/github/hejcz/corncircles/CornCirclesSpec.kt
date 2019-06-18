package io.github.hejcz.corncircles

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.*
import org.amshove.kluent.*
import org.spekframework.spek2.*
import org.spekframework.spek2.style.specification.*

object CornCirclesSpec : Spek({

    fun threePlayerGame(vararg tiles: Tile) = Game(
        setOf(Player(id = 1, order = 1), Player(id = 2, order = 2), Player(id = 3, order = 3)),
        CornCirclesGameSetup(TestBasicRemainingTiles(*tiles))
    ).apply { dispatch(Begin) }

    describe("examples from rule book") {

        it("first example") {
            val game = threePlayerGame(TileL, TileQ, TileW, TileE, TileE, TileH, TileG, TileU, TileU, Korn6, TileU)
            // red
            game.dispatch(PutTile(Position(-1, 0), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(SmallPiece, Peasant(Location(Down, LeftSide)))).shouldContainPlaceTileOnly()
            // green
            game.dispatch(PutTile(Position(-1, 1), Rotation270)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(SmallPiece, Knight(Up))).shouldContainPlaceTileOnly()
            // blue
            game.dispatch(PutTile(Position(-2, 0), Rotation270)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            // red
            game.dispatch(PutTile(Position(-3, 0), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            // green
            game.dispatch(PutTile(Position(-4, 0), Rotation270)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            // blue
            game.dispatch(PutTile(Position(-4, 1), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(SmallPiece, Peasant(Location(Down)))).shouldContainPlaceTileOnly()
            // red
            game.dispatch(PutTile(Position(-5, 1), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(SmallPiece, Knight(Right))).shouldContainPlaceTileOnly()
            // green
            game.dispatch(PutTile(Position(-1, -1), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            // blue
            game.dispatch(PutTile(Position(-2, -1), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            // red - Korn1
            game.dispatch(PutTile(Position(-3, 1), NoRotation)).shouldContainSelectPieceOnly()
            val events = game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            events.size.shouldBe(1)
            events.shouldContain(ChooseCornAction(1))
            // action
            game.dispatch(ChooseCornCircleActionCommand(CornCircleAction.ADD_PIECE)).shouldContain(AddPiece(2))
            game.dispatch(AddPieceCommand(Position(-1, 1), SmallPiece, Knight(Up))).shouldContain(AddPiece(3))
            game.dispatch(AvoidCornCircleAction).shouldContain(AddPiece(1))
            game.dispatch(AddPieceCommand(Position(-3, 1), SmallPiece, Knight(Down))).shouldContainPlaceTileOnly()
        }

        // TODO what if korn 6 is last - game should not end immediately
        // TODO add piece with different role
        // TODO expectation validator passes but then other validator fails

    }

})
