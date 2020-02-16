package io.github.hejcz.corncircles

import io.github.hejcz.core.*
import io.github.hejcz.util.*

data class AddPieceCmd(val position: Position, val piece: Piece, val role: Role) : Command

val AddPieceHandler = object : CommandHandler {
    override fun isApplicableTo(command: Command): Boolean = command is AddPieceCmd

    override fun beforeScoring(state: State, command: Command): GameChanges =
        (command as AddPieceCmd).let {
            GameChanges.noEvents(
                state.addPiece(command.position, command.piece, command.role)
                    .changeActivePlayer()
            )
        }
}

val AddPieceValidator = commandValidator<AddPieceCmd> { state, command ->
    when (val tile = state.recentTile()) {
        is CornCircleTile -> when {
            !tile.cornCircleEffect().matches(command.role) -> setOf(InvalidPieceLocation)
            playerDoesNotHaveAnyPieceThere(state, command) -> setOf(InvalidPieceLocation)
            else -> emptySet()
        }
        else -> emptySet()
    }
}

private fun playerDoesNotHaveAnyPieceThere(state: State, command: AddPieceCmd) =
    state.findPieces(command.position, command.role)
        .none { state.currentPlayerId() == it.playerId }