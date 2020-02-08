package io.github.hejcz.core.handler

import io.github.hejcz.core.*

object PutTileHandler : CommandHandler {
    override fun isApplicableTo(command: Command): Boolean = command is PutTile

    override fun beforeScoring(state: State, command: Command): GameChanges =
        (command as PutTile).let { GameChanges.noEvents(state.addTile(command.position, command.rotation)) }

}
