package io.github.hejcz.basic

import io.github.hejcz.PlayerScored
import io.github.hejcz.PutPiece
import io.github.hejcz.PutTile
import io.github.hejcz.engine.Game
import io.github.hejcz.helper.Players
import io.github.hejcz.helper.TestBasicRemainingTiles
import io.github.hejcz.helper.TestGameSetup
import io.github.hejcz.mapples.Knight
import io.github.hejcz.mapples.Mapple
import io.github.hejcz.placement.*
import io.github.hejcz.rules.basic.CloisterCompletedRule
import io.github.hejcz.rules.basic.IncompleteCastleRule
import io.github.hejcz.tiles.basic.Tile
import io.github.hejcz.tiles.basic.TileD
import io.github.hejcz.tiles.basic.TileF
import io.github.hejcz.tiles.basic.TileG
import org.amshove.kluent.shouldContain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import org.spekframework.spek2.style.specification.describe

object IncompleteCastleEndGameRewardSpec : Spek({

    describe("Incomplete castle rule") {

        fun singlePlayer(vararg tiles: Tile) = Game(
            emptySet(),
            setOf(IncompleteCastleRule),
            Players.singlePlayer(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        )

        it("should give reward for incomplete castle") {
            val game = singlePlayer(TileG)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(Mapple, Knight(Down))) shouldContain PlayerScored(1, 2, emptySet())
        }

        it("should give reward for incomplete castle with emblem") {
            val game = singlePlayer(TileF)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(Mapple, Knight(Down))) shouldContain PlayerScored(1, 3, emptySet())
        }

    }

})
