package io.github.hejcz.api

/**
 * Rules rewarding players with points when the game ends.
 */
interface EndGameScoring {
    fun apply(state: State): Collection<GameEvent>
}