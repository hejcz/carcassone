package io.github.hejcz.core

interface CommandValidator {

    fun validate(state: State, command: Command): Collection<GameEvent>

}
