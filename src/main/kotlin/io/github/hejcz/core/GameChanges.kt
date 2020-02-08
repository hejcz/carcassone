package io.github.hejcz.core

data class GameChanges(val state: State, val events: Collection<GameEvent>) {

    companion object {
        fun noEvents(state: State) = GameChanges(state, emptySet())
    }

}
