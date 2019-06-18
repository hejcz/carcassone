package io.github.hejcz.corncircles

import io.github.hejcz.core.*

data class AddPieceCommand(val position: Position, val piece: Piece, val role: Role): Command

object AddPieceHandler : CommandHandler {
    override fun handle(game: Game, command: Command): Collection<GameEvent> = handle(game, command as AddPieceCommand)

    private fun handle(game: Game, command: AddPieceCommand): Collection<GameEvent> {
        game.state.addPiece(command.position, command.piece, command.role)
        game.state.changeActivePlayer()
        return emptySet()
    }

    override fun isApplicableTo(command: Command): Boolean = command is AddPieceCommand
}

object AddPieceValidator : CommandValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> =
        (command as? AddPieceCommand)?.let { validate(state, it) } ?: emptySet()

    private fun validate(state: State, command: AddPieceCommand): Collection<GameEvent> = when (val tile = state.recentTile) {
        is CornCircleTile -> when {
            !tile.cornCircleEffect().matches(command.role) -> setOf(InvalidPieceLocation)
            playerDoesNotHaveAnyPieceThere(state, command) -> setOf(InvalidPieceLocation)
            else -> emptySet()
        }
        else -> emptySet()
    }

    private fun playerDoesNotHaveAnyPieceThere(state: State, command: AddPieceCommand) =
        state.findPieces(command.position, command.role)
            .none { state.currentPlayerId() == it.first }
}