package io.github.hejcz.core.validator

import io.github.hejcz.core.*

object PiecePlacementValidator : CommandValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> {
        return when (command) {
            is PutPiece -> {
                val role = command.role
                val tile = state.recentTile
                when {
                    !command.piece.mayBeUsedAs(command.role) -> setOf(PieceMayNotBeUsedInARole(command.piece, command.role))
                    role.canBePlacedOn(tile) -> emptySet()
                    else -> setOf(PiecePlacedInInvalidPlace)
                }
            }
            else -> emptySet()
        }
    }
}
