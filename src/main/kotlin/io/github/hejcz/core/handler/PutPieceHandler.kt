package io.github.hejcz.core.handler

import io.github.hejcz.core.*

object PutPieceHandler : CommandHandler {
    override fun isApplicableTo(command: Command): Boolean = command is PieceCmd

    override fun apply(state: State, command: Command): State =
        (command as PieceCmd).let { state.addPiece(command.piece, command.role) }
}
