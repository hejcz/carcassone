package io.github.hejcz.engine

import io.github.hejcz.*
import io.github.hejcz.rules.EndRule
import io.github.hejcz.rules.Rule
import io.github.hejcz.tiles.basic.NoTile

private typealias Events = Collection<GameEvent>

class Game(
    private val rules: Collection<Rule>,
    private val endRules: Collection<EndRule>,
    players: Collection<Player>,
    board: Board,
    remainingTiles: RemainingTiles
) {
    private var state = State(board, players, remainingTiles)

    private val validators: Collection<CommandValidator> =
        setOf(PutTileValidator, PiecePlacementValidator, SinglePieceInObjectValidator, MappleAvailabilityValidator)

    private val listeners: Collection<EventListener> = setOf(ReturnPieceListener)

    fun dispatch(command: Command): Events {
        val events = when (command) {
            is PutTile ->
                validationEventsOr(command) {
                    state.addTile(command.position, command.rotation)
                    rules.flatMap { it.afterTilePlaced(command.position, state) } + setOf(SelectPiece)
                }
            is PutPiece -> {
                validationEventsOr(command) {
                    state.addPiece(command.pieceId, command.pieceRole)
                    val events = rules.flatMap { it.afterPiecePlaced(state, command.pieceId, command.pieceRole) }
                    endTurn(events)
                }
            }
            is SkipPiece -> endTurn()
            is Begin -> setOf(PlaceTile(state.currentTile.name(), state.currentPlayerId()))
        }
        listeners.forEach { listener -> events.forEach { event -> listener.handle(state, event) } }
        return events
    }

    private fun <T : Command> validationEventsOr(command: T, action: () -> Events): Events =
        when (val validationEvents = validate(command)) {
            emptySet<GameEvent>() -> action()
            else -> validationEvents
        }

    private fun validate(command: Command) =
        validators.asSequence().map { it.validate(state, command) }.firstOrNull { it.isNotEmpty() }?.toSet() ?: emptySet()

    private fun endTurn(events: Events = emptySet()): Events {
        state.endTurn()
        when (state.currentTile) {
            is NoTile -> return endRules.flatMap { it.apply(state) } + events
            else -> return setOf(PlaceTile(state.currentTile.name(), state.currentPlayerId()))
        }
    }
}
