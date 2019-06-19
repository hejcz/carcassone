package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.*
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotContain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object PlainEndGameRewardSpec : Spek({

    describe("Green field") {

        fun singlePlayer(vararg tiles: Tile) =
            Game(Players.singlePlayer(), TestGameSetup(TestBasicRemainingTiles(*tiles))).apply { dispatch(Begin) }

        fun multiPlayer(vararg tiles: Tile) =
            Game(Players.twoPlayers(), TestGameSetup(TestBasicRemainingTiles(*tiles))).apply { dispatch(Begin) }

        it("should be scored") {
            val game = singlePlayer(TileE)
            game.dispatch(PutTile(Position(0, 1), Rotation180))
            game.dispatch(PutPiece(SmallPiece, Peasant(Location(Left)))) containsEvent PlayerScored(1, 3, emptySet())
        }

        it("Simple case without scoring") {
            val game = singlePlayer(TileE)
            game.dispatch(PutTile(Position(0, 1), Rotation180))
            game.dispatch(SkipPiece) shouldEqual emptyList()
        }

        it("Scoring for two castles") {
            val game = singlePlayer(TileI, TileE)
            game.dispatch(PutTile(Position(0, 1), Rotation270))
            game.dispatch(PutPiece(SmallPiece, Peasant(Location(Right))))
            game.dispatch(PutTile(Position(-1, 1), Rotation90))
            game.dispatch(SkipPiece) containsEvent PlayerScored(1, 6, emptySet())
        }

        it("Wrong side of road") {
            val game = singlePlayer(TileJ)
            game.dispatch(PutTile(Position(0, 1), Rotation180))
            game.dispatch(PutPiece(SmallPiece, Peasant(Location(Up, LeftSide)))) shouldEqual emptyList()
        }

        it("Right side of road") {
            val game = singlePlayer(TileJ)
            game.dispatch(PutTile(Position(0, 1), Rotation180))
            game.dispatch(PutPiece(SmallPiece, Peasant(Location(Up, RightSide)))) containsEvent PlayerScored(1, 3, emptySet())
        }

        it("Multiple players disconnected green fields") {
            val game = multiPlayer(TileF, TileE)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Peasant(Location(Right))))
            game.dispatch(PutTile(Position(0, 2), Rotation180))
            val events = game.dispatch(PutPiece(SmallPiece, Peasant(Location(Right))))
            events containsEvent PlayerScored(1, 3, emptySet())
            events containsEvent PlayerScored(2, 3, emptySet())
        }

        it("Multiple players dominance of one") {
            val game = multiPlayer(TileF, TileF, TileE, TileA, TileA, TileA)
            game.dispatch(PutTile(Position(0, 1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Peasant(Location(Right))))
            game.dispatch(PutTile(Position(0, 2), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Peasant(Location(Right))))
            game.dispatch(PutTile(Position(0, 3), Rotation180))
            game.dispatch(PutPiece(SmallPiece, Peasant(Location(Right))))
            game.dispatch(PutTile(Position(1, 1), Rotation270))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 2), Rotation270))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 3), Rotation270))
            val events = game.dispatch(SkipPiece)
            events doesNotContainEvent PlayerScored(2, 3, emptySet())
            events containsEvent PlayerScored(1, 3, emptySet())
        }
    }
})
