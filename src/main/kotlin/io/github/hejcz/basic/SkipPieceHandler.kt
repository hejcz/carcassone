package io.github.hejcz.basic

import io.github.hejcz.core.*

object SkipPieceHandler : CommandHandler {
    override fun isApplicableTo(command: Command): Boolean = command is SkipPiece
    override fun handle(game: Game, command: Command): Collection<GameEvent> = handle(game)

    private fun handle(game: Game): Collection<GameEvent> =
        GameAction.endTurn(game)
}
