package io.github.hejcz.abbot

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*

object GardenCompletedRule : Rule {

    override fun afterCommand(command: Command, state: State): Collection<GameEvent> = when (command) {
        is PutTile -> afterTilePlaced(state.recentPosition, state)
        is PutPiece -> afterPiecePlaced(state, command.role)
        else -> emptySet()
    }

    private fun afterTilePlaced(position: Position, state: State): Collection<GameEvent> =
        position.surrounding()
            .filter { state.tileAt(it).hasGarden() && it.isSurrounded(state) }
            .mapNotNull { cloisterPosition -> pieceWithOwner(state, cloisterPosition) }
            .map { (player, pieceOnBoard) -> PlayerScored(player.id, 9, setOf(pieceOnBoard)) }

    private fun pieceWithOwner(state: State, completedCloisterPosition: Position) =
        state.players
            .mapNotNull { player -> player.pieceOn(completedCloisterPosition, Abbot)?.let { Pair(player, it) } }
            .firstOrNull()

    private fun afterPiecePlaced(state: State, role: Role): Collection<GameEvent> = when {
        role is Abbot && state.recentPosition.isSurrounded(state) ->
            setOf(PlayerScored(state.currentPlayerId(), 9, setOf(PieceOnBoard(state.recentPosition, AbbotPiece, Abbot))))
        else -> emptySet()
    }

    private fun Position.isSurrounded(state: State): Boolean = this.surrounding().all { state.tileAt(it) !is NoTile }
}
