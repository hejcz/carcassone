package io.github.hejcz

import io.github.hejcz.engine.Board
import io.github.hejcz.engine.Game
import io.github.hejcz.engine.RemainingTilesFromSeq
import io.github.hejcz.mapples.Mapple
import io.github.hejcz.mapples.Monk
import io.github.hejcz.placement.*
import io.github.hejcz.rules.basic.CloisterCompletedRule
import io.github.hejcz.tiles.basic.*
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotContain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object CloisterCompletedSpec : Spek({
    Feature("Cloister completed rule") {

        Scenario("Place last tile ") {
            val game = Game(
                    setOf(CloisterCompletedRule),
                    emptySet(),
                    Players.singlePlayer(),
                    Board(),
                RemainingTilesFromSeq(
                    TileD,
                    TileD,
                    TileB,
                    TileB,
                    TileB,
                    TileB,
                    TileB,
                    TileB
                )
            )

            Then("") {
                game.dispatch(PutTile(Position(1, 0), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(-1, 0), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(1, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(0, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(PutPiece(Mapple, Monk))
                game.dispatch(PutTile(Position(-1, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(-1, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(0, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(1, -2), NoRotation)) shouldContain PlayerScored(1, 9, setOf(Mapple))
            }
        }

        Scenario("Completed cloister as a last tile ") {
            val game = Game(
                    setOf(CloisterCompletedRule),
                    emptySet(),
                    Players.singlePlayer(),
                    Board(),
                RemainingTilesFromSeq(
                    TileD,
                    TileD,
                    TileB,
                    TileB,
                    TileB,
                    TileB,
                    TileB,
                    TileB
                )
            )

            Then("") {
                game.dispatch(PutTile(Position(1, 0), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(-1, 0), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(1, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(-1, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(-1, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(0, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(1, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(0, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(PutPiece(Mapple, Monk)) shouldContain PlayerScored(1, 9, setOf(Mapple))
            }
        }

        Scenario("Two different players cloisters surrounded in single move") {
            val game = Game(
                    setOf(CloisterCompletedRule),
                    emptySet(),
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
                    TileH,
                    TileK,
                    TileJ
                )
            )

            Then("both players should be rewarded - 9 points each one") {
                game.dispatch(PutTile(Position(1, 0), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(-1, 0), Rotation180)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(2, 0), Rotation90)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(-1, -1), Rotation270)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(2, -1), Rotation90)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(0, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(PutPiece(Mapple, Monk))
                game.dispatch(PutTile(Position(1, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(PutPiece(Mapple, Monk))
                game.dispatch(PutTile(Position(-1, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(2, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(0, -2), Rotation270)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                val events = game.dispatch(PutTile(Position(1, -2), Rotation90))
                events shouldContain PlayerScored(1, 9, setOf(Mapple))
                events shouldContain PlayerScored(2, 9, setOf(Mapple))
            }
        }

        Scenario("Two same player cloisters surrounded in single move") {
            val game = Game(
                    setOf(CloisterCompletedRule),
                    emptySet(),
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
                    TileK,
                    TileJ
                )
            )

            Then("player should be rewarded twice - 9 points for each cloister") {
                game.dispatch(PutTile(Position(1, 0), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(-1, 0), Rotation180)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(2, 0), Rotation90)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(-1, -1), Rotation270)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(0, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(PutPiece(Mapple, Monk))
                game.dispatch(PutTile(Position(2, -1), Rotation90)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(1, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(PutPiece(Mapple, Monk))
                game.dispatch(PutTile(Position(-1, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(2, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(0, -2), Rotation270)) shouldNotContain PlayerScored(1, 9, setOf(Mapple))
                game.dispatch(SkipPiece)
                val events = game.dispatch(PutTile(Position(1, -2), Rotation90))
                events shouldEqual listOf(PlayerScored(1, 9, setOf(Mapple)), PlayerScored(1, 9, setOf(Mapple)), SelectPiece)
            }
        }

    }
})