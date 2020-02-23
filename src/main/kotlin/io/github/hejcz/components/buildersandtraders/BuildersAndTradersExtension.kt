package io.github.hejcz.components.buildersandtraders

import io.github.hejcz.api.*
import io.github.hejcz.base.*
import io.github.hejcz.setup.Extension
import io.github.hejcz.setup.PiecesSetup
import io.github.hejcz.setup.StateExtensionSetup
import io.github.hejcz.setup.ValidatorsSetup

interface BuildersAndTradersTile

object BuildersAndTradersExtension : Extension {

    // TODO
    // new tiles
    // goods tokens
    // builder
    // pig

    val ID = StateExtensionId("BUILDERS_AND_TRADERS")

    override fun modify(setup: ValidatorsSetup) {
        setup.add(PigPlacementValidator)
        setup.add(BuilderPlacementValidator)
    }

    override fun modify(setup: PiecesSetup) {
        setup.add(PigPiece)
        setup.add(BuilderPiece)
    }

    override fun modify(setup: StateExtensionSetup) {
        setup.add(BuildersAndTradersStateExtension())
    }

    class BuilderRole(val direction: Direction) : Role {
        override fun canBePlacedOn(tile: Tile): Boolean =
            direction in tile.exploreCastle(direction) || direction in tile.exploreRoad(direction)
    }

    object BuilderPiece : Piece {
        override fun mayBeUsedAs(role: Role): Boolean = role is BuilderRole
        override fun power(): Int = 0
    }

    class PigRole(val location: Location) : Role {
        override fun canBePlacedOn(tile: Tile): Boolean =
            location in tile.exploreGreenFields(location)
    }

    object PigPiece : Piece {
        override fun mayBeUsedAs(role: Role): Boolean = role is PigRole
        override fun power(): Int = 0
    }

    private object PigPlacementValidator : CmdValidator {
        override fun validate(state: State, command: Command): Collection<GameEvent> = when {
            command is PieceCmd && command.piece is PigPiece && command.role is PigRole -> {
                val field: Set<Pair<Position, Location>> = GreenFieldsExplorer.explore(state, state.recentPosition(), command.role.location)
                val thereIsOtherPiece = state.allOf(Peasant::class, state.currentPlayerId())
                    .any { field.contains(it.pieceOnBoard.position to (it.pieceOnBoard.role as Peasant).location) }
                if (thereIsOtherPiece) {
                    emptySet<GameEvent>()
                } else {
                    setOf(InvalidPieceLocationEvent)
                }
            }
            else ->
                emptySet()
        }
    }

    private object BuilderPlacementValidator : CmdValidator {
        override fun validate(state: State, command: Command): Collection<GameEvent> {
            return emptySet()
        }
    }

    private data class BuildersAndTradersStateExtension(
        val pigs: Map<Long, PositionedLocation> = emptyMap(),
        val builders: Map<Long, PositionedDirection> = emptyMap(),
        val tokens: Map<Long, GoodsTokens> = emptyMap(),
        val shouldHaveAnotherTurn: Boolean = false
    ) : StateExtension, BuildersAndTradersStateExtensionApi {
        override fun id(): StateExtensionId = ID

        override fun maybeRemoveBuilderOrPig(state: State, event: PieceRemoved): GameChanges {
            println(event)
            return GameChanges.withState(state)
        }

        override fun extendedBuilder(state: State): Boolean {
            val id = state.currentPlayerId()
            val builder = state.allOf(BuilderRole::class, id)
            if (builder.isEmpty()) {
                return false
            }
            val (_, pieceOnBoard) = builder.first()
            val position = pieceOnBoard.position
            val direction = (pieceOnBoard.role as BuilderRole).direction
            val tile = state.tileAt(position)
            val isOnCastle = direction in tile.exploreCastle(direction)
            val explorer = if (isOnCastle) CastlesExplorer else RoadsExplorer
            return explorer.explore(state, position, direction).first.any { it.position == state.recentPosition() }
        }

        companion object {
            private data class PositionedLocation(val position: Position, val location: Location)

            private data class GoodsTokens(val fabric: Int, val corn: Int, val barrel: Int)
        }
    }
}
