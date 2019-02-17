package io.github.hejcz

import io.github.hejcz.engine.Board
import io.github.hejcz.engine.Game
import io.github.hejcz.engine.RemainingTilesFromSeq
import io.github.hejcz.mapples.Knight
import io.github.hejcz.mapples.Mapple
import io.github.hejcz.placement.*
import io.github.hejcz.rules.basic.IncompleteCastleRule
import io.github.hejcz.tiles.basic.TileD
import io.github.hejcz.tiles.basic.TileF
import io.github.hejcz.tiles.basic.TileG
import org.amshove.kluent.shouldContain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object IncompleteCastleSpec : Spek({

    Feature("Incomplete castle scoring") {

        Scenario("Simple case ") {
            val game = Game(
                    emptySet(),
                    setOf(IncompleteCastleRule),
                    Players.singlePlayer(),
                    Board(),
                RemainingTilesFromSeq(TileG)
            )

            Then("") {
                game.dispatch(PutTile(Position(0, 1), Rotation90))
                game.dispatch(PutPiece(Mapple, Knight(Down))) shouldContain PlayerScored(1, 2, emptySet())
            }
        }

        Scenario("Simple case with emblem") {
            val game = Game(
                    emptySet(),
                    setOf(IncompleteCastleRule),
                    Players.singlePlayer(),
                    Board(),
                RemainingTilesFromSeq(TileF)
            )

            Then("") {
                game.dispatch(PutTile(Position(0, 1), Rotation90))
                game.dispatch(PutPiece(Mapple, Knight(Down))) shouldContain PlayerScored(1, 3, emptySet())
            }
        }

        Scenario("Simple case") {
            val game = Game(
                    emptySet(),
                    setOf(IncompleteCastleRule),
                    Players.singlePlayer(),
                    Board(),
                RemainingTilesFromSeq(TileD)
            )

            Then("") {
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Knight(Up))) shouldContain PlayerScored(1, 1, emptySet())
            }
        }

    }
})