package io.github.hejcz

import io.github.hejcz.engine.Board
import io.github.hejcz.engine.Game
import io.github.hejcz.engine.RemainingTilesFromSeq
import io.github.hejcz.mapples.Mapple
import io.github.hejcz.mapples.Monk
import io.github.hejcz.placement.*
import io.github.hejcz.rules.basic.IncompleteCloisterRule
import io.github.hejcz.tiles.basic.*
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotContain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object CloisterIncompletedSpec : Spek({
    Feature("Cloister completed rule") {

        Scenario("7 tiles adjacent to cloister ") {
            val game = Game(
                    emptySet(),
                    setOf(IncompleteCloisterRule),
                    Players.singlePlayer(),
                    Board(),
                RemainingTilesFromSeq(
                    TileD,
                    TileD,
                    TileB,
                    TileB,
                    TileB,
                    TileB,
                    TileB
                )
            )

            Then("") {
                game.dispatch(PutTile(Position(1, 0), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(-1, 0), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(1, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(0, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
                game.dispatch(PutPiece(Mapple, Monk))
                game.dispatch(PutTile(Position(-1, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(-1, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(0, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
                game.dispatch(SkipPiece) shouldContain PlayerScored(1, 8, emptySet())
            }
        }

        Scenario("6 tiles adjacent to cloister ") {
            val game = Game(
                    emptySet(),
                    setOf(IncompleteCloisterRule),
                    Players.singlePlayer(),
                    Board(),
                RemainingTilesFromSeq(
                    TileD,
                    TileD,
                    TileB,
                    TileB,
                    TileB,
                    TileB
                )
            )

            Then("") {
                game.dispatch(PutTile(Position(1, 0), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(-1, 0), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(1, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(0, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
                game.dispatch(PutPiece(Mapple, Monk))
                game.dispatch(PutTile(Position(-1, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(-1, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
                game.dispatch(SkipPiece) shouldContain PlayerScored(1, 7, emptySet())
            }
        }

        Scenario("Single tile adjacent to cloister") {
            val game = Game(
                    emptySet(),
                    setOf(IncompleteCloisterRule),
                    Players.singlePlayer(),
                    Board(),
                RemainingTilesFromSeq(TileB)
            )

            Then("") {
                game.dispatch(PutTile(Position(0, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
                game.dispatch(PutPiece(Mapple, Monk)) shouldContain PlayerScored(1, 2, emptySet())
            }
        }

        Scenario("Completed cloister as a last tile ") {
            val game = Game(
                    emptySet(),
                    setOf(IncompleteCloisterRule),
                    Players.singlePlayer(),
                    Board(),
                RemainingTilesFromSeq(
                    TileD,
                    TileD,
                    TileB,
                    TileB,
                    TileB,
                    TileB,
                    TileB
                )
            )

            Then("") {
                game.dispatch(PutTile(Position(1, 0), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(-1, 0), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(1, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(-1, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(-1, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(0, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(0, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
                game.dispatch(PutPiece(Mapple, Monk)) shouldContain PlayerScored(1, 8, emptySet())
            }
        }

        Scenario("Two different players cloisters surrounded in single move") {
            val game = Game(
                    emptySet(),
                    setOf(IncompleteCloisterRule),
                    Players.twoPlayers(),
                    Board(),
                RemainingTilesFromSeq(
                    TileD,
                    TileW,
                    TileV,
                    TileE,
                    TileE,
                    TileB,
                    TileB,
                    TileH,
                    TileK
                )
            )

            Then("both players should be rewarded - 9 points each one") {
                game.dispatch(PutTile(Position(1, 0), NoRotation)).shouldContainSelectPieceOnly()
                game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(-1, 0), Rotation180)).shouldContainSelectPieceOnly()
                game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(2, 0), Rotation90)).shouldContainSelectPieceOnly()
                game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(-1, -1), Rotation270)).shouldContainSelectPieceOnly()
                game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(2, -1), Rotation90)).shouldContainSelectPieceOnly()
                game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(0, -1), NoRotation)).shouldContainSelectPieceOnly()
                game.dispatch(PutPiece(Mapple, Monk)).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(1, -1), NoRotation)).shouldContainSelectPieceOnly()
                game.dispatch(PutPiece(Mapple, Monk)).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(-1, -2), NoRotation)).shouldContainSelectPieceOnly()
                game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(0, -2), Rotation270)).shouldContainSelectPieceOnly()
                val events = game.dispatch(SkipPiece)
                events shouldContain PlayerScored(1, 7, emptySet())
                events shouldContain PlayerScored(2, 8, emptySet())
            }
        }

        Scenario("Two same player cloisters surrounded in single move") {
            val game = Game(
                    emptySet(),
                    setOf(IncompleteCloisterRule),
                    Players.twoPlayers(),
                    Board(),
                RemainingTilesFromSeq(
                    TileD,
                    TileW,
                    TileV,
                    TileE,
                    TileB,
                    TileE,
                    TileB,
                    TileH,
                    TileH,
                    TileK
                )
            )

            Then("player should be rewarded twice - 9 points for each cloister") {
                game.dispatch(PutTile(Position(1, 0), NoRotation)).shouldContainSelectPieceOnly()
                game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(-1, 0), Rotation180)).shouldContainSelectPieceOnly()
                game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(2, 0), Rotation90)).shouldContainSelectPieceOnly()
                game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(-1, -1), Rotation270)).shouldContainSelectPieceOnly()
                game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(0, -1), NoRotation)).shouldContainSelectPieceOnly()
                game.dispatch(PutPiece(Mapple, Monk)).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(2, -1), Rotation90)).shouldContainSelectPieceOnly()
                game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(1, -1), NoRotation)).shouldContainSelectPieceOnly()
                game.dispatch(PutPiece(Mapple, Monk)).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(-1, -2), NoRotation)).shouldContainSelectPieceOnly()
                game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(2, -2), NoRotation)).shouldContainSelectPieceOnly()
                game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(0, -2), Rotation270)).shouldContainSelectPieceOnly()
                val events = game.dispatch(SkipPiece)
                events shouldEqual listOf(PlayerScored(1, 8, emptySet()), PlayerScored(1, 8, emptySet()))
            }
        }

    }
})