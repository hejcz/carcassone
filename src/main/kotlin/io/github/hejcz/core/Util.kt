package io.github.hejcz.core

inline fun <reified T> commandValidator(crossinline handler: (state: State, command: T) -> Collection<GameEvent>) =
    object : CommandValidator {
        override fun validate(state: State, command: Command): Collection<GameEvent> =
            (command as? T)?.let { handler(state, command) } ?: emptySet()
    }

inline fun <reified T : Command> eventsHandler(): CommandHandler {
    return object : CommandHandler {
        override fun isApplicableTo(command: Command): Boolean = command is T
        override fun beforeScoring(state: State, command: Command): GameChanges = GameChanges.noEvents(state)
    }
}

inline fun <reified T : Command> eventsHandler(crossinline body: (state: State, command: T) -> GameChanges): CommandHandler {
    return object : CommandHandler {
        override fun isApplicableTo(command: Command): Boolean = command is T
        override fun beforeScoring(state: State, command: Command): GameChanges = body(state, command as T)
    }
}
