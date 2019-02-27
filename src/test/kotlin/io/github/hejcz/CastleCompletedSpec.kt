package io.github.hejcz

import io.github.hejcz.engine.Game
import io.github.hejcz.mapples.Knight
import io.github.hejcz.mapples.Mapple
import io.github.hejcz.placement.*
import io.github.hejcz.rules.basic.CastleCompletedRule
import io.github.hejcz.tiles.basic.*
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object CastleCompletedSpec : Spek({
    Feature("Castle completed rule") {

        Scenario("Single tile added next to starting tile - no castle completed") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                TestGameSetup(TestBasicRemainingTiles(TileD))
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Knight(Up))) shouldEqual emptyList()
            }
        }

        Scenario("Single tile added above to starting tile - smallest possible castle completed") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                TestGameSetup(TestBasicRemainingTiles(TileD))
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, 1), Rotation180))
                game.dispatch(PutPiece(Mapple, Knight(Down))) shouldContain PlayerScored(1, 4, listOf(Mapple))
            }
        }

        Scenario("Two tiles added to starting tile - bigger castle completed") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                TestGameSetup(TestBasicRemainingTiles(TileG, TileD))
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, 1), Rotation90))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(0, 2), Rotation180))
                game.dispatch(PutPiece(Mapple, Knight(Down))) shouldContain PlayerScored(1, 6, listOf(Mapple))
            }
        }

        Scenario("Two tiles added to starting tile - bigger castle incomplete") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                TestGameSetup(TestBasicRemainingTiles(TileR, TileD))
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, 1), Rotation90))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(0, 2), Rotation180))
                game.dispatch(PutPiece(Mapple, Knight(Down))) shouldEqual emptyList()
            }
        }

        Scenario("Three tiles added to starting tile - bigger castle completed") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                TestGameSetup(TestBasicRemainingTiles(TileR, TileD, TileD))
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, 1), Rotation90))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(0, 2), Rotation180))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(1, 1), Rotation270))
                game.dispatch(PutPiece(Mapple, Knight(Left))) shouldContain PlayerScored(1, 8, listOf(Mapple))
            }
        }

        Scenario("Pieces added to castle are taken into account") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                TestGameSetup(TestBasicRemainingTiles(TileR, TileD, TileD))
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, 1), Rotation90))
                game.dispatch(PutPiece(Mapple, Knight(Down)))
                game.dispatch(PutTile(Position(0, 2), Rotation180))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(1, 1), Rotation270)) shouldContain PlayerScored(1, 8, listOf(Mapple))
            }
        }

        Scenario("Pieces added to castle are returned if there is more than one") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                TestGameSetup(
                    TestBasicRemainingTiles(
                        TileR,
                        TileD,
                        TileD,
                        TileN
                    )
                )
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, 1), Rotation90))
                game.dispatch(PutPiece(Mapple, Knight(Down)))
                game.dispatch(PutTile(Position(0, 2), Rotation180))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(1, 2), Rotation180))
                game.dispatch(PutPiece(Mapple, Knight(Down)))
                game.dispatch(PutTile(Position(1, 1), Rotation270)) shouldContain PlayerScored(
                    1,
                    10,
                    listOf(Mapple, Mapple)
                )
            }
        }

        Scenario("No pieces in finished castle") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                TestGameSetup(
                    TestBasicRemainingTiles(
                        TileR,
                        TileD,
                        TileD,
                        TileN
                    )
                )
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, 1), Rotation90))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(0, 2), Rotation180))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(1, 2), Rotation180))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(1, 1), Rotation270)).shouldContainSelectPieceOnly()
                game.dispatch(SkipPiece) shouldEqual emptyList()
            }
        }

        Scenario("Equal number of pieces of two players in same castle") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.twoPlayers(),
                TestGameSetup(
                    TestBasicRemainingTiles(
                        TileR,
                        TileD,
                        TileA,
                        TileD,
                        TileN
                    )
                )
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, 1), Rotation90))
                game.dispatch(PutPiece(Mapple, Knight(Down)))
                game.dispatch(PutTile(Position(0, 2), Rotation180))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(1, 0), Rotation90))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(1, 2), Rotation180))
                game.dispatch(PutPiece(Mapple, Knight(Down)))
                val events = game.dispatch(PutTile(Position(1, 1), Rotation270))
                events shouldContain PlayerScored(1, 10, listOf(Mapple))
                events shouldContain PlayerScored(2, 10, listOf(Mapple))
            }
        }

        Scenario("Advantage of one player in single castle") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.twoPlayers(),
                TestGameSetup(
                    TestBasicRemainingTiles(
                        TileR,
                        TileD,
                        TileD,
                        TileD,
                        TileR
                    )
                )
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, 1), Rotation90))
                game.dispatch(PutPiece(Mapple, Knight(Down)))
                game.dispatch(PutTile(Position(0, 2), Rotation180))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Knight(Up)))
                game.dispatch(PutTile(Position(1, 2), Rotation180))
                game.dispatch(PutPiece(Mapple, Knight(Down)))
                val events = game.dispatch(PutTile(Position(1, 1), Rotation270))
                events shouldContain PlayerScored(1, 12, listOf(Mapple, Mapple))
                events shouldContain OccupiedAreaCompleted(2, listOf(Mapple))
            }
        }

        Scenario("Two castles completed at the same time") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.twoPlayers(),
                TestGameSetup(
                    TestBasicRemainingTiles(
                        TileN,
                        TileD,
                        TileI
                    )
                )
            )

            Then("it should give corresponding number of points to both players") {
                game.dispatch(PutTile(Position(0, 1), Rotation90))
                game.dispatch(PutPiece(Mapple, Knight(Down)))
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Knight(Up)))
                val events = game.dispatch(PutTile(Position(1, 1), Rotation270))
                events shouldContain PlayerScored(1, 6, listOf(Mapple))
                events shouldContain PlayerScored(2, 4, listOf(Mapple))
            }
        }

        Scenario("Two tiles added to starting tile - bigger castle completed") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                TestGameSetup(TestBasicRemainingTiles(TileF, TileD))
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, 1), Rotation90))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(0, 2), Rotation180))
                game.dispatch(PutPiece(Mapple, Knight(Down))) shouldContain PlayerScored(1, 8, listOf(Mapple))
            }
        }

        Scenario("Honoring multiple emblems") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                TestGameSetup(TestBasicRemainingTiles(TileF, TileM, TileD))
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, 1), Rotation90))
                game.dispatch(PutPiece(Mapple, Knight(Down)))
                game.dispatch(PutTile(Position(0, 2), Rotation90))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(1, 2), Rotation270)) shouldContain PlayerScored(1, 12, listOf(Mapple))
            }
        }

    }
})