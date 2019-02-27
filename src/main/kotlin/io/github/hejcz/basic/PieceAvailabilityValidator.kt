package io.github.hejcz.basic

import io.github.hejcz.core.*

object PieceAvailabilityValidator : CommandValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> {
        return when (command) {
            is PutPiece -> if (state.currentPlayer.isAvailable(command.piece)) emptySet() else setOf(NoMappleAvailable(command.piece))
            else -> emptySet()
        }
    }
}
