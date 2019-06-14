package io.github.hejcz.basic.rule

import io.github.hejcz.basic.tile.*
import io.github.hejcz.core.*
import io.github.hejcz.helpers.*

object RewardCompletedCloister : Rule {

    override fun afterCommand(command: Command, state: State): Collection<GameEvent> = when (command) {
        is PutTile -> afterTilePlaced(state.recentPosition, state)
        is PutPiece -> afterPiecePlaced(state, command.piece, command.role)
        else -> emptySet()
    }

    private fun afterTilePlaced(position: Position, state: State): Collection<GameEvent> =
        position.surrounding()
            .filter { state.tileAt(it).hasCloister() }
            .filter { isSurrounded(state, it) }
            .mapNotNull { cloisterPosition -> pieceWithOwner(state, cloisterPosition) }
            .onEach { (player, pieceOnBoard) -> state.returnPieces(setOf(OwnedPiece(pieceOnBoard, player.id))) }
            .map { (player, pieceOnBoard) -> PlayerScored(player.id, 9, setOf(pieceOnBoard)) }

    private fun pieceWithOwner(state: State, completedCloisterPosition: Position) =
        state.players.mapNotNull { player -> player.pieceOn(completedCloisterPosition, Monk)?.let { Pair(player, it) } }
            .firstOrNull()

    private fun afterPiecePlaced(state: State, piece: Piece, role: Role): Collection<GameEvent> =
        when (role) {
            !is Monk -> emptySet()
            else -> when {
                isSurrounded(state, state.recentPosition) -> {
                    val returnedPieces =
                        state.returnPieces(setOf(OwnedPiece(PieceOnBoard(state.recentPosition, piece, role), state.currentPlayerId())))
                    setOf(PlayerScored(state.currentPlayerId(), 9, returnedPieces.mapTo(mutableSetOf()) { it.pieceOnBoard }))
                }
                else -> emptySet()
            }
        }

    private fun isSurrounded(state: State, position: Position): Boolean =
        position.surrounding()
            .all { state.tileAt(it) !is NoTile }
}
