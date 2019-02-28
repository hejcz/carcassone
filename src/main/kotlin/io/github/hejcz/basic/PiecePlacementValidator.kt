package io.github.hejcz.basic

import io.github.hejcz.core.*

object PiecePlacementValidator : CommandValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> {
        return when (command) {
            is PutPiece -> {
                val role = command.pieceRole
                val tile = state.recentTile
                when {
                    !command.piece.mayBeUsedAs(command.pieceRole) -> setOf(PieceMayNotBeUsedInARole(command.piece, command.pieceRole))
                    role.canBePlacedOn(tile) -> emptySet()
                    else -> setOf(PiecePlacedInInvalidPlace)
                }
            }
            else -> emptySet()
        }
    }
}
