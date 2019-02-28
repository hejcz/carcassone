package io.github.hejcz.basic

import io.github.hejcz.basic.tiles.*
import io.github.hejcz.core.*
import io.github.hejcz.helper.Players
import io.github.hejcz.helper.TestBasicRemainingTiles
import io.github.hejcz.helper.TestGameSetup
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotContain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object CompletedCloisterDetectionSpec : Spek({

    describe("A cloister detector") {

        fun singlePlayer(vararg tiles: Tile) = Game(
            Players.singlePlayer(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        )

        fun multiPlayer(vararg tiles: Tile) = Game(
            Players.twoPlayers(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        )

        it("should detect completed cloister") {
            val game = singlePlayer(TileD, TileD, TileB, TileB, TileB, TileB, TileB, TileB)
            game.dispatch(PutTile(Position(1, 0), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, 0), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(PutPiece(SmallPiece, Monk))
            game.dispatch(PutTile(Position(-1, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, -2), NoRotation)) shouldContain PlayerScored(1, 9, setOf(SmallPiece))
        }

        it("should detect completed cloister filling the hole") {
            val game = singlePlayer(TileD, TileD, TileB, TileB, TileB, TileB, TileB, TileB)
            game.dispatch(PutTile(Position(1, 0), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, 0), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(PutPiece(SmallPiece, Monk)) shouldContain PlayerScored(1, 9, setOf(SmallPiece))
        }

        it("should detect two cloisters of diferent players completed in single move") {
            val game = multiPlayer(TileD, TileW, TileV, TileE, TileE, TileB, TileB, TileH, TileH, TileK, TileJ)
            game.dispatch(PutTile(Position(1, 0), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, 0), Rotation180)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(2, 0), Rotation90)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, -1), Rotation270)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(2, -1), Rotation90)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(PutPiece(SmallPiece, Monk))
            game.dispatch(PutTile(Position(1, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(PutPiece(SmallPiece, Monk))
            game.dispatch(PutTile(Position(-1, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(2, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, -2), Rotation270)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            val events = game.dispatch(PutTile(Position(1, -2), Rotation90))
            events shouldContain PlayerScored(1, 9, setOf(SmallPiece))
            events shouldContain PlayerScored(2, 9, setOf(SmallPiece))
        }

        it("should detect two cloisters of same player completed in single move") {
            val game = multiPlayer(TileD, TileW, TileV, TileE, TileB, TileE, TileB, TileH, TileH, TileK, TileJ)
            game.dispatch(PutTile(Position(1, 0), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, 0), Rotation180)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(2, 0), Rotation90)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(-1, -1), Rotation270)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(PutPiece(SmallPiece, Monk))
            game.dispatch(PutTile(Position(2, -1), Rotation90)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(1, -1), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(PutPiece(SmallPiece, Monk))
            game.dispatch(PutTile(Position(-1, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(2, -2), NoRotation)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            game.dispatch(PutTile(Position(0, -2), Rotation270)) shouldNotContain PlayerScored(1, 9, setOf(SmallPiece))
            game.dispatch(SkipPiece)
            val events = game.dispatch(PutTile(Position(1, -2), Rotation90))
            events shouldEqual listOf(PlayerScored(1, 9, setOf(SmallPiece)), PlayerScored(1, 9, setOf(SmallPiece)), SelectPiece)
        }

    }

})