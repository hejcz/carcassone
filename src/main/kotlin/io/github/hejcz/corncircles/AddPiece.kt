package io.github.hejcz.corncircles

import io.github.hejcz.core.*
import io.github.hejcz.util.*

data class AddPieceCommand(val position: Position, val piece: Piece, val role: Role) : Command

val AddPieceHandler = eventHandlerNoEvents<AddPieceCommand> { game, command ->
    game.state.addPiece(command.position, command.piece, command.role)
    game.state.changeActivePlayer()
}

val AddPieceValidator = commandValidator<AddPieceCommand> { state, command ->
    when (val tile = state.recentTile()) {
        is CornCircleTile -> when {
            !tile.cornCircleEffect().matches(command.role) -> setOf(InvalidPieceLocation)
            playerDoesNotHaveAnyPieceThere(state, command) -> setOf(InvalidPieceLocation)
            else -> emptySet()
        }
        else -> emptySet()
    }
}

private fun playerDoesNotHaveAnyPieceThere(state: State, command: AddPieceCommand) =
    state.findPieces(command.position, command.role)
        .none { state.currentPlayerId() == it.first }