package io.github.hejcz

import io.github.hejcz.engine.Board
import io.github.hejcz.engine.Game
import io.github.hejcz.engine.Player
import io.github.hejcz.engine.RemainingTilesFromSeq
import io.github.hejcz.mapples.Brigand
import io.github.hejcz.mapples.Knight
import io.github.hejcz.mapples.Mapple
import io.github.hejcz.mapples.Peasant
import io.github.hejcz.placement.*
import io.github.hejcz.rules.basic.CastleCompletedRule
import io.github.hejcz.tiles.basic.*
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object MappleAvailabilityValidatorSpec : Spek({
    Feature("Putting pieces in taken object") {

        fun playerWithSingleMapple() = Player(id = 1, order = 1, mapples = listOf(Mapple))

        fun playerWithTwoMapples() = Player(id = 1, order = 1, mapples = listOf(Mapple, Mapple))

        Scenario("no mapples available") {
            val game = Game(emptySet(), emptySet(), setOf(playerWithSingleMapple()), Board(),
                RemainingTilesFromSeq(
                    TileD,
                    TileD
                )
            )

            Then("putting mapple is detected as invalid command") {
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Knight(Up)))
                game.dispatch(PutTile(Position(2, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Brigand(Left))) shouldContain NoMappleAvailable(Mapple)
            }
        }

        Scenario("restored mapples can be used again") {
            val game = Game(setOf(CastleCompletedRule), emptySet(), setOf(playerWithSingleMapple()), Board(),
                RemainingTilesFromSeq(
                    TileD,
                    TileD,
                    TileD
                )
            )

            Then("putting mapple is detected as invalid command") {
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Knight(Up)))
                game.dispatch(PutTile(Position(1, 1), Rotation180))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(2, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Brigand(Left))) shouldEqual emptyList()
            }
        }

        Scenario("multiple mapples restored") {
            val game = Game(setOf(CastleCompletedRule), emptySet(), setOf(playerWithTwoMapples()), Board(),
                RemainingTilesFromSeq(
                    TileG,
                    TileV,
                    TileE,
                    TileM,
                    TileU,
                    TileV,
                    TileD
                )
            )

            Then("putting mapple is detected as invalid command") {
                game.dispatch(PutTile(Position(0, 1), Rotation90))
                game.dispatch(PutPiece(Mapple, Knight(Up)))
                game.dispatch(PutTile(Position(1, 1), Rotation270))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(1, 2), Rotation270))
                game.dispatch(PutPiece(Mapple, Knight(Left)))
                game.dispatch(PutTile(Position(0, 2), Rotation90))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(2, 1), Rotation90))
                game.dispatch(PutPiece(Mapple, Brigand(Left))).shouldContainPlaceTileOnly()
                game.dispatch(PutTile(Position(0, 1), Rotation90))
                game.dispatch(PutPiece(Mapple, Peasant(Location(Right, RightSide)))).shouldContainPlaceTileOnly()
            }
        }

    }
})