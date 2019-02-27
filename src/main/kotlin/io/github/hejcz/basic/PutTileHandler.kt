package io.github.hejcz.basic

import io.github.hejcz.core.*

object PutTileHandler : CommandHandler {
    override fun isApplicableTo(command: Command): Boolean = command is PutTile
    override fun handle(game: Game, command: Command): Collection<GameEvent> =
        handle(game, command as PutTile)

    private fun handle(game: Game, command: PutTile): Collection<GameEvent> {
        game.state.addTile(command.position, command.rotation)
        return game.rules.flatMap { it.afterCommand(command, game.state) } + setOf(SelectPiece)
    }
}
