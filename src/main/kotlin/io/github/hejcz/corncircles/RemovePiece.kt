package io.github.hejcz.corncircles

import io.github.hejcz.core.*
import io.github.hejcz.util.*

data class RemovePieceCommand(val position: Position, val piece: Piece, val role: Role): Command

val RemovePieceHandler = eventHandler<RemovePieceCommand> { game, command ->
    game.state.addPiece(command.position, command.piece, command.role)
    game.state.changeActivePlayer()
    emptySet()
}

val RemovePieceValidator = commandValidator<RemovePieceCommand> { state, command ->
    when (val tile = state.recentTile()) {
        is CornCircleTile -> when {
            !tile.cornCircleEffect().matches(command.role) -> setOf(InvalidPieceLocation)
            playerDoesNotHaveSuchPieceThere(state, command) -> setOf(InvalidPieceLocation)
            else -> emptySet()
        }
        else -> emptySet()
    }
}

private fun playerDoesNotHaveSuchPieceThere(state: State, command: RemovePieceCommand) =
    state.findPieces(command.position, command.role)
        .none { state.currentPlayerId() == it.first && it.second.piece == command.piece }