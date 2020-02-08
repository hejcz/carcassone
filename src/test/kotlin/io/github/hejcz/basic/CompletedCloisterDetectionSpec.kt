package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.*
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object CompletedCloisterDetectionSpec : Spek({

    describe("A cloister detector") {

        fun singlePlayer(vararg tiles: Tile) = Game(
            Players.singlePlayer(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        ).apply { dispatch(Begin) }

        fun multiPlayer(vararg tiles: Tile) = Game(
            Players.twoPlayers(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        ).apply { dispatch(Begin) }

        it("should detect completed cloister") {
            val game = singlePlayer(TileD, TileD, TileB, TileB, TileB, TileB, TileB, TileB)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, 0), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, -1), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, -1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Monk))
            game.dispatch(PutTile(Position(-1, -1), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, -2), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, -2), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, -2), NoRotation)) containsEvent PlayerScored(1, 9, setOf(PieceOnBoard(Position(0, -1), SmallPiece, Monk)))
        }

        it("should detect completed cloister filling the hole") {
            val game = singlePlayer(TileD, TileD, TileB, TileB, TileB, TileB, TileB, TileB)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, 0), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, -1), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, -1), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, -2), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, -2), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, -2), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, -1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Monk)) containsEvent PlayerScored(1, 9, setOf(PieceOnBoard(Position(0, -1), SmallPiece, Monk)))
        }

        it("should detect two cloisters of diferent players completed in single move") {
            val game = multiPlayer(TileD, TileW, TileV, TileE, TileE, TileB, TileB, TileH, TileH, TileK, TileJ)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, 0), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(2, 0), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, -1), Rotation270))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(2, -1), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, -1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Monk))
            game.dispatch(PutTile(Position(1, -1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Monk))
            game.dispatch(PutTile(Position(-1, -2), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(2, -2), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, -2), Rotation270))
            game.dispatch(SkipPiece)
            val events = game.dispatch(PutTile(Position(1, -2), Rotation90))
            events containsEvent PlayerScored(1, 9, setOf(PieceOnBoard(Position(1, -1), SmallPiece, Monk)))
            events containsEvent PlayerScored(2, 9, setOf(PieceOnBoard(Position(0, -1), SmallPiece, Monk)))
        }

        it("should detect two cloisters of same player completed in single move") {
            val game = multiPlayer(TileD, TileW, TileV, TileE, TileB, TileE, TileB, TileH, TileH, TileK, TileJ)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, 0), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(2, 0), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, -1), Rotation270))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, -1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Monk))
            game.dispatch(PutTile(Position(2, -1), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, -1), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Monk))
            game.dispatch(PutTile(Position(-1, -2), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(2, -2), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, -2), Rotation270))
            game.dispatch(SkipPiece)
            val events = game.dispatch(PutTile(Position(1, -2), Rotation90))
            events shouldContain SelectPiece
            events containsEvent PlayerScored(1, 9, setOf(PieceOnBoard(Position(0, -1), SmallPiece, Monk)))
            events containsEvent PlayerScored(1, 9, setOf(PieceOnBoard(Position(1, -1), SmallPiece, Monk)))
        }
    }
})
