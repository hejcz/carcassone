package io.github.hejcz.corncircles

import io.github.hejcz.core.*

// TODO order of command is not enforced anywhere so 2 consecutive PutTile will work

// TODO this code is almost copy of each other
object AddPieceValidator : CommandValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> =
        (command as? AddPiece)?.let { validate(state, it) } ?: emptySet()

    private fun validate(state: State, command: AddPiece): Collection<GameEvent> = when (val tile = state.recentTile) {
        is CornCircleTile -> when {
            !tile.cornCircleEffect().matches(command.role) -> setOf(PiecePlacedInInvalidPlace)
            else -> state.findPiece(command.position, command.role)
                ?.let { if (state.currentPlayer.id == it.first) emptySet() else setOf(PiecePlacedInInvalidPlace) }
                ?: setOf(PiecePlacedInInvalidPlace)
        }
        else -> emptySet()
    }
}

object RemovePieceValidator : CommandValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> =
        (command as? RemovePiece)?.let { validate(state, it) } ?: emptySet()

    private fun validate(state: State, command: RemovePiece): Collection<GameEvent> = when (val tile = state.recentTile) {
        is CornCircleTile -> when {
            !tile.cornCircleEffect().matches(command.role) -> setOf(PiecePlacedInInvalidPlace)
            // TODO wont work because findPiece return first piece and there might be more than 1 so piece equality not always works
            else -> state.findPiece(command.position, command.role)
                ?.let { if (state.currentPlayer.id == it.first && it.second.piece == command.piece) emptySet() else setOf(PiecePlacedInInvalidPlace) }
                ?: setOf(PiecePlacedInInvalidPlace)
        }
        else -> emptySet()
    }
}