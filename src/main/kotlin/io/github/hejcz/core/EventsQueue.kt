package io.github.hejcz.core

class EventsQueue private constructor(private val expectations: List<Expectation>) {

    constructor() : this(listOf(BeginExpectation()))

    fun validate(command: Command): Collection<GameEvent> {
        return when {
            currentExpectation().expects(command) -> emptySet()
            else -> setOf(UnexpectedCommand)
        }
    }

    fun next(state: State, command: Command) =
        EventsQueue(
            expectations.drop(1) + currentExpectation().next(command, state, expectations.size == 1)
        )

    fun event(state: State) = currentExpectation().toEvent(state)

    fun isPutTileNext(): Boolean = currentExpectation() is PutTileExpectation

    fun shouldRunRules(state: State): Boolean = currentExpectation().shouldRunRules(state)

    private fun currentExpectation() = expectations.first()
}