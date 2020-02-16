package io.github.hejcz.core.handler

import io.github.hejcz.core.*

object PutPieceHandler : CommandHandler {
    override fun isApplicableTo(command: Command): Boolean = command is PieceCmd

    override fun beforeScoring(state: State, command: Command): GameChanges =
        (command as PieceCmd).let { GameChanges.noEvents(state.addPiece(command.piece, command.role)) }
}
