package io.github.hejcz.core.handler

import io.github.hejcz.core.*

object SkipPieceHandler : CommandHandler {
    override fun isApplicableTo(command: Command): Boolean = command is SkipPiece
    override fun handle(game: Game, command: Command): Collection<GameEvent> = endTurn(game)
}
