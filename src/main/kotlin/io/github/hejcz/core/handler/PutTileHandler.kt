package io.github.hejcz.core.handler

import io.github.hejcz.core.*

object PutTileHandler : CommandHandler {
    override fun isApplicableTo(command: Command): Boolean = command is PutTile
    override fun handle(game: Game, command: Command): Collection<GameEvent> {
        val putTileCommand = command as PutTile
        game.state.addTile(putTileCommand.position, putTileCommand.rotation)
        return game.runAllRules(command)
    }
}
