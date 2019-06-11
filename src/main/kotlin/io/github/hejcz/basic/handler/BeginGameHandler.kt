package io.github.hejcz.basic.handler

import io.github.hejcz.core.*

object BeginGameHandler : CommandHandler {
    override fun isApplicableTo(command: Command): Boolean = command is Begin
    override fun handle(game: Game, command: Command): Collection<GameEvent> =
        setOf(PlaceTile(game.state.currentTileName(), game.state.currentPlayerId()))
}
