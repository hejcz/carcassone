package io.github.hejcz.core

import io.github.hejcz.core.tile.*
import io.github.hejcz.setup.*

class Game(players: Collection<Player>, gameSetup: GameSetup, private val verbose: Boolean = false) {

    private val eventsQueue = EventsQueue()
    val state: State
    private val rules: Collection<Rule>
    private val endRules: Collection<EndRule>

    private val validators: Collection<CommandValidator>
    private val handlers: Collection<CommandHandler>

    init {
        val tiles = gameSetup.tiles()
        val board = Board(mapOf(Position(0, 0) to tiles.next()))
        state = State(players.mapTo(mutableSetOf()) { playerWithPieces(it, gameSetup) }, tiles, board)
        validators = gameSetup.validators()
        rules = gameSetup.rules()
        endRules = gameSetup.endRules()
        handlers = gameSetup.handlers()
    }

    fun runAllRules(command: Command) = rules.flatMap { it.afterCommand(command, state) }

    private fun playerWithPieces(player: Player, gameSetup: GameSetup) = when {
        player.initialPieces.isNotEmpty() -> player
        else -> Player(player.id, player.order, gameSetup.pieces())
    }

    fun dispatch(command: Command): Collection<GameEvent> {
        if (verbose) {
            println("-- Command --")
            println(command)
        }
        val errors = validate(command) + eventsQueue.validate(state, command)
        val events = when {
            errors.isNotEmpty() -> errors
            else -> {
                val events = handlers.first { it.isApplicableTo(command) }.handle(this, command) +
                        if (isEndOfTheGame()) endRules.flatMap { it.apply(state) } else setOf(eventsQueue.event(state))
                if (eventsQueue.isPutTileNext() && command != Begin) {
                    state.changeActivePlayer()
                }
                events
            }
        }
        if (verbose) {
            println("-- Events --")
            events.forEach { println(it) }
        }
        return events
    }

    private fun isEndOfTheGame() = eventsQueue.isPutTileNext() && state.currentTile is NoTile

    private fun validate(command: Command) =
        validators.asSequence().map { it.validate(state, command) }.firstOrNull { it.isNotEmpty() }?.toSet()
            ?: emptySet()
}
