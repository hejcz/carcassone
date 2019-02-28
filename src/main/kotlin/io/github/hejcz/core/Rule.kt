package io.github.hejcz.core

interface Rule {
    fun afterCommand(command: Command, state: State): Collection<GameEvent>
}
