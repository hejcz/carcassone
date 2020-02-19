package io.github.hejcz.core.handler

import io.github.hejcz.core.*

object PutTileHandler : CommandHandler {
    override fun isApplicableTo(command: Command): Boolean = command is TileCmd

    override fun apply(state: State, command: Command): State =
        (command as TileCmd).let { state.addTile(command.position, command.rotation) }
}
