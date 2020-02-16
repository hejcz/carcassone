package io.github.hejcz.corncircles

import io.github.hejcz.core.*
import io.github.hejcz.util.*

data class RemovePieceCmd(val position: Position, val piece: Piece, val role: Role) : Command

val RemovePieceHandler = object : CommandHandler {
    override fun isApplicableTo(command: Command): Boolean = command is RemovePieceCmd

    override fun beforeScoring(state: State, command: Command): GameChanges =
        (command as RemovePieceCmd).let {
            GameChanges.noEvents(
                state.removePiece(command.position, command.piece, command.role)
                    .changeActivePlayer()
            )
        }
}

val RemovePieceValidator = commandValidator<RemovePieceCmd> { state, command ->
    when (val tile = state.recentTile()) {
        is CornCircleTile -> when {
            !tile.cornCircleEffect().matches(command.role) -> setOf(InvalidPieceLocationEvent)
            playerDoesNotHaveSuchPieceThere(state, command) -> setOf(InvalidPieceLocationEvent)
            else -> emptySet()
        }
        else -> emptySet()
    }
}

private fun playerDoesNotHaveSuchPieceThere(state: State, command: RemovePieceCmd) =
    state.findPieces(command.position, command.role)
        .none { state.currentPlayerId() == it.playerId && it.pieceOnBoard.piece == command.piece }