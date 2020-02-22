package io.github.hejcz.core.handler

import io.github.hejcz.core.Command
import io.github.hejcz.core.CmdHandler
import io.github.hejcz.core.PieceCmd
import io.github.hejcz.core.State

object PutPieceHandler : CmdHandler {
    override fun isApplicableTo(command: Command): Boolean = command is PieceCmd

    override fun apply(state: State, command: Command): State =
        (command as PieceCmd).let { state.addPiece(command.piece, command.role) }
}
