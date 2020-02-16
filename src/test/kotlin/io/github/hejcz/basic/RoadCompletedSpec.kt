package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.*
import org.spekframework.spek2.*
import org.spekframework.spek2.style.specification.*

object RoadCompletedSpec : Spek({

    describe("Road completed rule") {

        fun singlePlayer(vararg tiles: Tile) =
            Game(Players.singlePlayer(), TestGameSetup(TestBasicRemainingTiles(*tiles))).dispatch(BeginCmd)

        it("Simple road example") {
            GameScenario(singlePlayer(TileS, TileS))
                .then(TileCmd(Position(1, 0), Rotation90))
                .then(PieceCmd(SmallPiece, Brigand(Left)))
                .then(TileCmd(Position(-1, 0), Rotation270))
                .thenReceivedEventShouldBe(
                    PlayerScored(1, 3, setOf(PieceOnBoard(Position(1, 0), SmallPiece, Brigand(Left))))
                )
        }

        it("Simple road example with piece placed after tile") {
            GameScenario(singlePlayer(TileS, TileS))
                .then(TileCmd(Position(1, 0), Rotation90))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 0), Rotation270)).thenReceivedEventShouldBe(SelectPiece)
                .then(PieceCmd(SmallPiece, Brigand(Right)))
                .thenReceivedEventShouldBe(
                    PlayerScored(1, 3, setOf(PieceOnBoard(Position(-1, 0), SmallPiece, Brigand(Right))))
                )
        }

        it("Simple road 3") {
            GameScenario(singlePlayer(TileS, TileV, TileS))
                .then(TileCmd(Position(-1, 0), Rotation270))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Brigand(Left)))
                .then(TileCmd(Position(1, -1), Rotation180))
                .thenReceivedEventShouldBe(
                    PlayerScored(1, 4, setOf(PieceOnBoard(Position(1, 0), SmallPiece, Brigand(Left))))
                )
        }

        it("should detect single event on road loop") {
            GameScenario(singlePlayer(TileL, TileV, TileV, TileV))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Brigand(Down)))
                .then(TileCmd(Position(2, 0), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(2, -1), Rotation90))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, -1), Rotation180))
                .thenReceivedEventShouldBe(
                    PlayerScored(1, 4, setOf(PieceOnBoard(Position(1, 0), SmallPiece, Brigand(Down))))
                )
                .thenReceivedEventShouldBe(SelectPiece)
        }

        it("should detect single event when last tile has roads in many directions") {
            GameScenario(singlePlayer(TileR, TileT, TileT, TileV))
                .then(TileCmd(Position(0, -1), Rotation180))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 0), Rotation270))
                .then(PieceCmd(SmallPiece, Brigand(Right)))
                .then(TileCmd(Position(1, -1), Rotation180))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 0), NoRotation))
                .thenReceivedEventShouldBe(
                    PlayerScored(1, 4, setOf(PieceOnBoard(Position(-1, 0), SmallPiece, Brigand(Right))))
                )
                .thenReceivedEventShouldBe(SelectPiece)
        }

        it("should detect multiple roads completed with different pieces count") {
            GameScenario(singlePlayer(TileL, TileV, TileU, TileU, TileV, TileV, TileW))
                .then(TileCmd(Position(1, 0), Rotation90))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, -1), Rotation90))
                .then(PieceCmd(SmallPiece, Brigand(Left)))
                .then(TileCmd(Position(-1, 0), Rotation90))
                .then(PieceCmd(SmallPiece, Brigand(Left)))
                .then(TileCmd(Position(-1, -1), Rotation90))
                .then(PieceCmd(SmallPiece, Brigand(Left)))
                .then(TileCmd(Position(-2, 0), Rotation270))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-2, -1), Rotation180))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, -1), NoRotation))
                .thenReceivedEventShouldBe(
                    PlayerScored(1, 3, setOf(PieceOnBoard(Position(1, -1), SmallPiece, Brigand(Left))))
                )
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        1, 7, setOf(
                            PieceOnBoard(Position(-1, -1), SmallPiece, Brigand(Left)),
                            PieceOnBoard(Position(-1, 0), SmallPiece, Brigand(Left))
                        )
                    )
                )
                .thenReceivedEventShouldBe(SelectPiece)
        }
    }
})
