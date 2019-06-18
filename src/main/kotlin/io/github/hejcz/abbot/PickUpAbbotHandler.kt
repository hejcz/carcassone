package io.github.hejcz.abbot

import io.github.hejcz.core.*

object PickUpAbbotHandler : CommandHandler {
    override fun isApplicableTo(command: Command): Boolean = command is PickUpAbbot
    override fun handle(game: Game, command: Command): Collection<GameEvent> = handle(game, command as PickUpAbbot)

    private fun handle(game: Game, command: PickUpAbbot): Collection<GameEvent> {
        val events = game.rules.flatMap { it.afterCommand(command, game.state) }
        game.state.returnPieces(setOf(OwnedPiece(PieceOnBoard(command.position, AbbotPiece, Abbot), game.state.currentPlayerId())))
        return events
    }
}
