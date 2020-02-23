package io.github.hejcz.components.buildersandtraders

import io.github.hejcz.api.*
import io.github.hejcz.base.*
import io.github.hejcz.base.tile.*
import io.github.hejcz.components.buildersandtraders.BuildersAndTradersExtension.BuilderPiece
import io.github.hejcz.components.buildersandtraders.BuildersAndTradersExtension.BuilderRole
import io.github.hejcz.components.buildersandtraders.BuildersAndTradersExtension.PigPiece
import io.github.hejcz.components.buildersandtraders.BuildersAndTradersExtension.PigRole
import io.github.hejcz.engine.Game
import io.github.hejcz.helper.BuildersAndTradersGameSetup
import io.github.hejcz.helper.GameScenario
import io.github.hejcz.helper.TestBasicRemainingTiles
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object BuildersAndTradersSpec : Spek({

    fun singlePlayer(vararg tiles: Tile) = Game(
        setOf(Player(id = 1, order = 1)),
        BuildersAndTradersGameSetup(TestBasicRemainingTiles(*tiles))
    ).dispatch(BeginCmd)

    fun twoPlayers(vararg tiles: Tile) = Game(
        setOf(Player(id = 1, order = 1), Player(id = 2, order = 2)),
        BuildersAndTradersGameSetup(TestBasicRemainingTiles(*tiles))
    ).dispatch(BeginCmd)

    describe("goods") {

        it("give 10 points to player who has the most of them for each one") {
            error("TODO")
        }

        it("give 10 points to all player in case of ties") {
            error("TODO")
        }

        it("give no points when no player has some") {
            error("TODO")
        }

    }

    describe("builder") {

        it("gives another turn when player adds tile to castle with builder") {
            GameScenario(twoPlayers(TileC, TileD, TileD, TileD, TileD, TileD, TileD))
                .then(TileCmd(Position(0, 1), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Down)))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, 2), Rotation180))
                .then(PieceCmd(BuilderPiece, BuilderRole(Down)))
                .then(TileCmd(Position(2, 0), NoRotation))
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBe(TileEvent("TileD", 1))
                .then(TileCmd(Position(-1, 1), Rotation90))
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBe(TileEvent("TileD", 1))
        }

        it("bonus turn does not chain") {
            GameScenario(twoPlayers(TileC, TileD, TileD, TileD, TileQ, TileQ, TileD))
                .then(TileCmd(Position(0, 1), NoRotation))
                .then(PieceCmd(SmallPiece, Knight(Down)))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, 2), Rotation180))
                .then(PieceCmd(BuilderPiece, BuilderRole(Down)))
                .then(TileCmd(Position(2, 0), NoRotation))
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBe(TileEvent("TileQ", 1))
                .then(TileCmd(Position(-1, 1), NoRotation))
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBe(TileEvent("TileQ", 1))
                .then(TileCmd(Position(-2, 1), NoRotation))
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBe(TileEvent("TileD", 2))
        }

        it("builder can be used once") {

        }

        it("builder can be used when it is returned") {

        }

        it("can't be placed when player does not have a piece on castle") {
            error("TODO")
        }

        it("can't be placed when player does not have a piece on road") {
            error("TODO")
        }

        it("gives another turn when player adds tile to road with builder") {
            error("TODO")
        }

        it("builder goes back to player when castle is finished") {
            error("TODO")
        }

        it("builder goes back to player when road is finished") {
            error("TODO")
        }

        it("piece can be added on first and second tile") {
            error("TODO")
        }
    }

    describe("pig") {

        it("can't be added when there is no peasant on this greenfield yet") {
            GameScenario(singlePlayer(TileD, TileD, TileD))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(PigPiece, PigRole(Location(Down))))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }

        it("can be added when there is a peasant on this greenfield already") {
            GameScenario(singlePlayer(TileD, TileD, TileD))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Peasant(Location(Down))))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(PigPiece, PigRole(Location(Down))))
                .thenReceivedEventShouldBeOnlyPlaceTile()
        }

        it("can't be added when peasant is on another greenfield") {
            GameScenario(singlePlayer(TileD, TileD, TileD))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(SmallPiece, Peasant(Location(Left, RightSide))))
                .then(TileCmd(Position(1, 0), NoRotation))
                .then(PieceCmd(PigPiece, PigRole(Location(Down))))
                .thenReceivedEventShouldBe(InvalidPieceLocationEvent)
        }

        it("can't be used in other role") {
            setOf(
                Knight(Up),
                Brigand(Left),
                Peasant(Location(Down))
            ).forEach {
                GameScenario(singlePlayer(TileD, TileD, TileD))
                    .then(TileCmd(Position(1, 0), NoRotation))
                    .then(PieceCmd(PigPiece, it))
                    .thenReceivedEventShouldBe(InvalidPieceRoleEvent(PigPiece, it))
            }
        }

        it("increases score for field") {
            GameScenario(twoPlayers(TileD, TileI, TileJ, TileW, TileD, TileG, TileE, TileA))
                // red is first and TileD is already on table
                .then(TileCmd(Position(-1, 0), Rotation180))
                .then(PieceCmd(SmallPiece, Peasant(Location(Up))))
                .then(TileCmd(Position(-1, -1), Rotation90))
                .then(PieceCmd(SmallPiece, Peasant(Location(Down))))
                .then(TileCmd(Position(-2, 0), NoRotation))
                .then(PieceCmd(PigPiece, PigRole(Location(Left))))
                .then(TileCmd(Position(-2, -1), Rotation90))
                .then(PieceCmd(PigPiece, PigRole(Location(Right))))
                .then(TileCmd(Position(0, 1), Rotation180))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, -1), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, -1), Rotation270))
                .then(PieceCmd(SmallPiece, Peasant(Location(Right))))
                .then(TileCmd(Position(1, 0), Rotation90))
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBe(ScoreEvent(1, 8, emptyList()))
                .thenReceivedEventShouldBe(NoScoreEvent(2, emptyList()))
        }

        it("other players pig does not increase your score + is worth 0 points when calculating advantage") {
            GameScenario(twoPlayers(TileD, TileI, TileJ, TileW, TileD, TileG, TileE, TileA))
                // red is first and TileD is already on table
                .then(TileCmd(Position(-1, 0), Rotation180))
                .then(PieceCmd(SmallPiece, Peasant(Location(Up))))
                .then(TileCmd(Position(-1, -1), Rotation90))
                .then(PieceCmd(SmallPiece, Peasant(Location(Down))))
                .then(TileCmd(Position(-2, 0), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(-2, -1), Rotation90))
                .then(PieceCmd(PigPiece, PigRole(Location(Right))))
                .then(TileCmd(Position(0, 1), Rotation180))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(0, -1), NoRotation))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, -1), Rotation270))
                .then(SkipPieceCmd)
                .then(TileCmd(Position(1, 0), Rotation90))
                .then(SkipPieceCmd)
                .thenReceivedEventShouldBe(ScoreEvent(1, 6, emptyList()))
                .thenReceivedEventShouldBe(ScoreEvent(2, 8, emptyList()))
        }

    }

})
