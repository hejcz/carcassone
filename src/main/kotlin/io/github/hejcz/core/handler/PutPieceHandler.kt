package io.github.hejcz.core.handler

import io.github.hejcz.core.*

object PutPieceHandler : CommandHandler {
    override fun isApplicableTo(command: Command): Boolean = command is PutPiece
    override fun handle(game: Game, command: Command): Collection<GameEvent> {
        val putPieceCommand = command as PutPiece
        game.state.addPiece(putPieceCommand.piece, putPieceCommand.role)
        val events = game.rules.flatMap { it.afterCommand(putPieceCommand, game.state) }
        return endTurn(game, events)
    }
}
