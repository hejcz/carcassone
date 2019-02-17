package io.github.hejcz

import io.github.hejcz.engine.Board
import io.github.hejcz.engine.Game
import io.github.hejcz.engine.RemainingTilesFromSeq
import io.github.hejcz.mapples.Brigand
import io.github.hejcz.mapples.Mapple
import io.github.hejcz.placement.*
import io.github.hejcz.rules.basic.IncompleteRoadRule
import io.github.hejcz.tiles.basic.*
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldNotContain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object IncompleteRoadSpec : Spek({

    Feature("Incomplete road scoring") {

        Scenario("Simple case ") {
            val game = Game(
                    emptySet(),
                    setOf(IncompleteRoadRule),
                    Players.singlePlayer(),
                    Board(),
                RemainingTilesFromSeq(TileK)
            )

            Then("") {
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Brigand(Left))) shouldContain PlayerScored(1, 2, emptySet())
            }
        }

        Scenario("Simple case another part of road") {
            val game = Game(
                    emptySet(),
                    setOf(IncompleteRoadRule),
                    Players.singlePlayer(),
                    Board(),
                RemainingTilesFromSeq(TileK)
            )

            Then("") {
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Brigand(Down))) shouldContain PlayerScored(1, 2, emptySet())
            }
        }

        Scenario("Simple case but some tiles left in deck") {
            val game = Game(
                    emptySet(),
                    setOf(IncompleteRoadRule),
                    Players.singlePlayer(),
                    Board(),
                RemainingTilesFromSeq(TileK, TileA)
            )

            Then("") {
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Brigand(Down))).shouldContainPlaceTileOnly()
            }
        }

        Scenario("Road bounded from one side") {
            val game = Game(
                    emptySet(),
                    setOf(IncompleteRoadRule),
                    Players.singlePlayer(),
                    Board(),
                RemainingTilesFromSeq(TileK, TileL)
            )

            Then("") {
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Brigand(Down))).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(1, -1), Rotation90))
                game.dispatch(PutPiece(Mapple, Brigand(Down))) shouldContain PlayerScored(1, 3, emptySet())
            }
        }

        Scenario("Shared road") {
            val game = Game(
                    emptySet(),
                    setOf(IncompleteRoadRule),
                    Players.twoPlayers(),
                    Board(),
                RemainingTilesFromSeq(
                    TileK,
                    TileU,
                    TileJ,
                    TileK
                )
            )

            Then("") {
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Brigand(Down))).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(2, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Brigand(Down))).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(1, -1), Rotation270))
                game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(2, -1), Rotation90))
                val events = game.dispatch(SkipPiece)
                events shouldContain PlayerScored(1, 5, emptySet())
                events shouldContain PlayerScored(2, 5, emptySet())
            }
        }

        Scenario("Shared road with advantage of one player") {
            val game = Game(
                    emptySet(),
                    setOf(IncompleteRoadRule),
                    Players.twoPlayers(),
                    Board(),
                RemainingTilesFromSeq(
                    TileV,
                    TileV,
                    TileB,
                    TileU,
                    TileV,
                    TileV,
                    TileV
                )
            )

            Then("") {
                game.dispatch(PutTile(Position(1, 0), NoRotation)).shouldContainSelectPieceOnly()
                game.dispatch(PutPiece(Mapple, Brigand(Down))).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(2, 0), Rotation270)).shouldContainSelectPieceOnly()
                game.dispatch(PutPiece(Mapple, Brigand(Down))).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(2, 1), NoRotation)).shouldContainSelectPieceOnly()
                game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(3, 1), NoRotation)).shouldContainSelectPieceOnly()
                game.dispatch(PutPiece(Mapple, Brigand(Down))).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(3, 0), Rotation90)).shouldContainSelectPieceOnly()
                game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(1, -1), Rotation180)).shouldContainSelectPieceOnly()
                game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(2, -1), Rotation90)).shouldContainSelectPieceOnly()
                val events = game.dispatch(SkipPiece)
                events shouldContain PlayerScored(2, 7, emptySet())
                events shouldNotContain PlayerScored(1, 7, emptySet())
            }
        }


    }
})