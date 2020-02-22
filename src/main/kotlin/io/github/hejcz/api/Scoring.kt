package io.github.hejcz.api

/**
 * Rules rewarding players with points during normals coring phase.
 */
interface Scoring {
    fun apply(command: Command, state: State): Collection<GameEvent>
}