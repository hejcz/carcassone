package io.github.hejcz.core

interface EndRule {
    fun apply(state: State): Collection<GameEvent>
}
