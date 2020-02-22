package io.github.hejcz.core.handler

import io.github.hejcz.core.Command
import io.github.hejcz.core.CmdHandler
import io.github.hejcz.core.State
import io.github.hejcz.core.TileCmd

object PutTileHandler : CmdHandler {
    override fun isApplicableTo(command: Command): Boolean = command is TileCmd

    override fun apply(state: State, command: Command): State =
        (command as TileCmd).let { state.addTile(command.position, command.rotation) }
}
