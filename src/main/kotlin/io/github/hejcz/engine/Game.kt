package io.github.hejcz.engine

import io.github.hejcz.core.*
import io.github.hejcz.setup.GameSetup

class Game private constructor(
    private val flowController: GameFlowController,
    private val validators: Collection<CmdValidator>,
    private val scorings: Collection<Scoring>,
    private val endGameScorings: Collection<EndGameScoring>,
    private val handlers: Collection<CmdHandler>,
    private val events: Collection<GameEvent>,
    private val state: State,
    private val verbose: Boolean
) {

    constructor(players: Collection<Player>, gameSetup: GameSetup, verbose: Boolean = false) : this(
        GameFlowController(
            FlowState(
                idOfPlayerMakingMove = players.toList()[0].id, gameStarted = false
            )
        ),
        gameSetup.validators(),
        gameSetup.rules(),
        gameSetup.endRules(),
        gameSetup.handlers(),
        emptySet(),
        InitialState(
            players.map {
                when {
                    it.initialPieces.isNotEmpty() -> it
                    else -> Player(it.id, it.order, gameSetup.pieces())
                }
            },
            gameSetup.tiles(),
            gameSetup.stateExtensions()
        ),
        verbose
    )

    private fun copy(flowController: GameFlowController, newEvents: Collection<GameEvent>, newState: State) =
        Game(
            flowController, validators, scorings, endGameScorings, handlers, newEvents, newState, verbose
        )

    fun dispatch(command: Command): Game {
        printIfVerbose(command)

        // validation

        val state = state.setCurrentPlayer(flowController.currentPlayer())

        val validateEvents = validators.asSequence()
            .map { it.validate(state, command) }
            .firstOrNull { it.isNotEmpty() }
            ?.toSet()
            ?: emptySet()

        val errors = validateEvents + when {
            !flowController.isOk(command, state) -> setOf(
                UnexpectedCommandEvent
            )
            else -> emptySet()
        }

        if (errors.isNotEmpty()) {
            return copy(flowController, errors, state)
        }

        // apply command to state

        val handler = handlers.firstOrNull { it.isApplicableTo(command) }
            ?: throw NoHandlerForCommand(command)

        var newState = handler.apply(state, command)

        var newFlowController = flowController.dispatch(command, state)

        var events = newFlowController.events(newState)

        val dispatchEvents = dispatchEvents(command, newState, events)

        newState = dispatchEvents.state
        events = dispatchEvents.events

        // dispatch everything what does not require user action

        while (newFlowController.isOk(SystemCmd, newState)) {
            newFlowController = newFlowController.dispatch(SystemCmd, state)
            val de = dispatchEvents(command, newState, newFlowController.events(newState))
            newState = de.state
            events += de.events
        }

        printIfVerbose(events)

        return copy(newFlowController, events, newState)
    }

    private fun dispatchEvents(command: Command, state: State, events: Collection<GameEvent>): GameChanges {
        var changingState = state
        var changingEvents = events
        var processedEvents: Collection<GameEvent> = emptySet()

        while (true) {
            val systemEvents = changingEvents.filter { it is SystemGameEvent || it is MixedGameEvent }
            val userEvents = changingEvents.filter { it !is SystemGameEvent }

            if (systemEvents.isEmpty()) {
                return GameChanges(changingState, userEvents + processedEvents)
            }

            val changes = systemEvents.fold(
                GameChanges(changingState, emptySet())
            ) { acc, event ->
                when (event) {
                    is ScorePointsEvent -> GameChanges(
                        acc.state, acc.events + scorings.flatMap { it.apply(command, acc.state) })
                    is EndGameEvent -> GameChanges(
                        acc.state, acc.events + endGameScorings.flatMap { it.apply(acc.state) })
                    is ChangePlayerEvent -> GameChanges(acc.state.changeActivePlayer(), acc.events)
                    is ScoreEvent -> GameChanges(
                        acc.state.returnPieces(
                            event.returnedPieces.map {
                                OwnedPiece(
                                    event.playerId, it
                                )
                            }),
                        acc.events
                    )
                    is NoScoreEvent -> GameChanges(
                        acc.state.returnPieces(
                            event.returnedPieces.map {
                                OwnedPiece(
                                    event.playerId, it
                                )
                            }),
                        acc.events
                    )
                    else -> throw RuntimeException("Unknown system event: $event")
                }
            }

            changingState = changes.state
            processedEvents += userEvents
            changingEvents = changes.events
        }
    }

    private fun printIfVerbose(command: Command) {
        if (verbose) {
            println("-- Command --")
            println(command)
        }
    }

    private fun printIfVerbose(events: Collection<GameEvent>) {
        if (verbose) {
            println("-- Events --")
            events.forEach { println(it) }
        }
    }

    fun recentEvents() = events
}
