package io.github.hejcz

import io.github.hejcz.engine.Game
import io.github.hejcz.mapples.Mapple
import io.github.hejcz.mapples.Peasant
import io.github.hejcz.placement.*
import io.github.hejcz.rules.basic.CastleCompletedRule
import io.github.hejcz.rules.basic.PlainCompletedRule
import io.github.hejcz.tiles.basic.*
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotContain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object PlainSpec : Spek({

    Feature("Scoring for green field") {

        Scenario("Simple case with scoring") {
            val game = Game(
                setOf(CastleCompletedRule),
                setOf(PlainCompletedRule),
                Players.singlePlayer(),
                TestGameSetup(TestBasicRemainingTiles(TileE))
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, 1), Rotation180))
                game.dispatch(PutPiece(Mapple, Peasant(Location(Left)))) shouldContain PlayerScored(1, 3, emptySet())
            }
        }

        Scenario("Simple case without scoring") {
            val game = Game(
                setOf(CastleCompletedRule),
                setOf(PlainCompletedRule),
                Players.singlePlayer(),
                TestGameSetup(TestBasicRemainingTiles(TileE))
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, 1), Rotation180))
                game.dispatch(SkipPiece) shouldEqual emptyList()
            }
        }

        Scenario("Scoring for two castles") {
            val game = Game(
                setOf(CastleCompletedRule),
                setOf(PlainCompletedRule),
                Players.singlePlayer(),
                TestGameSetup(TestBasicRemainingTiles(TileI, TileE))
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, 1), Rotation270))
                game.dispatch(PutPiece(Mapple, Peasant(Location(Right))))
                game.dispatch(PutTile(Position(-1, 1), Rotation90))
                game.dispatch(SkipPiece) shouldContain PlayerScored(1, 6, emptySet())
            }
        }

        Scenario("Wrong side of road") {
            val game = Game(
                setOf(CastleCompletedRule),
                setOf(PlainCompletedRule),
                Players.singlePlayer(),
                TestGameSetup(TestBasicRemainingTiles(TileJ))
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, 1), Rotation180))
                game.dispatch(PutPiece(Mapple, Peasant(Location(Up, LeftSide)))) shouldEqual emptyList()
            }
        }

        Scenario("Right side of road") {
            val game = Game(
                setOf(CastleCompletedRule),
                setOf(PlainCompletedRule),
                Players.singlePlayer(),
                TestGameSetup(TestBasicRemainingTiles(TileJ))
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, 1), Rotation180))
                game.dispatch(PutPiece(Mapple, Peasant(Location(Up, RightSide)))) shouldContain PlayerScored(
                    1,
                    3,
                    emptySet()
                )
            }
        }

        Scenario("Multiple players disconnected green fields") {
            val game = Game(
                setOf(CastleCompletedRule),
                setOf(PlainCompletedRule),
                Players.twoPlayers(),
                TestGameSetup(TestBasicRemainingTiles(TileF, TileE))
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, 1), Rotation90))
                game.dispatch(PutPiece(Mapple, Peasant(Location(Right))))
                game.dispatch(PutTile(Position(0, 2), Rotation180))
                val events = game.dispatch(PutPiece(Mapple, Peasant(Location(Right))))
                events shouldContain PlayerScored(1, 3, emptySet())
                events shouldContain PlayerScored(2, 3, emptySet())
            }
        }

        Scenario("Multiple players dominance of one") {
            val game = Game(
                setOf(CastleCompletedRule),
                setOf(PlainCompletedRule),
                Players.twoPlayers(),
                TestGameSetup(
                    TestBasicRemainingTiles(
                        TileF,
                        TileF,
                        TileE,
                        TileA,
                        TileA,
                        TileA
                    )
                )
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, 1), Rotation90))
                game.dispatch(PutPiece(Mapple, Peasant(Location(Right))))
                game.dispatch(PutTile(Position(0, 2), Rotation90))
                game.dispatch(PutPiece(Mapple, Peasant(Location(Right))))
                game.dispatch(PutTile(Position(0, 3), Rotation180))
                game.dispatch(PutPiece(Mapple, Peasant(Location(Right))))
                game.dispatch(PutTile(Position(1, 1), Rotation270))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(1, 2), Rotation270))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(1, 3), Rotation270))
                val events = game.dispatch(SkipPiece)
                events shouldNotContain PlayerScored(2, 3, emptySet())
                events shouldContain PlayerScored(1, 3, emptySet())
            }
        }


    }

})