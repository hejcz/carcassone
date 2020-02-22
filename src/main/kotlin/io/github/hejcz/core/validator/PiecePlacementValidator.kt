package io.github.hejcz.core.validator

import io.github.hejcz.core.*

object PiecePlacementValidator : CmdValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> {
        return when (command) {
            is PieceCmd -> {
                val role = command.role
                val tile = state.recentTile()
                when {
                    !command.piece.mayBeUsedAs(command.role) -> setOf(
                        InvalidPieceRoleEvent(command.piece, command.role)
                    )
                    role.canBePlacedOn(tile) -> emptySet()
                    else -> setOf(InvalidPieceLocationEvent)
                }
            }
            else -> emptySet()
        }
    }
}
