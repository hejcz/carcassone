package io.github.hejcz.expansion.corncircles

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.CornCirclesGameSetup
import io.github.hejcz.helper.GameScenario
import io.github.hejcz.helper.TestBasicRemainingTiles
import io.github.hejcz.helper.TestGameSetup
import io.github.hejcz.expansion.inn.InnAndCathedralsExtension
import io.github.hejcz.expansion.inn.tiles.TileEK
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object CornCirclesSpec : Spek({

    fun threePlayerGame(vararg tiles: Tile) = Game(
        setOf(Player(id = 1, order = 1), Player(id = 2, order = 2), Player(id = 3, order = 3)),
        CornCirclesGameSetup(TestBasicRemainingTiles(*tiles))
    ).dispatch(BeginCmd)

    fun innAndCornTwoPlayersGame(vararg tiles: Tile) = Game(
        setOf(Player(id = 1, order = 1), Player(id = 2, order = 2)),
        TestGameSetup(TestBasicRemainingTiles(*tiles), listOf(CornCirclesExtension, InnAndCathedralsExtension))
    ).dispatch(BeginCmd)

    describe("examples from rule book") {

        it("first example") {
            GameScenario(threePlayerGame(TileL, TileQ, TileW, TileE, TileE, TileH, TileG, TileU, TileU, Korn6, TileU))
                // red
                .then(TileCmd(Position(-1, 0), NoRotation)).thenReceivedEventShouldBe(PieceEvent)
                .then(PieceCmd(SmallPiece, Peasant(Location(Down, LeftSide)))).thenReceivedEventShouldBeOnlyPlaceTile()
                // green
                .then(TileCmd(Position(-1, 1), Rotation270)).thenReceivedEventShouldBe(PieceEvent)
                .then(PieceCmd(SmallPiece, Knight(Up))).thenReceivedEventShouldBeOnlyPlaceTile()
                // blue
                .then(TileCmd(Position(-2, 0), Rotation270)).thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd).thenReceivedEventShouldBeOnlyPlaceTile()
                // red
                .then(TileCmd(Position(-3, 0), NoRotation)).thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd).thenReceivedEventShouldBeOnlyPlaceTile()
                // green
                .then(TileCmd(Position(-4, 0), Rotation270)).thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd).thenReceivedEventShouldBeOnlyPlaceTile()
                // blue
                .then(TileCmd(Position(-4, 1), NoRotation)).thenReceivedEventShouldBe(PieceEvent)
                .then(PieceCmd(SmallPiece, Peasant(Location(Down)))).thenReceivedEventShouldBeOnlyPlaceTile()
                // red
                .then(TileCmd(Position(-5, 1), NoRotation)).thenReceivedEventShouldBe(PieceEvent)
                .then(PieceCmd(SmallPiece, Knight(Right))).thenReceivedEventShouldBeOnlyPlaceTile()
                // green
                .then(TileCmd(Position(-1, -1), NoRotation)).thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd).thenReceivedEventShouldBeOnlyPlaceTile()
                // blue
                .then(TileCmd(Position(-2, -1), NoRotation)).thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd).thenReceivedEventShouldBeOnlyPlaceTile()
                // red - Korn1
                .then(TileCmd(Position(-3, 1), NoRotation)).thenReceivedEventShouldBe(PieceEvent)
                .then(PieceCmd(SmallPiece, Knight(Down)))
                .thenReceivedEventShouldBe(ChooseCornActionEvent(1))
                // action
                .then(ChooseCornCircleActionCmd(CornCircleAction.ADD_PIECE))
                .thenReceivedEventShouldBe(AddPieceEvent(2))
                .then(AddPieceCmd(Position(-1, 1), SmallPiece, Knight(Up)))
                .thenReceivedEventShouldBe(AddPieceEvent(3))
                .then(AvoidCornCircleActionCmd)
                .thenReceivedEventShouldBe(AddPieceEvent(1))
                .then(AddPieceCmd(Position(-3, 1), SmallPiece, Knight(Down)))
                .thenReceivedEventShouldBeOnlyPlaceTile()
        }

        it("one player adds big and other one small piece to castle") {
            GameScenario(innAndCornTwoPlayersGame(TileN, TileK, TileEK, Korn5, TileE))
                .then(TileCmd(Position(0, 1), Rotation90))
                .then(PieceCmd(SmallPiece, Knight(Down)))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(1, 1), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(2, 1), Rotation90))
                .then(SkipPieceCmd)
                .then(ChooseCornCircleActionCmd(CornCircleAction.ADD_PIECE))
                .thenReceivedEventShouldBe(AddPieceEvent(1))
                .then(AddPieceCmd(Position(0, 1), SmallPiece, Knight(Down)))
                .thenReceivedEventShouldBe(AddPieceEvent(2))
                .then(AddPieceCmd(Position(1, 0), BigPiece, Knight(Up)))
                .then(TileCmd(Position(1, 2), Rotation180))
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBe(
                    ScoreEvent(
                        2, 18, listOf(
                            PieceOnBoard(Position(1, 0), BigPiece, Knight(Up)),
                            PieceOnBoard(Position(1, 0), SmallPiece, Knight(Up))
                        )
                    )
                )
                .thenReceivedEventShouldBe(
                    NoScoreEvent(
                        1, listOf(
                            PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down)),
                            PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down))
                        )
                    )

                )
        }

        it("when corn tile is the last time game should continue") {
            GameScenario(innAndCornTwoPlayersGame(Korn6))
                .then(TileCmd(Position(0, 1), NoRotation))
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBe(ChooseCornActionEvent(1))
        }

        it("can't choose corn action until corn tile is drawn") {
            try {
                GameScenario(innAndCornTwoPlayersGame(TileD))
                    .then(TileCmd(Position(1, 0), NoRotation))
                    .then(ChooseCornCircleActionCmd(CornCircleAction.ADD_PIECE))
                error("bad command not detected")
            } catch (ex: Exception) {
                // success
            }
        }

        it("can avoid placing mapple when player has no piece in role specified on tile") {
            GameScenario(innAndCornTwoPlayersGame(TileD, Korn6))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(0, 1), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Down)))
                .then(ChooseCornCircleActionCmd(CornCircleAction.ADD_PIECE))
                .then(AvoidCornCircleActionCmd)
                .thenReceivedEventShouldBe(CantSkipPieceEvent)
        }

        it("can't avoid placing mapple when player has no piece in role specified on tile") {
            GameScenario(innAndCornTwoPlayersGame(TileD, Korn6))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, 1), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Down)))
                .then(ChooseCornCircleActionCmd(CornCircleAction.ADD_PIECE))
                .then(AvoidCornCircleActionCmd)
                .thenReceivedEventShouldBe(AddPieceEvent(2))
        }

        it("can avoid placing mapple when player has no piece in role specified on tile") {
            GameScenario(innAndCornTwoPlayersGame(TileD, Korn6))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(0, 1), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Down)))
                .then(ChooseCornCircleActionCmd(CornCircleAction.REMOVE_PIECE))
                .then(AvoidCornCircleActionCmd)
                .thenReceivedEventShouldBe(CantSkipPieceEvent)
        }

        it("can't avoid placing mapple when player has no piece in role specified on tile") {
            GameScenario(innAndCornTwoPlayersGame(TileD, Korn6))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, 1), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Down)))
                .then(ChooseCornCircleActionCmd(CornCircleAction.REMOVE_PIECE))
                .then(AvoidCornCircleActionCmd)
                .thenReceivedEventShouldBe(RemovePieceEvent(2))
        }

        it("must add piece where it is already deployed") {
            GameScenario(innAndCornTwoPlayersGame(TileD, Korn6))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(0, 1), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Down)))
                .then(ChooseCornCircleActionCmd(CornCircleAction.ADD_PIECE))
                .then(AddPieceCmd(Position(1, 0), SmallPiece, Knight(Up)))
                .thenReceivedEventShouldBe(AddPieceEvent(2))
        }

        it("must not add piece where it is not deployed") {
            GameScenario(innAndCornTwoPlayersGame(TileD, Korn6))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(0, 1), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Down)))
                .then(ChooseCornCircleActionCmd(CornCircleAction.ADD_PIECE))
                .then(AddPieceCmd(Position(0, 1), SmallPiece, Knight(Down)))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }

        it("must not add piece when player does not have such mapple available") {
            GameScenario(innAndCornTwoPlayersGame(TileD, Korn6))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(BigPiece, Knight(Up)))
                .then(TileCmd(Position(0, 1), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Down)))
                .then(ChooseCornCircleActionCmd(CornCircleAction.ADD_PIECE))
                // player has only 1 big mapple
                .then(AddPieceCmd(Position(1, 0), BigPiece, Knight(Up)))
                .thenReceivedEventShouldBe(NoMappleEvent(BigPiece))
        }

        it("must remove piece from place where it is already deployed") {
            GameScenario(innAndCornTwoPlayersGame(TileD, Korn6))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(0, 1), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Down)))
                .then(ChooseCornCircleActionCmd(CornCircleAction.REMOVE_PIECE))
                .then(RemovePieceCmd(Position(1, 0), SmallPiece, Knight(Up)))
                .thenReceivedEventShouldBe(RemovePieceEvent(2))
        }

        it("must not remove piece from place where it is not deployed") {
            GameScenario(innAndCornTwoPlayersGame(TileD, Korn6))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(0, 1), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Down)))
                .then(ChooseCornCircleActionCmd(CornCircleAction.REMOVE_PIECE))
                .then(RemovePieceCmd(Position(0, 1), SmallPiece, Knight(Down)))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }

        it("must not remove piece that is not in his pool e.g. it is already on board") {
            GameScenario(innAndCornTwoPlayersGame(TileD, Korn6))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(0, 1), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Down)))
                .then(ChooseCornCircleActionCmd(CornCircleAction.REMOVE_PIECE))
                .then(RemovePieceCmd(Position(1, 0), BigPiece, Knight(Up)))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }
    }
})
