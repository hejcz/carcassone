package io.github.hejcz

import io.github.hejcz.engine.Game
import io.github.hejcz.mapples.Brigand
import io.github.hejcz.mapples.Mapple
import io.github.hejcz.placement.*
import io.github.hejcz.rules.basic.RoadCompletedRule
import io.github.hejcz.tiles.basic.TileS
import io.github.hejcz.tiles.basic.TileV
import org.amshove.kluent.shouldContain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object RoadCompletedSpec : Spek({
    Feature("Castle completed rule") {

        Scenario("Simple road example") {
            val game = Game(
                setOf(RoadCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                TestGameSetup(TestBasicRemainingTiles(TileS, TileS))
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(1, 0), Rotation90))
                game.dispatch(PutPiece(Mapple, Brigand(Left)))
                game.dispatch(PutTile(Position(-1, 0), Rotation270)) shouldContain PlayerScored(1, 3, listOf(Mapple))
            }
        }

        Scenario("Simple road example with piece placed after tile") {
            val game = Game(
                setOf(RoadCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                TestGameSetup(TestBasicRemainingTiles(TileS, TileS))
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(1, 0), Rotation90))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(-1, 0), Rotation270)).shouldContainSelectPieceOnly()
                game.dispatch(PutPiece(Mapple, Brigand(Right))) shouldContain PlayerScored(1, 3, listOf(Mapple))
            }
        }

        Scenario("Simple road 3") {
            val game = Game(
                setOf(RoadCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                TestGameSetup(
                    TestBasicRemainingTiles(
                        TileS,
                        TileV,
                        TileS
                    )
                )
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(-1, 0), Rotation270))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(1, 0), NoRotation))
                game.dispatch(PutPiece(Mapple, Brigand(Left)))
                game.dispatch(PutTile(Position(1, -1), Rotation180)) shouldContain PlayerScored(1, 4, listOf(Mapple))
            }
        }

    }
})