package io.github.hejcz.basic

import io.github.hejcz.PlayerScored
import io.github.hejcz.PutPiece
import io.github.hejcz.PutTile
import io.github.hejcz.SkipPiece
import io.github.hejcz.engine.Game
import io.github.hejcz.helper.*
import io.github.hejcz.mapples.Brigand
import io.github.hejcz.mapples.Mapple
import io.github.hejcz.placement.*
import io.github.hejcz.rules.basic.IncompleteCloisterRule
import io.github.hejcz.rules.basic.IncompleteRoadRule
import io.github.hejcz.tiles.basic.*
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldNotContain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import org.spekframework.spek2.style.specification.describe

object IncompleteRoadEndGameRewardSpec : Spek({

    describe("Incomplete road scoring") {

        fun singlePlayer(vararg tiles: Tile) = Game(
            emptySet(),
            setOf(IncompleteRoadRule),
            Players.singlePlayer(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        )

        fun multiPlayer(vararg tiles: Tile) = Game(
            emptySet(),
            setOf(IncompleteRoadRule),
            Players.twoPlayers(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        )

        it("should detect simple incomplete road") {
            val game = singlePlayer(TileK)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Brigand(Left))) shouldContain PlayerScored(1, 2, emptySet())
        }

        it("should not be triggered if there are still some tiles in the deck") {
            val game = singlePlayer(TileK, TileA)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Brigand(Left))).shouldContainPlaceTileOnly()
        }

        it("should detect simple road made out of 3 tiles") {
            val game = singlePlayer(TileK, TileL)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(Mapple, Brigand(Down))).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(1, -1), Rotation90))
            game.dispatch(PutPiece(Mapple, Brigand(Down))) shouldContain PlayerScored(1, 3, emptySet())
        }

        it("should reward multiple players if they share road with equal number of mapples") {
            val game = multiPlayer(TileK, TileU, TileJ, TileK)
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

        it("should reward single player if he has advantage of mapples over his opponent") {
            val game = multiPlayer(TileV, TileV, TileB, TileU, TileV, TileV, TileV)
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

})
