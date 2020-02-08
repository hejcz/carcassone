package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.*
import org.amshove.kluent.*
import org.spekframework.spek2.*
import org.spekframework.spek2.style.specification.*

object RoadCompletedSpec : Spek({

    describe("Road completed rule") {

        fun singlePlayer(vararg tiles: Tile) =
            Game(Players.singlePlayer(), TestGameSetup(TestBasicRemainingTiles(*tiles))).apply { dispatch(Begin) }

        it("Simple road example") {
            val game = singlePlayer(TileS, TileS)
            game.dispatch(PutTile(Position(1, 0), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Brigand(Left)))
            game.dispatch(PutTile(Position(-1, 0), Rotation270)) containsEvent PlayerScored(1, 3, setOf(PieceOnBoard(Position(1, 0), SmallPiece, Brigand(Left))))
        }

        it("Simple road example with piece placed after tile") {
            val game = singlePlayer(TileS, TileS)
            game.dispatch(PutTile(Position(1, 0), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, 0), Rotation270)).shouldContainSelectPiece()
            game.dispatch(PutPiece(SmallPiece, Brigand(Right))) containsEvent PlayerScored(1, 3, setOf(PieceOnBoard(Position(-1, 0), SmallPiece, Brigand(Right))))
        }

        it("Simple road 3") {
            val game = singlePlayer(TileS, TileV, TileS)
            game.dispatch(PutTile(Position(-1, 0), Rotation270))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Brigand(Left)))
            game.dispatch(PutTile(Position(1, -1), Rotation180)) containsEvent PlayerScored(1, 4, setOf(PieceOnBoard(Position(1, 0), SmallPiece, Brigand(Left))))
        }

        it("should detect single event on road loop") {
            val game = singlePlayer(TileL, TileV, TileV, TileV)
            game.dispatch(PutTile(Position(1, 0), NoRotation))
            game.dispatch(PutPiece(SmallPiece, Brigand(Down)))
            game.dispatch(PutTile(Position(2, 0), NoRotation))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(2, -1), Rotation90))
            game.dispatch(SkipPiece)
            val events = game.dispatch(PutTile(Position(1, -1), Rotation180))
            events.size shouldEqual 2
            events containsEvent PlayerScored(1, 4, setOf(PieceOnBoard(Position(1, 0), SmallPiece, Brigand(Down))))
            events shouldContain SelectPiece
        }

        it("should detect single event when last tile has roads in many directions") {
            val game = singlePlayer(TileR, TileT, TileT, TileV)
            game.dispatch(PutTile(Position(0, -1), Rotation180))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, 0), Rotation270))
            game.dispatch(PutPiece(SmallPiece, Brigand(Right)))
            game.dispatch(PutTile(Position(1, -1), Rotation180))
            game.dispatch(SkipPiece)
            val events = game.dispatch(PutTile(Position(1, 0), NoRotation))
            events.size shouldEqual 2
            events containsEvent PlayerScored(1, 4, setOf(PieceOnBoard(Position(-1, 0), SmallPiece, Brigand(Right))))
            events shouldContain SelectPiece
        }

        it("should detect multiple roads completed with different pieces count") {
            val game = singlePlayer(TileL, TileV, TileU, TileU, TileV, TileV, TileW)
            game.dispatch(PutTile(Position(1, 0), Rotation90))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, -1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Brigand(Left)))
            game.dispatch(PutTile(Position(-1, 0), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Brigand(Left)))
            game.dispatch(PutTile(Position(-1, -1), Rotation90))
            game.dispatch(PutPiece(SmallPiece, Brigand(Left)))
            game.dispatch(PutTile(Position(-2, 0), Rotation270))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-2, -1), Rotation180))
            game.dispatch(SkipPiece)
            val events = game.dispatch(PutTile(Position(0, -1), NoRotation))
            events.size shouldEqual 3
            events containsEvent PlayerScored(1, 3, setOf(PieceOnBoard(Position(1, -1), SmallPiece, Brigand(Left))))
            events containsEvent PlayerScored(1, 7, setOf(PieceOnBoard(Position(-1, -1), SmallPiece, Brigand(Left)), PieceOnBoard(Position(-1, 0), SmallPiece, Brigand(Left))))
            events shouldContain SelectPiece
        }
    }
})
