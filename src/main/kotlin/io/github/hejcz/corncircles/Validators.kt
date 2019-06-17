package io.github.hejcz.corncircles

import io.github.hejcz.core.*

object AddPieceValidator : CommandValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> =
        (command as? AddPiece)?.let { validate(state, it) } ?: emptySet()

    private fun validate(state: State, command: AddPiece): Collection<GameEvent> = when (val tile = state.recentTile) {
        is CornCircleTile -> when {
            !tile.cornCircleEffect().matches(command.role) -> setOf(PiecePlacedInInvalidPlace)
            playerDoesNotHaveAnyPieceThere(state, command) -> setOf(PiecePlacedInInvalidPlace)
            else -> emptySet()
        }
        else -> emptySet()
    }

    private fun playerDoesNotHaveAnyPieceThere(state: State, command: AddPiece) =
        state.findPieces(command.position, command.role)
            .none { state.currentPlayerId() == it.first }
}

object RemovePieceValidator : CommandValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> =
        (command as? RemovePiece)?.let { validate(state, it) } ?: emptySet()

    private fun validate(state: State, command: RemovePiece): Collection<GameEvent> = when (val tile = state.recentTile) {
        is CornCircleTile -> when {
            !tile.cornCircleEffect().matches(command.role) -> setOf(PiecePlacedInInvalidPlace)
            playerDoesNotHaveSuchPieceThere(state, command) -> setOf(PiecePlacedInInvalidPlace)
            else -> emptySet()
        }
        else -> emptySet()
    }

    private fun playerDoesNotHaveSuchPieceThere(state: State, command: RemovePiece) =
        state.findPieces(command.position, command.role)
            .none { state.currentPlayerId() == it.first && it.second.piece == command.piece }
}