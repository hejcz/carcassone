package io.github.hejcz.abbot

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*

object AbbotPickedUp : Rule {

    override fun afterCommand(command: Command, state: State): Collection<GameEvent> = when (command) {
        is PickUpAbbot -> afterAbbotPicked(state, command.position)
        else -> emptySet()
    }

    private fun afterAbbotPicked(state: State, position: Position): Collection<GameEvent> =
        state.findPieceAsSet(position, Abbot)
            .map { (playerId, piece) -> PlayerScored(playerId, score(state, piece.position), setOf(PieceOnBoard(position, AbbotPiece, Abbot))) }

    private fun score(state: State, cloisterPosition: Position): Int =
        1 + cloisterPosition.surrounding().count { state.tileAt(it) !is NoTile }
}
