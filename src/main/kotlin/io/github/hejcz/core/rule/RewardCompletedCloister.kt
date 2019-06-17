package io.github.hejcz.core.rule

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*

object RewardCompletedCloister : Rule {

    private const val COMPLETED_CLOISTER_REWARD = 9

    override fun afterCommand(command: Command, state: State): Collection<GameEvent> = when (command) {
        is PutTile -> afterTilePlaced(state.recentPosition, state)
        is PutPiece -> afterPiecePlaced(state, command.piece, command.role)
        else -> emptySet()
    }

    private fun afterTilePlaced(position: Position, state: State): Collection<GameEvent> =
        position.surrounding()
            .filter { state.tileAt(it).hasCloister() }
            .filter { isSurrounded(state, it) }
            .flatMap { cloisterPosition -> pieceWithOwner(state, cloisterPosition) }
            .onEach { (playerId, pieceOnBoard) -> state.returnPieces(setOf(OwnedPiece(pieceOnBoard, playerId))) }
            .map { (playerId, pieceOnBoard) -> PlayerScored(playerId, COMPLETED_CLOISTER_REWARD, setOf(pieceOnBoard)) }

    private fun pieceWithOwner(state: State, completedCloisterPosition: Position) =
        state.findPieces(completedCloisterPosition, Monk)

    private fun afterPiecePlaced(state: State, piece: Piece, role: Role): Collection<GameEvent> =
        when (role) {
            !is Monk -> emptySet()
            else -> when {
                isSurrounded(state, state.recentPosition) -> playerScoredEvent(state, piece, role)
                else -> emptySet()
            }
        }

    private fun playerScoredEvent(state: State, piece: Piece, role: Role): Set<PlayerScored> {
        val pieceOnBoard = PieceOnBoard(state.recentPosition, piece, role)
        state.returnPieces(setOf(OwnedPiece(pieceOnBoard, state.currentPlayerId())))
        return setOf(PlayerScored(state.currentPlayerId(), COMPLETED_CLOISTER_REWARD, setOf(pieceOnBoard)))
    }

    private fun isSurrounded(state: State, position: Position): Boolean =
        position.surrounding()
            .all { state.tileAt(it) !is NoTile }
}
