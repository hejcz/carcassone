package io.github.hejcz

import io.github.hejcz.engine.Board
import io.github.hejcz.engine.Game
import io.github.hejcz.engine.RemainingTilesFromSeq
import io.github.hejcz.placement.*
import io.github.hejcz.tiles.basic.TileA
import org.amshove.kluent.shouldContain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object PutTileValidatorSpec : Spek({
    Feature("Putting tile in invalid places") {

        Scenario("roads validation") {
            val game = Game(
                    emptySet(),
                    emptySet(),
                    Players.singlePlayer(),
                    Board(),
                RemainingTilesFromSeq(TileA)
            )

            Then("move is detected as invalid") {
                game.dispatch(PutTile(Position(1, 0), NoRotation)) shouldContain TilePlacedInInvalidPlace
                game.dispatch(PutTile(Position(1, 0), Rotation180)) shouldContain TilePlacedInInvalidPlace
                game.dispatch(PutTile(Position(1, 0), Rotation270)) shouldContain TilePlacedInInvalidPlace
                game.dispatch(PutTile(Position(1, 0), Rotation90)).shouldContainSelectPieceOnly()
            }
        }

    }
})