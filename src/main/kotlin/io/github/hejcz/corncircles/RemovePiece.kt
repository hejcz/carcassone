package io.github.hejcz.corncircles

import io.github.hejcz.core.*

data class RemovePieceCommand(val position: Position, val piece: Piece, val role: Role): Command

object RemovePieceHandler : CommandHandler {
    override fun handle(game: Game, command: Command): Collection<GameEvent> = handle(game, command as RemovePieceCommand)

    private fun handle(game: Game, command: RemovePieceCommand): Collection<GameEvent> {
        game.state.removePiece(command.position, command.piece, command.role)
        game.state.changeActivePlayer()
        return emptySet()
    }

    override fun isApplicableTo(command: Command): Boolean = command is RemovePieceCommand
}

object RemovePieceValidator : CommandValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> =
        (command as? RemovePieceCommand)?.let { validate(state, it) } ?: emptySet()

    private fun validate(state: State, command: RemovePieceCommand): Collection<GameEvent> = when (val tile = state.recentTile) {
        is CornCircleTile -> when {
            !tile.cornCircleEffect().matches(command.role) -> setOf(InvalidPieceLocation)
            playerDoesNotHaveSuchPieceThere(state, command) -> setOf(InvalidPieceLocation)
            else -> emptySet()
        }
        else -> emptySet()
    }

    private fun playerDoesNotHaveSuchPieceThere(state: State, command: RemovePieceCommand) =
        state.findPieces(command.position, command.role)
            .none { state.currentPlayerId() == it.first && it.second.piece == command.piece }
}
