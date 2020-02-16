package io.github.hejcz.magic

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.helper.*
import io.github.hejcz.inn.tiles.TileEK
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object WitchAndMagicianSpec : Spek({

    fun single(setup: (RemainingTiles) -> TestGameSetup, vararg tiles: Tile) =
        Game(
            setOf(Player(id = 1, order = 1, initialPieces = listOf(SmallPiece, BigPiece))),
            setup(TestBasicRemainingTiles(*tiles)),
            verbose = true
        ).dispatch(BeginCmd)

    describe("magician increases score") {
        it("castle - increases score by number of tiles (not emblems)") {
            GameScenario(single(::WitchAndMagicianGameSetup, MaHeD, TileE))
                .then(TileCmd(Position(0, 1), Rotation90)) // MaHeD
                .thenReceivedEventShouldBe(PlaceWitchOrMagician)
                .then(MoveMagicianOrWitchCmd(Position(0, 1), Up, MagicTarget.MAGICIAN))
                .thenReceivedEventShouldBe(PieceEvent)
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, 2), Rotation180)) // E
                .thenReceivedEventShouldBe(ScoreEvent(
                    1, 4 + 2, setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Up)))))
        }

        it("castle - increases score by number of tiles (not emblems)") {
            GameScenario(single(::WitchAndMagicianGameSetup, MaHeD, TileF, TileE))
                .then(TileCmd(Position(0, 1), Rotation90)) // MaHeD
                .thenReceivedEventShouldBe(PlaceWitchOrMagician)
                .then(MoveMagicianOrWitchCmd(Position(0, 1), Up, MagicTarget.MAGICIAN))
                .thenReceivedEventShouldBe(PieceEvent)
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, 2), Rotation90)) // F
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, 3), Rotation180)) // E
                .thenReceivedEventShouldBe(ScoreEvent(
                    1, 8 + 3, setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Up)))))
        }

        it("castle - score increase is added after cathedral evaluation") {
            GameScenario(single(::WitchAndMagicianAndInnAndCathedralsGameSetup, MaHeD, TileEK, TileE, TileE, TileE))
                .then(TileCmd(Position(0, 1), Rotation90)) // MaHeD
                .then(MoveMagicianOrWitchCmd(Position(0, 1), Up, MagicTarget.MAGICIAN))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(0, 2), NoRotation)) // EK
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 2), Rotation90)) // E
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, 3), Rotation180)) // E
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 2), Rotation270)) // E
                .thenReceivedEventShouldBe(ScoreEvent(
                    1, 15 + 5, setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Up)))))
        }
    }

    describe("witch decreases score") {
        it("from castle by half") {
            GameScenario(single(::WitchAndMagicianGameSetup, MaHeD, TileE))
                .then(TileCmd(Position(0, 1), Rotation90)) // MaHeD
                .thenReceivedEventShouldBe(PlaceWitchOrMagician)
                .then(MoveMagicianOrWitchCmd(Position(0, 1), Up, MagicTarget.WITCH))
                .thenReceivedEventShouldBe(PieceEvent)
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, 2), Rotation180)) // E
                .thenReceivedEventShouldBe(ScoreEvent(
                    1, 4 - 2, setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Up)))))
        }

        it("from castle by half rounded up including cathedral bonus") {
            GameScenario(single(::WitchAndMagicianAndInnAndCathedralsGameSetup, MaHeD, TileEK, TileE, TileE, TileE))
                .then(TileCmd(Position(0, 1), Rotation90)) // MaHeD
                .then(MoveMagicianOrWitchCmd(Position(0, 1), Up, MagicTarget.WITCH))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(0, 2), NoRotation)) // EK
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 2), Rotation90)) // E
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, 3), Rotation180)) // E
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 2), Rotation270)) // E
                .thenReceivedEventShouldBe(ScoreEvent(
                    1, 15 - 7, setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Up)))))
        }
    }

    describe("rules") {
        it("can be placed on open objects only 1") {
            GameScenario(single(::WitchAndMagicianAndInnAndCathedralsGameSetup, MaHeB))
                .then(TileCmd(Position(0, 1), Rotation90))
                .then(MoveMagicianOrWitchCmd(Position(0, 1), Down, MagicTarget.WITCH))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent) // closed castle
        }

        it("can be placed on open objects only 2") {
            GameScenario(single(::WitchAndMagicianAndInnAndCathedralsGameSetup, MaHeB))
                .then(TileCmd(Position(0, 1), Rotation90))
                .then(MoveMagicianOrWitchCmd(Position(0, 1), Right, MagicTarget.WITCH))
                .thenReceivedEventShouldBe(PieceEvent) // open castle
        }

        it("can be placed on open objects only 3") {
            GameScenario(single(::WitchAndMagicianAndInnAndCathedralsGameSetup, MaHeB))
                .then(TileCmd(Position(0, 1), Rotation90))
                .then(MoveMagicianOrWitchCmd(Position(0, 1), Down, MagicTarget.MAGICIAN))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent) // closed castle
        }

        it("can be placed on open objects only 4") {
            GameScenario(single(::WitchAndMagicianAndInnAndCathedralsGameSetup, MaHeB))
                .then(TileCmd(Position(0, 1), Rotation90))
                .then(MoveMagicianOrWitchCmd(Position(0, 1), Right, MagicTarget.MAGICIAN))
                .thenReceivedEventShouldBe(PieceEvent) // open castle
        }

        it("can be placed on open objects only 5") {
            GameScenario(single(::WitchAndMagicianAndInnAndCathedralsGameSetup, TileA, MaHeA))
                .then(TileCmd(Position(-1, 0), Rotation270))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(MoveMagicianOrWitchCmd(Position(1, 0), Left, MagicTarget.MAGICIAN))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent) // closed road
        }

        it("can be placed on open objects only 6") {
            GameScenario(single(::WitchAndMagicianAndInnAndCathedralsGameSetup, TileA, MaHeA))
                .then(TileCmd(Position(-1, 0), Rotation270))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(MoveMagicianOrWitchCmd(Position(1, 0), Right, MagicTarget.MAGICIAN))
                .thenReceivedEventShouldBe(PieceEvent) // open road
        }

        it("can be placed on open objects only 7") {
            GameScenario(single(::WitchAndMagicianAndInnAndCathedralsGameSetup, TileA, MaHeA))
                .then(TileCmd(Position(-1, 0), Rotation270))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(MoveMagicianOrWitchCmd(Position(1, 0), Left, MagicTarget.WITCH))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent) // closed road
        }

        it("can be placed on open objects only 8") {
            GameScenario(single(::WitchAndMagicianAndInnAndCathedralsGameSetup, TileA, MaHeA))
                .then(TileCmd(Position(-1, 0), Rotation270))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(MoveMagicianOrWitchCmd(Position(1, 0), Right, MagicTarget.WITCH))
                .thenReceivedEventShouldBe(PieceEvent) // open road
        }

        it("can be placed on other tile than recent") {
            GameScenario(single(::WitchAndMagicianAndInnAndCathedralsGameSetup, TileA, MaHeA))
                .then(TileCmd(Position(-1, 0), Rotation270))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(MoveMagicianOrWitchCmd(Position(0, 0), Up, MagicTarget.WITCH))
                .thenReceivedEventShouldBe(PieceEvent) // open castle on first tile
        }

        it("can't be placed on same object 1") {
            GameScenario(single(::WitchAndMagicianAndInnAndCathedralsGameSetup, TileC, MaHeA, MaHeE))
                .then(TileCmd(Position(0, 1), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 1), Rotation90))
                .then(MoveMagicianOrWitchCmd(Position(0, 0), Up, MagicTarget.MAGICIAN))
                .thenReceivedEventShouldBe(PieceEvent)
                .then(TileCmd(Position(1, 1), NoRotation))
                .then(MoveMagicianOrWitchCmd(Position(-1, 1), Right, MagicTarget.MAGICIAN))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }

        it("can't be placed on same object 2") {
            GameScenario(single(::WitchAndMagicianAndInnAndCathedralsGameSetup, TileC, MaHeA, MaHeE))
                .then(TileCmd(Position(0, 1), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 1), Rotation90))
                .then(MoveMagicianOrWitchCmd(Position(0, 0), Up, MagicTarget.MAGICIAN))
                .thenReceivedEventShouldBe(PieceEvent)
                .then(TileCmd(Position(1, 1), NoRotation))
                .then(MoveMagicianOrWitchCmd(Position(-1, 1), Right, MagicTarget.WITCH))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }

        it("can't be placed on same object 3") {
            GameScenario(single(::WitchAndMagicianAndInnAndCathedralsGameSetup, TileC, MaHeA, MaHeE))
                .then(TileCmd(Position(0, 1), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 1), Rotation90))
                .then(MoveMagicianOrWitchCmd(Position(0, 0), Up, MagicTarget.WITCH))
                .thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 1), NoRotation))
                .then(MoveMagicianOrWitchCmd(Position(-1, 1), Right, MagicTarget.WITCH))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }

        it("can't be placed on same object 4") {
            GameScenario(single(::WitchAndMagicianAndInnAndCathedralsGameSetup, TileC, MaHeA, MaHeE))
                .then(TileCmd(Position(0, 1), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 1), Rotation90))
                .then(MoveMagicianOrWitchCmd(Position(0, 0), Up, MagicTarget.WITCH))
                .thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 1), NoRotation))
                .then(MoveMagicianOrWitchCmd(Position(-1, 1), Right, MagicTarget.MAGICIAN))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }

        it("can be moved to other open object 1") {
            GameScenario(single(::WitchAndMagicianAndInnAndCathedralsGameSetup, TileC, MaHeA, MaHeE))
                .then(TileCmd(Position(0, 1), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 1), Rotation90))
                .then(MoveMagicianOrWitchCmd(Position(0, 0), Up, MagicTarget.MAGICIAN))
                .thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 1), NoRotation))
                .then(MoveMagicianOrWitchCmd(Position(0, 0), Right, MagicTarget.MAGICIAN))
                .thenReceivedEventShouldBe(PieceEvent)
        }

        it("can be moved to other open object 2") {
            GameScenario(single(::WitchAndMagicianAndInnAndCathedralsGameSetup, TileC, MaHeA, MaHeE))
                .then(TileCmd(Position(0, 1), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 1), Rotation90))
                .then(MoveMagicianOrWitchCmd(Position(0, 0), Up, MagicTarget.WITCH))
                .thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 1), NoRotation))
                .then(MoveMagicianOrWitchCmd(Position(0, 0), Right, MagicTarget.WITCH))
                .thenReceivedEventShouldBe(PieceEvent)
        }

        it("delays points calculation if npc must be moved 1") {
            GameScenario(single(::WitchAndMagicianAndInnAndCathedralsGameSetup, MaHeA, MaHeF, TileP, TileK))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(MoveMagicianOrWitchCmd(Position(1, 0), Up, MagicTarget.WITCH))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(0, 1), Rotation180))
                .then(MoveMagicianOrWitchCmd(Position(0, 1), Down, MagicTarget.MAGICIAN))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 1), Rotation270))
                .thenReceivedEventShouldBe(PlaceWitchOrMagician)
                .thenReceivedEventsShouldHaveType<CastleClosedEvent>()
                .thenEventsCountShouldBe(2)
                .then(MoveMagicianOrWitchCmd(Position(0, 0), Left, MagicTarget.MAGICIAN))
                .thenReceivedEventShouldBe(ScoreEvent(1, 8 - 4, setOf(PieceOnBoard(Position(1, 0), SmallPiece, Knight(Up)))))
        }

        it("delays points calculation if npc must be moved 2") {
            GameScenario(single(::WitchAndMagicianAndInnAndCathedralsGameSetup, MaHeA, MaHeF, TileP, TileK))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(MoveMagicianOrWitchCmd(Position(1, 0), Up, MagicTarget.WITCH))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(0, 1), Rotation180))
                .then(MoveMagicianOrWitchCmd(Position(0, 1), Down, MagicTarget.MAGICIAN))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 1), Rotation270))
                .thenReceivedEventShouldBe(PlaceWitchOrMagician)
                .thenReceivedEventsShouldHaveType<CastleClosedEvent>()
                .thenEventsCountShouldBe(2)
                .then(MoveMagicianOrWitchCmd(Position(0, 0), Left, MagicTarget.WITCH))
                .thenReceivedEventShouldBe(ScoreEvent(1, 8 + 4, setOf(PieceOnBoard(Position(1, 0), SmallPiece, Knight(Up)))))
        }
    }

    describe("pick up rules") {

        it("can't pick up instead of move 1") {
            GameScenario(single(::WitchAndMagicianAndInnAndCathedralsGameSetup, MaHeA, MaHeF, TileP, TileK))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(MoveMagicianOrWitchCmd(Position(1, 0), Up, MagicTarget.WITCH))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(0, 1), Rotation180))
                .then(PickUpMagicianOrWitchCmd(MagicTarget.WITCH))
                .thenReceivedEventShouldBe(CantPickUpPieceEvent(MagicTarget.WITCH))
        }

        it("can't pick up instead of move 2") {
            GameScenario(single(::WitchAndMagicianAndInnAndCathedralsGameSetup, MaHeA, MaHeF, TileP, TileK))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(MoveMagicianOrWitchCmd(Position(1, 0), Up, MagicTarget.MAGICIAN))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(0, 1), Rotation180))
                .then(PickUpMagicianOrWitchCmd(MagicTarget.MAGICIAN))
                .thenReceivedEventShouldBe(CantPickUpPieceEvent(MagicTarget.MAGICIAN))
        }

        it("can't pick up instead of move 3") {
            GameScenario(single(::WitchAndMagicianAndInnAndCathedralsGameSetup, MaHeA, MaHeF, TileP, TileK))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(MoveMagicianOrWitchCmd(Position(1, 0), Up, MagicTarget.WITCH))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(0, 1), Rotation180))
                .then(PickUpMagicianOrWitchCmd(MagicTarget.MAGICIAN))
                .thenReceivedEventShouldBe(CantPickUpPieceEvent(MagicTarget.MAGICIAN))
        }

        it("can't pick up instead of move 4") {
            GameScenario(single(::WitchAndMagicianAndInnAndCathedralsGameSetup, MaHeA, MaHeF, TileP, TileK))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(MoveMagicianOrWitchCmd(Position(1, 0), Up, MagicTarget.WITCH))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(0, 1), Rotation180))
                .then(PickUpMagicianOrWitchCmd(MagicTarget.MAGICIAN))
                .thenReceivedEventShouldBe(CantPickUpPieceEvent(MagicTarget.MAGICIAN))
        }

        it("can pick up if there is no chance to move") {
            GameScenario(single(::WitchAndMagicianAndInnAndCathedralsGameSetup,
                TileA, MaHeD, TileB, TileB, TileB, TileE, MaHeD, TileB))
                .then(TileCmd(Position(-1, 0), Rotation270))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, 1), Rotation90))
                .then(MoveMagicianOrWitchCmd(Position(0, 0), Left, MagicTarget.WITCH))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 1), Rotation90))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 2), Rotation90))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 3), Rotation90))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, 3), Rotation180))
                .then(PieceCmd(SmallPiece, Knight(Down)))
                .then(TileCmd(Position(0, 2), Rotation90))
                .thenReceivedEventShouldBe(PlaceWitchOrMagician)
                .then(PickUpMagicianOrWitchCmd(MagicTarget.WITCH))
                .thenReceivedEventShouldBe(PieceEvent)
        }

        it("should not have points scored twice when magic tile closes object") {
            GameScenario(single(::WitchAndMagicianAndInnAndCathedralsGameSetup, TileG, MaHeB))
                .then(TileCmd(Position(0, 1), Rotation90))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(0, 2), Rotation90))
                .thenReceivedEventShouldBe(
                    ScoreEvent(1, 6, setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Up))))
                )
                .then(MoveMagicianOrWitchCmd(Position(0, 0), Left, MagicTarget.WITCH))
                .thenShouldNotReceiveEvent(
                    ScoreEvent(1, 6, setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Up))))
                )
        }
    }
})