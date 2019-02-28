package io.github.hejcz.basic

import io.github.hejcz.basic.tiles.NoTile
import io.github.hejcz.core.*

object CloisterCompletedRule : Rule {

    override fun afterCommand(command: Command, state: State): Collection<GameEvent> = when (command) {
        is PutTile -> afterTilePlaced(state.recentPosition, state)
        is PutPiece -> afterPiecePlaced(state, command.pieceRole)
        else -> emptySet()
    }

    private fun afterTilePlaced(position: Position, state: State): Collection<GameEvent> =
        position.surrounding()
            .filter { state.tileAt(it).hasCloister() }
            .filter { isSurrounded(state, it) }
            .mapNotNull { cloisterPosition -> pieceWithOwner(state, cloisterPosition) }
            .map { (player, pieceOnBoard) -> PlayerScored(player.id, 9, setOf(pieceOnBoard.piece)) }

    private fun pieceWithOwner(state: State, completedCloisterPosition: Position) =
        state.players.mapNotNull { player -> player.pieceOn(completedCloisterPosition, Monk)?.let { Pair(player, it) } }
            .firstOrNull()

    private fun afterPiecePlaced(state: State, pieceRole: PieceRole): Collection<GameEvent> =
        when (pieceRole) {
            !is Monk -> emptySet()
            else -> when {
                isSurrounded(state, state.recentPosition) -> setOf(
                    PlayerScored(
                        state.currentPlayerId(),
                        9,
                        setOf(SmallPiece)
                    )
                )
                else -> emptySet()
            }
        }

    private fun isSurrounded(state: State, position: Position): Boolean =
        position.surrounding()
            .all { state.tileAt(it) !is NoTile }
}
