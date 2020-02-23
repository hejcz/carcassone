package io.github.hejcz.base

import io.github.hejcz.api.*
import io.github.hejcz.base.tile.*
import io.github.hejcz.engine.Game
import io.github.hejcz.helper.GameScenario
import io.github.hejcz.helper.Players
import io.github.hejcz.helper.TestBasicRemainingTiles
import io.github.hejcz.helper.TestGameSetup
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object RoadCompletedSpec : Spek({

    describe("Road completed rule") {

        fun singlePlayer(vararg tiles: BasicTile) =
            Game(
                Players.singlePlayer(), TestGameSetup(TestBasicRemainingTiles(*tiles))
            ).dispatch(BeginCmd)

        it("Simple road example") {
            GameScenario(singlePlayer(TileS, TileS))
                .then(TileCmd(Position(1, 0), Rotation90))
                .then(PieceCmd(SmallPiece, Brigand(Left)))
                .then(TileCmd(Position(-1, 0), Rotation270))
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBe(
                    ScoreEvent(1, 3, setOf(
                        PieceOnBoard(
                            Position(1, 0),
                            SmallPiece, Brigand(Left)
                        )
                    ))
                )
        }

        it("Simple road example with piece placed after tile") {
            GameScenario(singlePlayer(TileS, TileS))
                .then(TileCmd(Position(1, 0), Rotation90))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 0), Rotation270)).thenReceivedEventShouldBe(PieceEvent)
                .then(PieceCmd(SmallPiece, Brigand(Right)))
                .thenReceivedEventShouldBe(
                    ScoreEvent(1, 3, setOf(
                        PieceOnBoard(
                            Position(-1, 0),
                            SmallPiece, Brigand(Right)
                        )
                    ))
                )
        }

        it("Simple road 3") {
            GameScenario(singlePlayer(TileS, TileV, TileS))
                .then(TileCmd(Position(-1, 0), Rotation270))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Brigand(Left)))
                .then(TileCmd(Position(1, -1), Rotation180))
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBe(
                    ScoreEvent(1, 4, setOf(
                        PieceOnBoard(
                            Position(1, 0),
                            SmallPiece, Brigand(Left)
                        )
                    ))
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
                .thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBe(
                    ScoreEvent(1, 4, setOf(
                        PieceOnBoard(
                            Position(1, 0),
                            SmallPiece, Brigand(Down)
                        )
                    ))
                )
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
                .thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBe(
                    ScoreEvent(1, 4, setOf(
                        PieceOnBoard(
                            Position(-1, 0),
                            SmallPiece, Brigand(Right)
                        )
                    ))
                )
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
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBe(
                    ScoreEvent(1, 3, setOf(
                        PieceOnBoard(
                            Position(1, -1),
                            SmallPiece, Brigand(Left)
                        )
                    ))
                )
                .thenReceivedEventShouldBe(
                    ScoreEvent(
                        1, 7, setOf(
                            PieceOnBoard(
                                Position(-1, -1), SmallPiece, Brigand(
                                    Left
                                )
                            ),
                            PieceOnBoard(
                                Position(-1, 0), SmallPiece, Brigand(
                                    Left
                                )
                            )
                        )
                    )
                )
        }
    }
})
