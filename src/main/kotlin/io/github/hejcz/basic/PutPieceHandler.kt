package io.github.hejcz.basic

import io.github.hejcz.core.*

object PutPieceHandler : CommandHandler {
    override fun isApplicableTo(command: Command): Boolean = command is PutPiece
    override fun handle(game: Game, command: Command): Collection<GameEvent> =
        handle(game, command as PutPiece)

    private fun handle(game: Game, command: PutPiece): Collection<GameEvent> {
        game.state.addPiece(command.piece, command.role)
        val events = game.rules.flatMap { it.afterCommand(command, game.state) }
        return GameAction.endTurn(game, events)
    }
}
