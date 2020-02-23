package io.github.hejcz.api

data class GameChanges(val state: State, val events: Collection<GameEvent>) {
    companion object {
        fun withState(state: State) = GameChanges(state, emptySet())
    }
}