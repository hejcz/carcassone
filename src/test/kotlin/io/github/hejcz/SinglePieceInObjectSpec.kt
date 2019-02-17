package io.github.hejcz

import io.github.hejcz.engine.Board
import io.github.hejcz.engine.Game
import io.github.hejcz.engine.RemainingTilesFromSeq
import io.github.hejcz.mapples.Brigand
import io.github.hejcz.mapples.Knight
import io.github.hejcz.mapples.Mapple
import io.github.hejcz.mapples.Peasant
import io.github.hejcz.placement.*
import io.github.hejcz.tiles.basic.TileD
import io.github.hejcz.tiles.basic.TileF
import io.github.hejcz.tiles.basic.TileV
import org.amshove.kluent.shouldContain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object SinglePieceInObjectSpec : Spek({
    Feature("Putting pieces in taken object") {

        Scenario("knights") {
            val game = Game(emptySet(), emptySet(), Players.singlePlayer(), Board(),
                RemainingTilesFromSeq(
                    TileF,
                    TileD
                )
            )

            Then("move is detected as invalid") {
                game.dispatch(PutTile(Position(0, 1), Rotation90))
                game.dispatch(PutPiece(Mapple, Knight(Down)))
                game.dispatch(PutTile(Position(0, 2), Rotation180))
                game.dispatch(PutPiece(Mapple, Knight(Down))) shouldContain PiecePlacedInInvalidPlace
            }
        }

        Scenario("knights two players") {
            val game = Game(emptySet(), emptySet(), Players.twoPlayers(), Board(),
                RemainingTilesFromSeq(
                    TileF,
                    TileD
                )
            )

            Then("move is detected as invalid") {
                game.dispatch(PutTile(Position(0, 1), Rotation90))
                game.dispatch(PutPiece(Mapple, Knight(Down)))
                game.dispatch(PutTile(Position(0, 2), Rotation180))
                game.dispatch(PutPiece(Mapple, Knight(Down))) shouldContain PiecePlacedInInvalidPlace
            }
        }

        Scenario("brigands") {
            val game = Game(emptySet(), emptySet(), Players.singlePlayer(), Board(),
                RemainingTilesFromSeq(
                    TileD,
                    TileV
                )
            )

            Then("move is detected as invalid") {
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Brigand(Right)))
                game.dispatch(PutTile(Position(2, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Brigand(Down))) shouldContain PiecePlacedInInvalidPlace
            }
        }

        Scenario("brigands two players") {
            val game = Game(emptySet(), emptySet(), Players.twoPlayers(), Board(),
                RemainingTilesFromSeq(
                    TileD,
                    TileV
                )
            )

            Then("move is detected as invalid") {
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Brigand(Right)))
                game.dispatch(PutTile(Position(2, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Brigand(Down))) shouldContain PiecePlacedInInvalidPlace
            }
        }

        Scenario("peasants") {
            val game = Game(emptySet(), emptySet(), Players.singlePlayer(), Board(),
                RemainingTilesFromSeq(
                    TileD,
                    TileV
                )
            )

            Then("move is detected as invalid") {
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Peasant(Location(Right, LeftSide))))
                game.dispatch(PutTile(Position(2, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Peasant(Location(Right)))) shouldContain PiecePlacedInInvalidPlace
            }
        }

        Scenario("peasants two players") {
            val game = Game(emptySet(), emptySet(), Players.twoPlayers(), Board(),
                RemainingTilesFromSeq(
                    TileD,
                    TileV
                )
            )

            Then("move is detected as invalid") {
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Peasant(Location(Right, LeftSide))))
                game.dispatch(PutTile(Position(2, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Peasant(Location(Right)))) shouldContain PiecePlacedInInvalidPlace
            }
        }

    }
})