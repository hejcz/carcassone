package io.github.hejcz.inn

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.GameScenario
import io.github.hejcz.helper.InnAndCathedralsTestGameSetup
import io.github.hejcz.helper.TestBasicRemainingTiles
import io.github.hejcz.inn.tiles.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object InnAndCathedralsExtensionSpec : Spek({

    fun singlePlayer(vararg tiles: Tile) = Game(
        setOf(Player(id = 1, order = 1, initialPieces = listOf(SmallPiece, BigPiece))),
        InnAndCathedralsTestGameSetup(TestBasicRemainingTiles(*tiles))
    ).apply { dispatch(Begin) }

    fun multiPlayer(vararg tiles: Tile) = Game(
        setOf(
            Player(id = 1, order = 1, initialPieces = listOf(SmallPiece, SmallPiece, SmallPiece, BigPiece)),
            Player(id = 2, order = 2, initialPieces = listOf(SmallPiece, SmallPiece, SmallPiece, BigPiece))
        ),
        InnAndCathedralsTestGameSetup(TestBasicRemainingTiles(*tiles))
    ).apply { dispatch(Begin) }

    describe("should score 2 points per castle and emblem in finished castle with cathedral") {
        it("castle with emblem and without cathedral") {
            GameScenario(singlePlayer(TileC, TileD, TileD, TileD, TileD))
                .then(PutTile(Position(0, 1), NoRotation))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .then(PutTile(Position(1, 1), Rotation270))
                .then(SkipPiece)
                .then(PutTile(Position(0, 2), Rotation180))
                .then(SkipPiece)
                .then(PutTile(Position(-1, 1), Rotation90))
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        1,
                        12,
                        setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down)))
                    )
                )
        }

        it("castle without emblem and without cathedral") {
            GameScenario(singlePlayer(TileD, TileD))
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
    }

    describe("should score 3 points per castle and emblem in finished castle with cathedral") {

        it("smallest castle with cathedral") {
            GameScenario(singlePlayer(TileEK, TileD, TileD, TileD, TileD))
                .then(PutTile(Position(0, 1), NoRotation))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .then(PutTile(Position(1, 1), Rotation270))
                .then(SkipPiece)
                .then(PutTile(Position(0, 2), Rotation180))
                .then(SkipPiece)
                .then(PutTile(Position(-1, 1), Rotation90))
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        1,
                        15,
                        setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down)))
                    )
                )
        }

        it("bigger castle with cathedral") {
            GameScenario(singlePlayer(TileEK, TileG, TileE, TileE, TileE, TileE))
                .then(PutTile(Position(0, 1), NoRotation))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .then(PutTile(Position(1, 1), NoRotation))
                .then(SkipPiece)
                .then(PutTile(Position(0, 2), Rotation180))
                .then(SkipPiece)
                .then(PutTile(Position(2, 1), Rotation270))
                .then(SkipPiece)
                .then(PutTile(Position(-1, 1), Rotation90))
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        1,
                        18,
                        setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down)))
                    )
                )
        }

        it("castle with two cathedral is treated the same as with single cathedral") {
            GameScenario(singlePlayer(TileEK, TileEK, TileE, TileE, TileE, TileE, TileE, TileE))
                .then(PutTile(Position(0, 1), NoRotation))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .then(PutTile(Position(0, 2), NoRotation))
                .then(SkipPiece)
                .then(PutTile(Position(1, 1), Rotation270))
                .then(SkipPiece)
                .then(PutTile(Position(1, 2), Rotation270))
                .then(SkipPiece)
                .then(PutTile(Position(0, 3), Rotation180))
                .then(SkipPiece)
                .then(PutTile(Position(-1, 1), Rotation90))
                .then(SkipPiece)
                .then(PutTile(Position(-1, 2), Rotation90))
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        1,
                        24,
                        setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down)))
                    )
                )
        }

        it("castle with cathedral and emblem") {
            GameScenario(singlePlayer(TileEK, TileC, TileE, TileE, TileE, TileE, TileE, TileE))
                .then(PutTile(Position(0, 1), NoRotation))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .then(PutTile(Position(0, 2), NoRotation))
                .then(SkipPiece)
                .then(PutTile(Position(1, 1), Rotation270))
                .then(SkipPiece)
                .then(PutTile(Position(1, 2), Rotation270))
                .then(SkipPiece)
                .then(PutTile(Position(0, 3), Rotation180))
                .then(SkipPiece)
                .then(PutTile(Position(-1, 1), Rotation90))
                .then(SkipPiece)
                .then(PutTile(Position(-1, 2), Rotation90))
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        1,
                        27,
                        setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down)))
                    )
                )
        }

        it("castle with cathedral and multiple emblem") {
            GameScenario(singlePlayer(TileEK, TileC, TileF, TileE, TileE, TileE, TileE, TileE, TileE))
                .then(PutTile(Position(0, 1), NoRotation))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .then(PutTile(Position(0, 2), NoRotation))
                .then(SkipPiece)
                .then(PutTile(Position(1, 1), NoRotation))
                .then(SkipPiece)
                .then(PutTile(Position(2, 1), Rotation270))
                .then(SkipPiece)
                .then(PutTile(Position(1, 2), Rotation270))
                .then(SkipPiece)
                .then(PutTile(Position(0, 3), Rotation180))
                .then(SkipPiece)
                .then(PutTile(Position(-1, 1), Rotation90))
                .then(SkipPiece)
                .then(PutTile(Position(-1, 2), Rotation90))
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        1,
                        33,
                        setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down)))
                    )
                )
        }
    }

    describe("should score 0 points per unfinished castle with cathedral") {

        it("small castle with cathedral") {
            GameScenario(singlePlayer(TileEK, TileD, TileD))
                .then(PutTile(Position(0, 1), NoRotation))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .then(PutTile(Position(1, 1), Rotation270))
                .then(SkipPiece)
                .then(PutTile(Position(0, 2), Rotation180))
                .then(SkipPiece).thenNoEventsShouldBePublished()
        }

        it("bigger castle with cathedral") {
            GameScenario(singlePlayer(TileEK, TileG, TileE, TileE))
                .then(PutTile(Position(0, 1), NoRotation))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .then(PutTile(Position(1, 1), NoRotation))
                .then(SkipPiece)
                .then(PutTile(Position(0, 2), Rotation180))
                .then(SkipPiece)
                .then(PutTile(Position(-1, 1), Rotation90))
                .then(SkipPiece).thenNoEventsShouldBePublished()
        }
    }

    describe("should score 1 points per unfinished castle without cathedral") {

        it("small castle without cathedral") {
            GameScenario(singlePlayer(TileC, TileD, TileD))
                .then(PutTile(Position(0, 1), NoRotation))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .then(PutTile(Position(1, 1), Rotation270))
                .then(SkipPiece)
                .then(PutTile(Position(0, 2), Rotation180))
                .then(SkipPiece)
                .thenReceivedEventShouldBe(PlayerScored(1, 5, emptySet()))
        }

        it("bigger castle without cathedral") {
            GameScenario(singlePlayer(TileC, TileF, TileE, TileE))
                .then(PutTile(Position(0, 1), NoRotation))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .then(PutTile(Position(1, 1), NoRotation))
                .then(SkipPiece)
                .then(PutTile(Position(0, 2), Rotation180))
                .then(SkipPiece)
                .then(PutTile(Position(-1, 1), Rotation90))
                .then(SkipPiece)
                .thenReceivedEventShouldBe(PlayerScored(1, 7, emptySet()))
        }
    }

    describe("big piece") {

        it("single should be available") {
            GameScenario(singlePlayer(TileD, TileD))
                .then(PutTile(Position(1, 0), NoRotation)).shouldContainSelectPiece()
                .then(PutPiece(BigPiece, Knight(Up))).shouldContainPlaceTileOnly()
        }

        it("can not be placed twice before recover") {
            GameScenario(singlePlayer(TileD, TileD, TileD))
                .then(PutTile(Position(1, 0), NoRotation)).shouldContainSelectPiece()
                .then(PutPiece(BigPiece, Knight(Up))).shouldContainPlaceTileOnly()
                .then(PutTile(Position(2, 0), NoRotation)).shouldContainSelectPiece()
                .then(PutPiece(BigPiece, Knight(Up)))
                .thenReceivedEventShouldBe(NoMappleAvailable(BigPiece))
        }

        it("wins with small piece") {
            GameScenario(multiPlayer(TileD, TileM, TileM))
                .then(PutTile(Position(1, 0), NoRotation))
                .then(PutPiece(BigPiece, Knight(Up)))
                .then(PutTile(Position(0, 1), Rotation90))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .then(PutTile(Position(1, 1), Rotation180))
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        1,
                        12,
                        setOf(PieceOnBoard(Position(1, 0), BigPiece, Knight(Up)))
                    )
                )
                .thenShouldNotReceiveEvent(
                    PlayerScored(
                        2,
                        12,
                        setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down)))
                    )
                )
                .thenReceivedEventShouldBe(
                    PlayerDidNotScore(
                        2,
                        setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down)))
                    )
                )
        }

        it("draws with 2 small pieces") {
            GameScenario(multiPlayer(TileD, TileR, TileD, TileM, TileM))
                .then(PutTile(Position(1, 0), NoRotation))
                .then(PutPiece(SmallPiece, Knight(Up)))
                .then(PutTile(Position(0, 1), Rotation180))
                .then(PutPiece(BigPiece, Knight(Down)))
                .then(PutTile(Position(-1, 0), NoRotation))
                .then(PutPiece(SmallPiece, Knight(Up)))
                .then(PutTile(Position(1, 1), Rotation180))
                .then(SkipPiece)
                .then(PutTile(Position(-1, 1), Rotation90))
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        1,
                        16,
                        setOf(
                            PieceOnBoard(Position(1, 0), SmallPiece, Knight(Up)),
                            PieceOnBoard(Position(-1, 0), SmallPiece, Knight(Up))
                        )
                    )
                )
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        2,
                        16,
                        setOf(PieceOnBoard(Position(0, 1), BigPiece, Knight(Down)))
                    )
                )
        }
    }

    describe("should score 2 points for road tile if there is one inn near road") {

        it("small road") {
            GameScenario(singlePlayer(TileEC, TileEF))
                .then(PutTile(Position(1, 0), NoRotation))
                .then(PutPiece(SmallPiece, Brigand(Left)))
                .then(PutTile(Position(-1, 0), NoRotation))
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        1,
                        6,
                        setOf(PieceOnBoard(Position(1, 0), SmallPiece, Brigand(Left)))
                    )
                )
        }
    }

    describe("should score 1 points for road tile if there is no inn near road") {

        it("small road") {
            GameScenario(singlePlayer(TileW, TileEF))
                .then(PutTile(Position(1, 0), NoRotation))
                .then(PutPiece(SmallPiece, Brigand(Left)))
                .then(PutTile(Position(-1, 0), NoRotation))
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        1,
                        3,
                        setOf(PieceOnBoard(Position(1, 0), SmallPiece, Brigand(Left)))
                    )
                )
        }
    }

    describe("should score 0 points for road tile if there is one inn near incomplete road") {

        it("small road") {
            GameScenario(singlePlayer(TileEC))
                .then(PutTile(Position(1, 0), NoRotation))
                .then(PutPiece(SmallPiece, Brigand(Left)))
                .thenNoEventsShouldBePublished()
        }
    }

    describe("should score 1 points for road tile if there is no inn near incomplete road") {

        it("small road") {
            GameScenario(singlePlayer(TileW))
                .then(PutTile(Position(1, 0), NoRotation))
                .then(PutPiece(SmallPiece, Brigand(Left)))
                .thenReceivedEventShouldBe(PlayerScored(1, 2, emptySet()))
        }
    }

    describe("big piece") {

        it("should be returned if road is finished") {
            GameScenario(singlePlayer(TileL, TileU))
                .then(PutTile(Position(1, 0), NoRotation))
                .then(PutPiece(BigPiece, Brigand(Left)))
                .then(PutTile(Position(-1, 0), Rotation90))
                .then(PutPiece(BigPiece, Peasant(Location(Up))))
                .thenReceivedEventShouldBe(NoMappleAvailable(BigPiece))
        }

        it("should be returned if road is finished") {
            GameScenario(singlePlayer(TileL, TileL))
                .then(PutTile(Position(1, 0), NoRotation))
                .then(PutPiece(BigPiece, Brigand(Left)))
                .then(PutTile(Position(-1, 0), NoRotation))
                .then(PutPiece(BigPiece, Brigand(Left)))
                .thenShouldNotReceiveEvent(NoMappleAvailable(BigPiece))
        }
    }

    describe("emblems") {

        it("may exists on 1 out of 2 castles on a single tile - castle with emblem") {
            GameScenario(singlePlayer(TileEP, TileEJ))
                .then(PutTile(Position(0, 1), Rotation90))
                .then(PutPiece(SmallPiece, Knight(Right)))
                .then(PutTile(Position(1, 1), Rotation270))
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        1,
                        8,
                        setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Right)))
                    )
                )
        }

        it("may exists on 1 out of 2 castles on a single tile - castle without emblem") {
            GameScenario(singlePlayer(TileEP))
                .then(PutTile(Position(0, 1), Rotation270))
                .then(PutPiece(SmallPiece, Knight(Down)))
                .thenReceivedEventShouldBe(
                    PlayerScored(
                        1,
                        4,
                        setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Down)))
                    )
                )
        }
    }

    // TODO
    describe("no ordinary castle shapes") {

        it("should be handled correctly - EP") {
        }

        it("should be handled correctly - EG") {
        }
    }
})
