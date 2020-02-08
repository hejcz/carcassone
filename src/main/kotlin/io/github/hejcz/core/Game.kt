package io.github.hejcz.core

import io.github.hejcz.core.tile.*
import io.github.hejcz.setup.*

class Game(players: Collection<Player>, gameSetup: GameSetup, private val verbose: Boolean = false) {

    private val eventsQueue = EventsQueue()
    private val validators: Collection<CommandValidator> = gameSetup.validators()
    private val rules: Collection<Rule> = gameSetup.rules()
    private val endRules: Collection<EndRule> = gameSetup.endRules()
    private val handlers: Collection<CommandHandler> = gameSetup.handlers()

    private var state: State

    init {
        val tiles = gameSetup.tiles()
        state = BasicState(players.mapTo(mutableSetOf()) { playerWithPieces(it, gameSetup) }, tiles)
    }

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
                val handler = handlers.firstOrNull { it.isApplicableTo(command) }
                    ?: throw NoHandlerForCommand(command)

                val (state1, events1) = handler.beforeScoring(state, command)
                val scoreEvents = rules.flatMap { it.afterCommand(command, state1) }
                val (state2, events2) = handler.afterScoring(state1, scoreEvents)

                val endGameEvents: Collection<GameEvent> = when {
                    isEndOfTheGame() -> endRules.flatMap { it.apply(state2) }
                    else -> emptySet()
                }

                val expectationsEvents: Collection<GameEvent> = when {
                    isEndOfTheGame() -> emptySet()
                    else -> setOf(eventsQueue.event(state2))
                }

                state = when {
                    eventsQueue.isPutTileNext() && command != Begin -> state2.changeActivePlayer()
                    else -> state2
                }

                events1 + events2 + scoreEvents + endGameEvents + expectationsEvents
            }
        }
        if (verbose) {
            println("-- Events --")
            events.forEach { println(it) }
        }
        return events
    }

    private fun isEndOfTheGame() = eventsQueue.isPutTileNext() && state.currentTile() is NoTile

    private fun validate(command: Command) =
        validators.asSequence().map { it.validate(state, command) }.firstOrNull { it.isNotEmpty() }?.toSet()
            ?: emptySet()
}
