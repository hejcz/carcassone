package io.github.hejcz.corncircles

import io.github.hejcz.core.*

object AddPieceHandler : CommandHandler {
    override fun handle(game: Game, command: Command): Collection<GameEvent> = handle(game, command as AddPiece)

    private fun handle(game: Game, command: AddPiece): Collection<GameEvent> {
        game.state.addPiece(command.position, command.piece, command.role)
        game.state.changeActivePlayer()
        return emptySet()
    }

    override fun isApplicableTo(command: Command): Boolean = command is AddPiece
}

object RemovePieceHandler : CommandHandler {
    override fun handle(game: Game, command: Command): Collection<GameEvent> = handle(game, command as RemovePiece)

    private fun handle(game: Game, command: RemovePiece): Collection<GameEvent> {
        game.state.removePiece(command.position, command.piece, command.role)
        game.state.changeActivePlayer()
        return emptySet()
    }

    override fun isApplicableTo(command: Command): Boolean = command is RemovePiece
}

object ChooseCornCircleActionHandler : CommandHandler {
    override fun handle(game: Game, command: Command): Collection<GameEvent> {
        game.state.changeActivePlayer()
        return emptySet()
    }

    override fun isApplicableTo(command: Command): Boolean = command is RemovePiece
}