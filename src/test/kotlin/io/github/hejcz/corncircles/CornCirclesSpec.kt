package io.github.hejcz.corncircles

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.*
import io.github.hejcz.inn.*
import io.github.hejcz.inn.tiles.*
import org.amshove.kluent.*
import org.spekframework.spek2.*
import org.spekframework.spek2.style.specification.*

object CornCirclesSpec : Spek({

    fun threePlayerGame(vararg tiles: Tile) = Game(
        setOf(Player(id = 1, order = 1), Player(id = 2, order = 2), Player(id = 3, order = 3)),
        CornCirclesGameSetup(TestBasicRemainingTiles(*tiles))
    ).apply { dispatch(Begin) }

    fun innAndCornTwoPlayersGame(vararg tiles: Tile) = Game(
        setOf(Player(id = 1, order = 1), Player(id = 2, order = 2)),
        TestGameSetup(TestBasicRemainingTiles(*tiles), listOf(CornCirclesExtension, InnAndCathedralsExtension))
    ).apply { dispatch(Begin) }

    describe("examples from rule book") {

        it("first example") {
            val game = threePlayerGame(TileL, TileQ, TileW, TileE, TileE, TileH, TileG, TileU, TileU, Korn6, TileU)
            // red
            game.dispatch(PutTile(Position(-1, 0), NoRotation)).shouldContainSelectPiece()
            game.dispatch(PutPiece(SmallPiece, Peasant(Location(Down, LeftSide)))).shouldContainPlaceTileOnly()
            // green
            game.dispatch(PutTile(Position(-1, 1), Rotation270)).shouldContainSelectPiece()
            game.dispatch(PutPiece(SmallPiece, Knight(Up))).shouldContainPlaceTileOnly()
            // blue
            game.dispatch(PutTile(Position(-2, 0), Rotation270)).shouldContainSelectPiece()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            // red
            game.dispatch(PutTile(Position(-3, 0), NoRotation)).shouldContainSelectPiece()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            // green
            game.dispatch(PutTile(Position(-4, 0), Rotation270)).shouldContainSelectPiece()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            // blue
            game.dispatch(PutTile(Position(-4, 1), NoRotation)).shouldContainSelectPiece()
            game.dispatch(PutPiece(SmallPiece, Peasant(Location(Down)))).shouldContainPlaceTileOnly()
            // red
            game.dispatch(PutTile(Position(-5, 1), NoRotation)).shouldContainSelectPiece()
            game.dispatch(PutPiece(SmallPiece, Knight(Right))).shouldContainPlaceTileOnly()
            // green
            game.dispatch(PutTile(Position(-1, -1), NoRotation)).shouldContainSelectPiece()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            // blue
            game.dispatch(PutTile(Position(-2, -1), NoRotation)).shouldContainSelectPiece()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            // red - Korn1
            game.dispatch(PutTile(Position(-3, 1), NoRotation)).shouldContainSelectPiece()
            val events = game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            events.size.shouldBe(1)
            events.shouldContain(ChooseCornAction(1))
            // action
            game.dispatch(ChooseCornCircleActionCommand(CornCircleAction.ADD_PIECE)).shouldContain(AddPiece(2))
            game.dispatch(AddPieceCommand(Position(-1, 1), SmallPiece, Knight(Up))).shouldContain(AddPiece(3))
            game.dispatch(AvoidCornCircleAction).shouldContain(AddPiece(1))
            game.dispatch(AddPieceCommand(Position(-3, 1), SmallPiece, Knight(Down))).shouldContainPlaceTileOnly()
        }

        it("one player adds big and other one small piece to castle") {
            val game = innAndCornTwoPlayersGame(TileN, TileK, TileEK, Korn5, TileE)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Up)))
            game.dispatch(PutTile(Position(1, 1), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(2, 1), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(ChooseCornCircleActionCommand(CornCircleAction.ADD_PIECE)).shouldContain(AddPiece(1))
            game.dispatch(AddPieceCommand(Position(0, 1), SmallPiece, Knight(Down))).shouldContain(AddPiece(2))
            game.dispatch(AddPieceCommand(Position(1, 0), BigPiece, Knight(Up)))
            val events = game.dispatch(PutTile(Position(1, 2), Rotation180))
            events containsEvent PlayerScored(2, 12, listOf(
                PieceOnBoard(Position(1, 0), BigPiece, Knight(Up)),
                PieceOnBoard(Position(1, 0), SmallPiece, Knight(Up))
            ))
            events containsEvent PlayerDidNotScore(1, listOf(
                PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down)),
                PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down))
            ))
        }

        it("when corn tile is the last time game should continue") {
            val game = innAndCornTwoPlayersGame(Korn6)
            game.dispatch(PutTile(Position(0, 1), NoRotation))
            val events = game.dispatch(SkipPiece)
            events.shouldContain(ChooseCornAction(1))
            events.size shouldEqual 1
        }

        it("can't choose corn action until corn tile is drawn") {
            val game = innAndCornTwoPlayersGame(TileD)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(ChooseCornCircleActionCommand(CornCircleAction.ADD_PIECE)).shouldContain(UnexpectedCommand)
        }

        it("can avoid placing mapple when player has no piece in role specified on tile") {
            val game = innAndCornTwoPlayersGame(TileD, Korn6)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Up)))
            game.dispatch(PutTile(Position(0, 1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(ChooseCornCircleActionCommand(CornCircleAction.ADD_PIECE))
            game.dispatch(AvoidCornCircleAction).shouldContain(PieceCanNotBeSkipped)
        }

        it("can't avoid placing mapple when player has no piece in role specified on tile") {
            val game = innAndCornTwoPlayersGame(TileD, Korn6)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, 1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(ChooseCornCircleActionCommand(CornCircleAction.ADD_PIECE))
            game.dispatch(AvoidCornCircleAction).shouldContain(AddPiece(2))
        }

        it("can avoid placing mapple when player has no piece in role specified on tile") {
            val game = innAndCornTwoPlayersGame(TileD, Korn6)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Up)))
            game.dispatch(PutTile(Position(0, 1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(ChooseCornCircleActionCommand(CornCircleAction.REMOVE_PIECE))
            game.dispatch(AvoidCornCircleAction).shouldContain(PieceCanNotBeSkipped)
        }

        it("can't avoid placing mapple when player has no piece in role specified on tile") {
            val game = innAndCornTwoPlayersGame(TileD, Korn6)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, 1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(ChooseCornCircleActionCommand(CornCircleAction.REMOVE_PIECE))
            game.dispatch(AvoidCornCircleAction).shouldContain(RemovePiece(2))
        }

        it("must add piece where it is already deployed") {
            val game = innAndCornTwoPlayersGame(TileD, Korn6)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Up)))
            game.dispatch(PutTile(Position(0, 1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(ChooseCornCircleActionCommand(CornCircleAction.ADD_PIECE))
            game.dispatch(AddPieceCommand(Position(1, 0), SmallPiece, Knight(Up))).shouldContain(AddPiece(2))
        }

        it("must not add piece where it is not deployed") {
            val game = innAndCornTwoPlayersGame(TileD, Korn6)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Up)))
            game.dispatch(PutTile(Position(0, 1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(ChooseCornCircleActionCommand(CornCircleAction.ADD_PIECE))
            game.dispatch(AddPieceCommand(Position(0, 1), SmallPiece, Knight(Down))).shouldContain(InvalidPieceLocation)
        }

        it("must not add piece when player does not have such mapple available") {
            val game = innAndCornTwoPlayersGame(TileD, Korn6)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(BigPiece, Knight(Up)))
            game.dispatch(PutTile(Position(0, 1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(ChooseCornCircleActionCommand(CornCircleAction.ADD_PIECE))
            // player has only 1 big mapple
            game.dispatch(AddPieceCommand(Position(1, 0), BigPiece, Knight(Up))).shouldContain(NoMappleAvailable(BigPiece))
        }

        it("must remove piece from place where it is already deployed") {
            val game = innAndCornTwoPlayersGame(TileD, Korn6)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Up)))
            game.dispatch(PutTile(Position(0, 1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(ChooseCornCircleActionCommand(CornCircleAction.REMOVE_PIECE))
            game.dispatch(RemovePieceCommand(Position(1, 0), SmallPiece, Knight(Up))).shouldContain(RemovePiece(2))
        }

        it("must not remove piece from place where it is not deployed") {
            val game = innAndCornTwoPlayersGame(TileD, Korn6)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Up)))
            game.dispatch(PutTile(Position(0, 1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(ChooseCornCircleActionCommand(CornCircleAction.REMOVE_PIECE))
            game.dispatch(RemovePieceCommand(Position(0, 1), SmallPiece, Knight(Down))).shouldContain(InvalidPieceLocation)
        }

        it("must not remove piece that is not in his pool e.g. it is already on board") {
            val game = innAndCornTwoPlayersGame(TileD, Korn6)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Up)))
            game.dispatch(PutTile(Position(0, 1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Knight(Down)))
            game.dispatch(ChooseCornCircleActionCommand(CornCircleAction.REMOVE_PIECE))
            game.dispatch(RemovePieceCommand(Position(1, 0), BigPiece, Knight(Up))).shouldContain(InvalidPieceLocation)
        }

    }

})
