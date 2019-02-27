package io.github.hejcz.engine

import io.github.hejcz.Command
import io.github.hejcz.GameEvent
import io.github.hejcz.NoMappleAvailable
import io.github.hejcz.PutPiece
import io.github.hejcz.mapples.Mapple

object MappleAvailabilityValidator : CommandValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> {
        return when (command) {
            is PutPiece -> if (state.currentPlayer.isMappleAvailable()) emptySet() else setOf(NoMappleAvailable(Mapple))
            else -> emptySet()
        }
    }
}