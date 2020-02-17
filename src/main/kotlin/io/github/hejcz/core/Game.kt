package io.github.hejcz.core

import io.github.hejcz.core.tile.NoTile
import io.github.hejcz.core.setup.GameSetup

class Game private constructor(
    private val eventsQueue: EventsQueue,
    private val validators: Collection<CommandValidator>,
    private val rules: Collection<Rule>,
    private val endRules: Collection<EndRule>,
    private val handlers: Collection<CommandHandler>,
    private val events: Collection<GameEvent>,
    private val state: State,
    private val verbose: Boolean
) {

    constructor(players: Collection<Player>, gameSetup: GameSetup, verbose: Boolean = false) : this(
        EventsQueue(),
        gameSetup.validators(),
        gameSetup.rules(),
        gameSetup.endRules(),
        gameSetup.handlers(),
        emptySet(),
        BasicState(
            players.map {
                when {
                    it.initialPieces.isNotEmpty() -> it
                    else -> Player(it.id, it.order, gameSetup.pieces())
                }
            },
            gameSetup.tiles()
        ),
        verbose
    )

    private fun copy(newEventsQueue: EventsQueue = eventsQueue, newEvents: Collection<GameEvent> = events, newState: State = state) =
        Game(newEventsQueue, validators, rules, endRules, handlers, newEvents, newState, verbose)

    fun dispatch(command: Command): Game {
        if (verbose) {
            println("-- Command --")
            println(command)
        }

        val errors = validate(command) + eventsQueue.validate(command)

        if (errors.isNotEmpty()) {
            printEventsIfVerbose(errors)
            return copy(newEvents = errors)
        }

        val handler = handlers.firstOrNull { it.isApplicableTo(command) }
            ?: throw NoHandlerForCommand(command)

        val (state1, events1) = handler.beforeScoring(state, command)

        val scoreEvents = when {
            eventsQueue.shouldRunRules(state1) -> rules.flatMap { it.afterCommand(command, state1) }
            else -> emptyList()
        }

        val (state2, events2) = handler.afterScoring(state1, scoreEvents)

        val newEventsQueue = eventsQueue.next(state2, command)

        val endGameEvents: Collection<GameEvent> = when {
            isEndOfTheGame(newEventsQueue) -> endRules.flatMap { it.apply(state2) }
            else -> emptySet()
        }

        val expectationsEvents: Collection<GameEvent> = when {
            isEndOfTheGame(newEventsQueue) -> emptySet()
            else -> setOf(newEventsQueue.event(state2))
        }

        val newState = when {
            newEventsQueue.isPutTileNext() && command != BeginCmd -> state2.changeActivePlayer()
            else -> state2
        }

        val newEvents = events1 + events2 + scoreEvents + endGameEvents + expectationsEvents

        printEventsIfVerbose(newEvents)

        return copy(newEventsQueue, newEvents, newState)
    }

    private fun printEventsIfVerbose(events: Collection<GameEvent>) {
        if (verbose) {
            println("-- Events --")
            events.forEach { println(it) }
        }
    }

    fun recentEvents() = events

    private fun isEndOfTheGame(eq: EventsQueue) = eq.isPutTileNext() && state.currentTile() is NoTile

    private fun validate(command: Command) =
        validators.asSequence().map { it.validate(state, command) }.firstOrNull { it.isNotEmpty() }?.toSet()
            ?: emptySet()
}
