package io.github.hejcz.abbot

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*

object AbbotPickedUp : Rule {

    override fun afterCommand(command: Command, state: State): Collection<GameEvent> = when (command) {
        is PickUpAbbotCmd -> afterAbbotPicked(state, command.position)
        else -> emptySet()
    }

    private fun afterAbbotPicked(state: State, position: Position): Collection<GameEvent> =
        state.findPieces(position, Abbot)
            .find { (playerId, _) -> playerId == state.currentPlayerId() }!!
            .let { (playerId, piece) ->
                setOf(
                    PlayerScored(
                        playerId, score(state, piece.position), setOf(PieceOnBoard(position, AbbotPiece, Abbot))
                    )
                )
            }

    private fun score(state: State, abbotPosition: Position): Int =
        1 + abbotPosition.surrounding().count { state.tileAt(it) !is NoTile }
}
