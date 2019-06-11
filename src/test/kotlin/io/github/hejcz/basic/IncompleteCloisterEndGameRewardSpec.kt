package io.github.hejcz.basic

import io.github.hejcz.basic.tile.*
import io.github.hejcz.core.*
import io.github.hejcz.helper.*
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotContain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object IncompleteCloisterEndGameRewardSpec : Spek({

    describe("Incomplete cloister detector") {

        fun singlePlayer(vararg tiles: Tile) = Game(
            Players.singlePlayer(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        )

        fun multiPlayer(vararg tiles: Tile) = Game(
            Players.twoPlayers(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        )

        it("should detect cloister with 7/8 surrounding tiles") {
            val game = singlePlayer(TileD, TileD, TileB, TileB, TileB, TileB, TileB)
            game.dispatch(PutTile(Position(1, 0), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, 0), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
            game.dispatch(PutPiece(SmallPiece, Monk))
            game.dispatch(PutTile(Position(-1, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
            game.dispatch(SkipPiece) shouldContain PlayerScored(1, 8, emptySet())
        }

        it("should detect cloister with 6/8 surrounding tiles") {
            val game = singlePlayer(TileD, TileD, TileB, TileB, TileB, TileB)
            game.dispatch(PutTile(Position(1, 0), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, 0), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
            game.dispatch(PutPiece(SmallPiece, Monk))
            game.dispatch(PutTile(Position(-1, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
            game.dispatch(SkipPiece) shouldContain PlayerScored(1, 7, emptySet())
        }

        it("should detect cloister with 1/8 surrounding tiles") {
            val game = singlePlayer(TileB)
            game.dispatch(PutTile(Position(0, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
            game.dispatch(PutPiece(SmallPiece, Monk)) shouldContain PlayerScored(1, 2, emptySet())
        }

        it("should detect cloister added as a last tile") {
            val game = singlePlayer(TileD, TileD, TileB, TileB, TileB, TileB, TileB)
            game.dispatch(PutTile(Position(1, 0), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, 0), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, emptySet())
            game.dispatch(PutPiece(SmallPiece, Monk)) shouldContain PlayerScored(1, 8, emptySet())
        }

        it("should detect two incomplete cloisters of different players with 7/8 and 6/8 tiles surrounding tiles respectively") {
            val game = multiPlayer(TileD, TileW, TileV, TileE, TileE, TileB, TileB, TileH, TileK)
            game.dispatch(PutTile(Position(1, 0), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(-1, 0), Rotation180)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(2, 0), Rotation90)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(-1, -1), Rotation270)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(2, -1), Rotation90)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(0, -1), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(SmallPiece, Monk)).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(1, -1), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(SmallPiece, Monk)).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(-1, -2), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(0, -2), Rotation270)).shouldContainSelectPieceOnly()
            val events = game.dispatch(SkipPiece)
            events shouldContain PlayerScored(1, 7, emptySet())
            events shouldContain PlayerScored(2, 8, emptySet())
        }

        it("should detect two incomplete cloisters of the same player") {
            val game = multiPlayer(TileD, TileW, TileV, TileE, TileB, TileE, TileB, TileH, TileH, TileK)
            game.dispatch(PutTile(Position(1, 0), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(-1, 0), Rotation180)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(2, 0), Rotation90)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(-1, -1), Rotation270)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(0, -1), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(SmallPiece, Monk)).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(2, -1), Rotation90)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(1, -1), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(PutPiece(SmallPiece, Monk)).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(-1, -2), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(2, -2), NoRotation)).shouldContainSelectPieceOnly()
            game.dispatch(SkipPiece).shouldContainPlaceTileOnly()
            game.dispatch(PutTile(Position(0, -2), Rotation270)).shouldContainSelectPieceOnly()
            val events = game.dispatch(SkipPiece)
            events shouldEqual listOf(PlayerScored(1, 8, emptySet()), PlayerScored(1, 8, emptySet()))
        }
    }
})
