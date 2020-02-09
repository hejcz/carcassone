package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object CompletedCastleDetectionSpec : Spek({

    describe("A castle detector") {

        fun singlePlayer(vararg tiles: Tile) = Game(
            Players.singlePlayer(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        ).apply { dispatch(Begin) }

        fun multiPlayer(vararg tiles: Tile) = Game(
            Players.twoPlayers(),
            TestGameSetup(TestBasicRemainingTiles(*tiles))
        ).apply { dispatch(Begin) }

        it("should not detect incomplete castle with knight") {
            GameScenario(singlePlayer(TileD, TileB))
                .then(PutTile(Position(1, 0), NoRotation))
                .then(PutPiece(SmallPiece, Knight(Up)))
                .thenReceivedEventShouldBeOnlyPlaceTile()
        }

        it("should not detect incomplete castle without knight") {
            GameScenario(singlePlayer(TileD, TileB))
                .then(PutTile(Position(1, 0), NoRotation))
                .then(SkipPiece)
                .thenReceivedEventShouldBeOnlyPlaceTile()
        }

        it("should detect XS completed castle") {
            GameScenario(singlePlayer(TileD))
                .then(PutTile(Position(0, 1), Rotation180))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        1,
                        4,
                        setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down)))
                    )
                )
        }

        it("should detect S completed castle") {
            GameScenario(singlePlayer(TileG, TileD))
                .then(PutTile(Position(0, 1), Rotation90))
                .then(SkipPiece)
                .then(PutTile(Position(0, 2), Rotation180))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        1,
                        6,
                        setOf(PieceOnBoard(Position(0, 2), SmallPiece, Knight(Down)))
                    )
                )
        }

        it("should not detect S incomplete castle") {
            GameScenario(singlePlayer(TileR, TileD, TileD))
                .then(PutTile(Position(0, 1), Rotation90))
                .then(SkipPiece)
                .then(PutTile(Position(0, 2), Rotation180))
                .then(PutPiece(SmallPiece, Knight(Down))).thenReceivedEventShouldBeOnlyPlaceTile()
        }

        it("should detect M incomplete castle") {
            GameScenario(singlePlayer(TileR, TileD, TileD))
                .then(PutTile(Position(0, 1), Rotation90))
                .then(SkipPiece)
                .then(PutTile(Position(0, 2), Rotation180))
                .then(SkipPiece)
                .then(PutTile(Position(1, 1), Rotation270))
                .then(PutPiece(SmallPiece, Knight(Left)))
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        1,
                        8,
                        setOf(PieceOnBoard(Position(1, 1), SmallPiece, Knight(Left)))
                    )
                )
        }

        it("should detect mapples placed before castle completion") {
            GameScenario(singlePlayer(TileR, TileD, TileD))
                .then(PutTile(Position(0, 1), Rotation90))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .then(PutTile(Position(0, 2), Rotation180))
                .then(SkipPiece)
                .then(PutTile(Position(1, 1), Rotation270))
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        1,
                        8,
                        setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down)))
                    )
                )
        }

        it("should return all mapples placed on completed castle") {
            GameScenario(singlePlayer(TileR, TileD, TileD, TileN))
                .then(PutTile(Position(0, 1), Rotation90))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .then(PutTile(Position(0, 2), Rotation180))
                .then(SkipPiece)
                .then(PutTile(Position(1, 2), Rotation180))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .then(PutTile(Position(1, 1), Rotation270))
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        1,
                        10,
                        setOf(
                            PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down)),
                            PieceOnBoard(Position(1, 2), SmallPiece, Knight(Down))
                        )
                    )
                )
        }

        it("should not give any reward if no handlers were on completed castle") {
            GameScenario(singlePlayer(TileR, TileD, TileD, TileN, TileD))
                        .then(PutTile(Position(0, 1), Rotation90))
                        .then(SkipPiece)
                        .then(PutTile(Position(0, 2), Rotation180))
                        .then(SkipPiece)
                        .then(PutTile(Position(1, 2), Rotation180))
                        .then(SkipPiece)
                        .then(PutTile(Position(1, 1), Rotation270)).thenReceivedEventShouldBe(SelectPiece)
                .then(SkipPiece).thenReceivedEventShouldBeOnlyPlaceTile()
        }

        it("should modify score if emblems are available on castle") {
            GameScenario(singlePlayer(TileF, TileM, TileD))
                .then(PutTile(Position(0, 1), Rotation90))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .then(PutTile(Position(0, 2), Rotation90))
                .then(SkipPiece)
                .then(PutTile(Position(1, 2), Rotation270))
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        1,
                        12,
                        setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down)))
                    )
                )
        }

        it("should reward many players if they have the same amount of handlers in a castle") {
            GameScenario(multiPlayer(TileR, TileD, TileA, TileD, TileN))
                .then(PutTile(Position(0, 1), Rotation90))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .then(PutTile(Position(0, 2), Rotation180))
                .then(SkipPiece)
                .then(PutTile(Position(1, 0), Rotation90))
                .then(SkipPiece)
                .then(PutTile(Position(1, 2), Rotation180))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .then(PutTile(Position(1, 1), Rotation270))
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        1,
                        10,
                        setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down)))
                    )
                )
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        2,
                        10,
                        setOf(PieceOnBoard(Position(1, 2), SmallPiece, Knight(Down)))
                    )
                )
        }

        it("should reward only one player if he has advantage of handlers in castle") {
            GameScenario(multiPlayer(TileR, TileD, TileD, TileD, TileR))
                .then(PutTile(Position(0, 1), Rotation90))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .then(PutTile(Position(0, 2), Rotation180))
                .then(SkipPiece)
                .then(PutTile(Position(1, 0), NoRotation))
                .then(PutPiece(SmallPiece, Knight(Up)))
                .then(PutTile(Position(1, 2), Rotation180))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .then(PutTile(Position(1, 1), Rotation270))
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        1,
                        12,
                        setOf(
                            PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down)),
                            PieceOnBoard(Position(1, 0), SmallPiece, Knight(Up))
                        )
                    )
                )
                .thenReceivedEventShouldBe(
                    PlayerDidNotScore(
                        2,
                        setOf(PieceOnBoard(Position(1, 2), SmallPiece, Knight(Down)))
                    )
                )
        }

        it("should detect that multiple castles were finished with single tile") {
            GameScenario(multiPlayer(TileN, TileD, TileI))
                .then(PutTile(Position(0, 1), Rotation90))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .then(PutTile(Position(1, 0), NoRotation))
                .then(PutPiece(SmallPiece, Knight(Up)))
                .then(PutTile(Position(1, 1), Rotation270))
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        1,
                        6,
                        setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down)))
                    )
                )
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        2,
                        4,
                        setOf(PieceOnBoard(Position(1, 0), SmallPiece, Knight(Up)))
                    )
                )
        }

        it("should return single score event if the same castle is on multiple tiles directions") {
            GameScenario(singlePlayer(TileD, TileN, TileN))
                .then(PutTile(Position(1, 0), NoRotation))
                .then(PutPiece(SmallPiece, Knight(Up)))
                .then(PutTile(Position(0, 1), Rotation90))
                .then(SkipPiece)
                .then(PutTile(Position(1, 1), Rotation180))
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        1,
                        8,
                        setOf(PieceOnBoard(Position(1, 0), SmallPiece, Knight(Up)))
                    )
                )
                .thenReceivedEventShouldBe(SelectPiece)
        }
    }
})
