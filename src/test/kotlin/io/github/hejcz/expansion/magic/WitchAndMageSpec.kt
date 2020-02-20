package io.github.hejcz.expansion.magic

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*
import io.github.hejcz.engine.Game
import io.github.hejcz.expansion.inn.tiles.TileEK
import io.github.hejcz.helper.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object WitchAndMageSpec : Spek({

    fun single(setup: (RemainingTiles) -> TestGameSetup, vararg tiles: Tile) =
        Game(
            setOf(Player(id = 1, order = 1, initialPieces = listOf(SmallPiece, BigPiece))),
            setup(TestBasicRemainingTiles(*tiles))
        ).dispatch(BeginCmd)

    describe("mage increases score") {
        it("castle - increases score by number of tiles (not emblems)") {
            GameScenario(single(::WitchAndMageGameSetup, MaHeD, TileE))
                .then(TileCmd(Position(0, 1), Rotation90)) // MaHeD
                .thenReceivedEventShouldBe(PlaceWitchOrMage)
                .then(MoveMageOrWitchCmd(Position(0, 1), Up, Mage))
                .thenReceivedEventShouldBe(PieceEvent)
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, 2), Rotation180)) // E
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBe(ScoreEvent(
                    1, 4 + 2, setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Up)))))
        }

        it("castle - increases score by number of tiles (not emblems)") {
            GameScenario(single(::WitchAndMageGameSetup, MaHeD, TileF, TileE))
                .then(TileCmd(Position(0, 1), Rotation90)) // MaHeD
                .thenReceivedEventShouldBe(PlaceWitchOrMage)
                .then(MoveMageOrWitchCmd(Position(0, 1), Up, Mage))
                .thenReceivedEventShouldBe(PieceEvent)
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, 2), Rotation90)) // F
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, 3), Rotation180)) // E
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBe(ScoreEvent(
                    1, 8 + 3, setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Up)))))
        }

        it("castle - score increase is added after cathedral evaluation") {
            GameScenario(single(::WitchAndMageAndInnAndCathedralsGameSetup, MaHeD, TileEK, TileE, TileE, TileE))
                .then(TileCmd(Position(0, 1), Rotation90)) // MaHeD
                .then(MoveMageOrWitchCmd(Position(0, 1), Up, Mage))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(0, 2), NoRotation)) // EK
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 2), Rotation90)) // E
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, 3), Rotation180)) // E
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 2), Rotation270)) // E
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBe(ScoreEvent(
                    1, 15 + 5, setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Up)))))
        }
    }

    describe("witch decreases score") {
        it("from castle by half") {
            GameScenario(single(::WitchAndMageGameSetup, MaHeD, TileE))
                .then(TileCmd(Position(0, 1), Rotation90)) // MaHeD
                .thenReceivedEventShouldBe(PlaceWitchOrMage)
                .then(MoveMageOrWitchCmd(Position(0, 1), Up, Witch))
                .thenReceivedEventShouldBe(PieceEvent)
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .thenReceivedEventShouldBeOnlyPlaceTile()
                .then(TileCmd(Position(0, 2), Rotation180)) // E
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBe(ScoreEvent(
                    1, 4 - 2, setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Up)))))
        }

        it("from castle by half rounded up including cathedral bonus") {
            GameScenario(single(::WitchAndMageAndInnAndCathedralsGameSetup, MaHeD, TileEK, TileE, TileE, TileE))
                .then(TileCmd(Position(0, 1), Rotation90)) // MaHeD
                .then(MoveMageOrWitchCmd(Position(0, 1), Up, Witch))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(0, 2), NoRotation)) // EK
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 2), Rotation90)) // E
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, 3), Rotation180)) // E
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 2), Rotation270)) // E
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBe(ScoreEvent(
                    1, 15 - 7, setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Up)))))
        }
    }

    describe("rules") {
        it("can be placed on open objects only 1") {
            GameScenario(single(::WitchAndMageAndInnAndCathedralsGameSetup, MaHeB))
                .then(TileCmd(Position(0, 1), Rotation90))
                .then(MoveMageOrWitchCmd(Position(0, 1), Down, Witch))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent) // closed castle
        }

        it("can be placed on open objects only 2") {
            GameScenario(single(::WitchAndMageAndInnAndCathedralsGameSetup, MaHeB))
                .then(TileCmd(Position(0, 1), Rotation90))
                .then(MoveMageOrWitchCmd(Position(0, 1), Right, Witch))
                .thenReceivedEventShouldBe(PieceEvent) // open castle
        }

        it("can be placed on open objects only 3") {
            GameScenario(single(::WitchAndMageAndInnAndCathedralsGameSetup, MaHeB))
                .then(TileCmd(Position(0, 1), Rotation90))
                .then(MoveMageOrWitchCmd(Position(0, 1), Down, Mage))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent) // closed castle
        }

        it("can be placed on open objects only 4") {
            GameScenario(single(::WitchAndMageAndInnAndCathedralsGameSetup, MaHeB))
                .then(TileCmd(Position(0, 1), Rotation90))
                .then(MoveMageOrWitchCmd(Position(0, 1), Right, Mage))
                .thenReceivedEventShouldBe(PieceEvent) // open castle
        }

        it("can be placed on open objects only 5") {
            GameScenario(single(::WitchAndMageAndInnAndCathedralsGameSetup, TileA, MaHeA))
                .then(TileCmd(Position(-1, 0), Rotation270))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(MoveMageOrWitchCmd(Position(1, 0), Left, Mage))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent) // closed road
        }

        it("can be placed on open objects only 6") {
            GameScenario(single(::WitchAndMageAndInnAndCathedralsGameSetup, TileA, MaHeA))
                .then(TileCmd(Position(-1, 0), Rotation270))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(MoveMageOrWitchCmd(Position(1, 0), Right, Mage))
                .thenReceivedEventShouldBe(PieceEvent) // open road
        }

        it("can be placed on open objects only 7") {
            GameScenario(single(::WitchAndMageAndInnAndCathedralsGameSetup, TileA, MaHeA))
                .then(TileCmd(Position(-1, 0), Rotation270))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(MoveMageOrWitchCmd(Position(1, 0), Left, Witch))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent) // closed road
        }

        it("can be placed on open objects only 8") {
            GameScenario(single(::WitchAndMageAndInnAndCathedralsGameSetup, TileA, MaHeA))
                .then(TileCmd(Position(-1, 0), Rotation270))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(MoveMageOrWitchCmd(Position(1, 0), Right, Witch))
                .thenReceivedEventShouldBe(PieceEvent) // open road
        }

        it("can be placed on other tile than recent") {
            GameScenario(single(::WitchAndMageAndInnAndCathedralsGameSetup, TileA, MaHeA))
                .then(TileCmd(Position(-1, 0), Rotation270))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(MoveMageOrWitchCmd(Position(0, 0), Up, Witch))
                .thenReceivedEventShouldBe(PieceEvent) // open castle on first tile
        }

        it("can't be placed on same object 1") {
            GameScenario(single(::WitchAndMageAndInnAndCathedralsGameSetup, TileC, MaHeA, MaHeE))
                .then(TileCmd(Position(0, 1), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 1), Rotation90))
                .then(MoveMageOrWitchCmd(Position(0, 0), Up, Mage))
                .thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 1), NoRotation))
                .then(MoveMageOrWitchCmd(Position(-1, 1), Right, Mage))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }

        it("can't be placed on same object 2") {
            GameScenario(single(::WitchAndMageAndInnAndCathedralsGameSetup, TileC, MaHeA, MaHeE))
                .then(TileCmd(Position(0, 1), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 1), Rotation90))
                .then(MoveMageOrWitchCmd(Position(0, 0), Up, Mage))
                .thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 1), NoRotation))
                .then(MoveMageOrWitchCmd(Position(-1, 1), Right, Witch))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }

        it("can't be placed on same object 3") {
            GameScenario(single(::WitchAndMageAndInnAndCathedralsGameSetup, TileC, MaHeA, MaHeE))
                .then(TileCmd(Position(0, 1), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 1), Rotation90))
                .then(MoveMageOrWitchCmd(Position(0, 0), Up, Witch))
                .thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 1), NoRotation))
                .then(MoveMageOrWitchCmd(Position(-1, 1), Right, Witch))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }

        it("can't be placed on same object 4") {
            GameScenario(single(::WitchAndMageAndInnAndCathedralsGameSetup, TileC, MaHeA, MaHeE))
                .then(TileCmd(Position(0, 1), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 1), Rotation90))
                .then(MoveMageOrWitchCmd(Position(0, 0), Up, Witch))
                .thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 1), NoRotation))
                .then(MoveMageOrWitchCmd(Position(-1, 1), Right, Mage))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }

        it("can be moved to other open object 1") {
            GameScenario(single(::WitchAndMageAndInnAndCathedralsGameSetup, TileC, MaHeA, MaHeE))
                .then(TileCmd(Position(0, 1), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 1), Rotation90))
                .then(MoveMageOrWitchCmd(Position(0, 0), Up, Mage))
                .thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 1), NoRotation))
                .then(MoveMageOrWitchCmd(Position(0, 0), Right, Mage))
                .thenReceivedEventShouldBe(PieceEvent)
        }

        it("can be moved to other open object 2") {
            GameScenario(single(::WitchAndMageAndInnAndCathedralsGameSetup, TileC, MaHeA, MaHeE))
                .then(TileCmd(Position(0, 1), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-1, 1), Rotation90))
                .then(MoveMageOrWitchCmd(Position(0, 0), Up, Witch))
                .thenReceivedEventShouldBe(PieceEvent)
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 1), NoRotation))
                .then(MoveMageOrWitchCmd(Position(0, 0), Right, Witch))
                .thenReceivedEventShouldBe(PieceEvent)
        }

        it("delays points calculation if npc must be moved 1") {
            GameScenario(single(::WitchAndMageAndInnAndCathedralsGameSetup, MaHeA, MaHeF, TileP, TileK))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(MoveMageOrWitchCmd(Position(1, 0), Up, Witch))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(0, 1), Rotation180))
                .then(MoveMageOrWitchCmd(Position(0, 1), Down, Mage))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 1), Rotation270))
                .thenReceivedEventShouldBe(PlaceWitchOrMage)
                .then(MoveMageOrWitchCmd(Position(0, 0), Left, Mage))
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBe(ScoreEvent(1, 8 - 4, setOf(PieceOnBoard(Position(1, 0), SmallPiece, Knight(Up)))))
        }

        it("delays points calculation if npc must be moved 2") {
            GameScenario(single(::WitchAndMageAndInnAndCathedralsGameSetup, MaHeA, MaHeF, TileP, TileK))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(MoveMageOrWitchCmd(Position(1, 0), Up, Witch))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(0, 1), Rotation180))
                .then(MoveMageOrWitchCmd(Position(0, 1), Down, Mage))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 1), Rotation270))
                .thenReceivedEventShouldBe(PlaceWitchOrMage)
                .then(MoveMageOrWitchCmd(Position(0, 0), Left, Witch))
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBe(ScoreEvent(1, 8 + 4, setOf(PieceOnBoard(Position(1, 0), SmallPiece, Knight(Up)))))
        }
    }

    describe("pick up rules") {

        it("can't pick up instead of move 1") {
            GameScenario(single(::WitchAndMageAndInnAndCathedralsGameSetup, MaHeA, MaHeF, TileP, TileK))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(MoveMageOrWitchCmd(Position(1, 0), Up, Witch))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(0, 1), Rotation180))
                .then(PickUpMageOrWitchCmd(Witch))
                .thenReceivedEventShouldBe(CantPickUpPieceEvent(Witch))
        }

        it("can't pick up instead of move 2") {
            GameScenario(single(::WitchAndMageAndInnAndCathedralsGameSetup, MaHeA, MaHeF, TileP, TileK))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(MoveMageOrWitchCmd(Position(1, 0), Up, Mage))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(0, 1), Rotation180))
                .then(PickUpMageOrWitchCmd(Mage))
                .thenReceivedEventShouldBe(CantPickUpPieceEvent(Mage))
        }

        it("can't pick up instead of move 3") {
            GameScenario(single(::WitchAndMageAndInnAndCathedralsGameSetup, MaHeA, MaHeF, TileP, TileK))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(MoveMageOrWitchCmd(Position(1, 0), Up, Witch))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(0, 1), Rotation180))
                .then(PickUpMageOrWitchCmd(Mage))
                .thenReceivedEventShouldBe(CantPickUpPieceEvent(Mage))
        }

        it("can't pick up instead of move 4") {
            GameScenario(single(::WitchAndMageAndInnAndCathedralsGameSetup, MaHeA, MaHeF, TileP, TileK))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(MoveMageOrWitchCmd(Position(1, 0), Up, Witch))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(0, 1), Rotation180))
                .then(PickUpMageOrWitchCmd(Mage))
                .thenReceivedEventShouldBe(CantPickUpPieceEvent(Mage))
        }

        it("can pick up if there is no chance to move") {
            GameScenario(single(::WitchAndMageAndInnAndCathedralsGameSetup,
                TileA, MaHeD, TileB, TileB, TileB, TileE, MaHeD, TileB))
                .then(TileCmd(Position(-1, 0), Rotation270))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, 1), Rotation90))
                .then(MoveMageOrWitchCmd(Position(0, 0), Left, Witch))
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
                .thenReceivedEventShouldBe(PlaceWitchOrMage)
                .then(PickUpMageOrWitchCmd(Witch))
                .thenReceivedEventShouldBe(PieceEvent)
        }

        it("should not have points scored twice when magic tile closes object") {
            GameScenario(single(::WitchAndMageAndInnAndCathedralsGameSetup, TileG, MaHeB))
                .then(TileCmd(Position(0, 1), Rotation90))
                .then(PieceCmd(SmallPiece, Knight(Up)))
                .then(TileCmd(Position(0, 2), Rotation90))
                .then(MoveMageOrWitchCmd(Position(0, 0), Left, Witch))
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBe(
                    ScoreEvent(1, 6, setOf(PieceOnBoard(Position(0, 1), SmallPiece, Knight(Up))))
                )
        }
    }
})
