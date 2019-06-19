package io.github.hejcz.core.handler

import io.github.hejcz.core.*

object PutPieceHandler : CommandHandler {
    override fun isApplicableTo(command: Command): Boolean = command is PutPiece
    override fun handle(game: Game, command: Command): Collection<GameEvent> {
        val putPieceCommand = command as PutPiece
        game.state.addPiece(putPieceCommand.piece, putPieceCommand.role)
        return game.runAllRules(command)
    }
}
