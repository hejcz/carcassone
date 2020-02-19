package io.github.hejcz.core

interface CommandHandler {
    fun isApplicableTo(command: Command): Boolean
    fun apply(state: State, command: Command): State
}

interface CommandValidator {
    fun validate(state: State, command: Command): Collection<GameEvent>
}

interface EndRule {
    fun apply(state: State): Collection<GameEvent>
}

interface Rule {
    fun afterCommand(command: Command, state: State): Collection<GameEvent>
}
