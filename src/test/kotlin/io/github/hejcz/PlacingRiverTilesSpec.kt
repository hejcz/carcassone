package io.github.hejcz

import io.github.hejcz.engine.DefaultDeck
import io.github.hejcz.engine.Game
import io.github.hejcz.engine.River
import io.github.hejcz.mapples.*
import io.github.hejcz.placement.*
import io.github.hejcz.rules.basic.CastleCompletedRule
import io.github.hejcz.tiles.basic.TileD
import io.github.hejcz.tiles.basic.TileI
import io.github.hejcz.tiles.river.*
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object PlacingRiverTilesSpec : Spek({
    Feature("Castle completed rule") {

        Scenario("Placing river tile") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                RiverTestGameSetup(TestRiverRemainingTiles(TileBB6F6))
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, -1), NoRotation)).shouldContainSelectPieceOnly()
            }
        }

        Scenario("Placing river tile in invalid place") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                RiverTestGameSetup(TestRiverRemainingTiles(TileD))
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(1, 0), NoRotation)) shouldContain TilePlacedInInvalidPlace
                game.dispatch(PutTile(Position(-1, 0), Rotation90)) shouldContain TilePlacedInInvalidPlace
                game.dispatch(PutTile(Position(0, 1), Rotation180)) shouldContain TilePlacedInInvalidPlace
            }
        }

        Scenario("Connecting river tiles 1") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                RiverTestGameSetup(TestRiverRemainingTiles(TileBB6F2))
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, -1), Rotation90)).shouldContainSelectPieceOnly()
            }
        }

        Scenario("Connecting river tiles 2") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                RiverTestGameSetup(TestRiverRemainingTiles(TileBB6F2))
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, -1), Rotation270)).shouldContainSelectPieceOnly()
            }
        }

        Scenario("Connecting river tiles invalid") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                RiverTestGameSetup(TestRiverRemainingTiles(TileBB6F2))
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, -1), NoRotation)) shouldContain TilePlacedInInvalidPlace
                game.dispatch(PutTile(Position(0, -1), Rotation180)) shouldContain TilePlacedInInvalidPlace
            }
        }

        Scenario("Connecting tiles with bounded object") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                TestGameSetup(TestBasicRemainingTiles(TileI, TileI))
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, -1), Rotation180))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(0, -2), Rotation180)) shouldContain TilePlacedInInvalidPlace
            }
        }

        Scenario("Two consecutive river tiles can't turn in same direction") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                RiverTestGameSetup(TestRiverRemainingTiles(TileBB6F9, TileBB6F10))
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, -1), Rotation180))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(-1, -1), Rotation270)) shouldContain TilePlacedInInvalidPlace
            }
        }

        Scenario("Two consecutive river tiles can turn in same direction if are not consecutive") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                RiverTestGameSetup(TestRiverRemainingTiles(TileBB6F9, TileBB6F6, TileBB6F10))
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, -1), Rotation180))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(-1, -1), Rotation90))
                game.dispatch(SkipPiece)
                game.dispatch(PutTile(Position(-2, -1), Rotation270)).shouldContainSelectPieceOnly()
            }
        }

        Scenario("River must be connected with previous river - it can't be adjacent by field e.g.") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                RiverTestGameSetup(TestRiverRemainingTiles(TileBB6F7))
            )

            Then("on next tile it should detect completed castle") {
                game.dispatch(PutTile(Position(0, 1), NoRotation)) shouldContain TilePlacedInInvalidPlace
                game.dispatch(PutTile(Position(-1, 0), NoRotation)) shouldContain TilePlacedInInvalidPlace
                game.dispatch(PutTile(Position(1, 0), Rotation90)) shouldContain TilePlacedInInvalidPlace
            }
        }

        Scenario("Can place knight in castle on river tile") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                RiverTestGameSetup(TestRiverRemainingTiles(TileBB6F2))
            )

            Then("knight can be placed") {
                game.dispatch(PutTile(Position(0, -1), Rotation90))
                game.dispatch(PutPiece(Mapple, Knight(Right))) shouldEqual emptyList()
            }
        }

        Scenario("Can place brigand on a road on river tile") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                RiverTestGameSetup(TestRiverRemainingTiles(TileBB6F2))
            )

            Then("brigand can be placed") {
                game.dispatch(PutTile(Position(0, -1), Rotation90))
                game.dispatch(PutPiece(Mapple, Brigand(Left))) shouldEqual emptyList()
            }
        }

        Scenario("Can place peasant on a field on river tile") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                RiverTestGameSetup(TestRiverRemainingTiles(TileBB6F2))
            )

            Then("peasant can be placed") {
                game.dispatch(PutTile(Position(0, -1), Rotation90))
                game.dispatch(PutPiece(Mapple, Peasant(Location(Left, RightSide)))) shouldEqual emptyList()
            }
        }

        Scenario("Can place monk on a field on river tile") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                RiverTestGameSetup(TestRiverRemainingTiles(TileBB6F8))
            )

            Then("knight can be placed") {
                game.dispatch(PutTile(Position(0, -1), Rotation90))
                game.dispatch(PutPiece(Mapple, Monk)) shouldEqual emptyList()
            }
        }

        Scenario("Can't place a knight on river") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                RiverTestGameSetup(TestRiverRemainingTiles(TileBB6F2))
            )

            Then("knight can be placed") {
                game.dispatch(PutTile(Position(0, -1), Rotation90))
                game.dispatch(PutPiece(Mapple, Knight(Down))) shouldContain PiecePlacedInInvalidPlace
            }
        }

        Scenario("Can't place a brigand on river") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                RiverTestGameSetup(TestRiverRemainingTiles(TileBB6F2))
            )

            Then("knight can be placed") {
                game.dispatch(PutTile(Position(0, -1), Rotation90))
                game.dispatch(PutPiece(Mapple, Brigand(Down))) shouldContain PiecePlacedInInvalidPlace
            }
        }

        Scenario("Can't place a peasant on river") {
            val game = Game(
                setOf(CastleCompletedRule),
                emptySet(),
                Players.singlePlayer(),
                RiverTestGameSetup(TestRiverRemainingTiles(TileBB6F2))
            )

            Then("knight can be placed") {
                game.dispatch(PutTile(Position(0, -1), Rotation90))
                game.dispatch(PutPiece(Mapple, Peasant(Location(Down)))) shouldContain PiecePlacedInInvalidPlace
            }
        }

        Scenario("If river enabled source should be first") {
            val deck = DefaultDeck().withExtensions(River)

            Then("source should be first") {
                deck.remainingTiles().next() shouldEqual TileBB6F1
            }
        }

        Scenario("Amount of tiles in basic deck") {
            val deck = DefaultDeck()

            Then("deck should have 72 tiles") {
                deck.remainingTiles().size() shouldEqual 72
            }
        }

        Scenario("Amount of tiles in game with river extension") {
            val deck = DefaultDeck().withExtensions(River)

            Then("deck should have 84 tiles") {
                deck.remainingTiles().size() shouldEqual 84
            }
        }

        Scenario("If river enabled estuary should be last of river tiles") {
            val deck = DefaultDeck().withExtensions(River)

            Then("source should be first") {
                val remainingTiles = deck.remainingTiles()
                (1..11).forEach { _ -> remainingTiles.next() }
                remainingTiles.next() shouldEqual TileBB6F12
            }
        }

    }
})
