package io.github.hejcz

import io.github.hejcz.engine.Board
import io.github.hejcz.engine.Game
import io.github.hejcz.engine.RemainingTilesFromSeq
import io.github.hejcz.mapples.*
import io.github.hejcz.placement.*
import io.github.hejcz.tiles.basic.TileD
import org.amshove.kluent.shouldContain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object PutPieceValidatorSpec : Spek({
    Feature("Putting pieces in invalid places") {

        Scenario("monk on tile without cloister") {
            val game = Game(emptySet(), emptySet(), Players.singlePlayer(), Board(),
                RemainingTilesFromSeq(TileD)
            )

            Then("move is detected as invalid") {
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Monk)) shouldContain PiecePlacedInInvalidPlace
            }
        }

        Scenario("knight on road") {
            val game = Game(emptySet(), emptySet(), Players.singlePlayer(), Board(),
                RemainingTilesFromSeq(TileD)
            )

            Then("move is detected as invalid") {
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Knight(Right))) shouldContain PiecePlacedInInvalidPlace
            }
        }

        Scenario("knight on road 2") {
            val game = Game(emptySet(), emptySet(), Players.singlePlayer(), Board(),
                RemainingTilesFromSeq(TileD)
            )

            Then("move is detected as invalid") {
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Knight(Left))) shouldContain PiecePlacedInInvalidPlace
            }
        }

        Scenario("knight on green field") {
            val game = Game(emptySet(), emptySet(), Players.singlePlayer(), Board(),
                RemainingTilesFromSeq(TileD)
            )

            Then("move is detected as invalid") {
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Knight(Down))) shouldContain PiecePlacedInInvalidPlace
            }
        }

        Scenario("knight on green field") {
            val game = Game(emptySet(), emptySet(), Players.singlePlayer(), Board(),
                RemainingTilesFromSeq(TileD)
            )

            Then("move is detected as invalid") {
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Knight(Down))) shouldContain PiecePlacedInInvalidPlace
            }
        }

        Scenario("brigand in castle") {
            val game = Game(emptySet(), emptySet(), Players.singlePlayer(), Board(),
                RemainingTilesFromSeq(TileD)
            )

            Then("move is detected as invalid") {
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Brigand(Up))) shouldContain PiecePlacedInInvalidPlace
            }
        }

        Scenario("brigand on green field") {
            val game = Game(emptySet(), emptySet(), Players.singlePlayer(), Board(),
                RemainingTilesFromSeq(TileD)
            )

            Then("move is detected as invalid") {
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Brigand(Down))) shouldContain PiecePlacedInInvalidPlace
            }
        }

        Scenario("peasant in castle") {
            val game = Game(emptySet(), emptySet(), Players.singlePlayer(), Board(),
                RemainingTilesFromSeq(TileD)
            )

            Then("move is detected as invalid") {
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Peasant(Location(Up)))) shouldContain PiecePlacedInInvalidPlace
            }
        }

        Scenario("peasant on road") {
            val game = Game(emptySet(), emptySet(), Players.singlePlayer(), Board(),
                RemainingTilesFromSeq(TileD)
            )

            Then("move is detected as invalid") {
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Peasant(Location(Right)))) shouldContain PiecePlacedInInvalidPlace
            }
        }

        Scenario("peasant on road 2") {
            val game = Game(emptySet(), emptySet(), Players.singlePlayer(), Board(),
                RemainingTilesFromSeq(TileD)
            )

            Then("move is detected as invalid") {
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Peasant(Location(Left)))) shouldContain PiecePlacedInInvalidPlace
            }
        }

    }
})