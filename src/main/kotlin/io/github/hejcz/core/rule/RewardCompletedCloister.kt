package io.github.hejcz.core.rule

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.NoTile

object RewardCompletedCloister : Scoring {

    private const val COMPLETED_CLOISTER_REWARD = 9

    override fun apply(command: Command, state: State): Collection<GameEvent> = when (command) {
        is PieceCmd -> afterTilePlaced(state.recentPosition(), state) + afterPiecePlaced(state, command.piece, command.role)
        is SkipPieceCmd -> afterTilePlaced(state.recentPosition(), state)
        else -> emptySet()
    }.distinct()

    private fun afterTilePlaced(position: Position, state: State): Collection<GameEvent> =
        position.surrounding()
            .filter { state.tileAt(it).hasCloister() }
            .filter { isSurrounded(state, it) }
            .flatMap { cloisterPosition -> pieceWithOwner(state, cloisterPosition) }
            .map { (playerId, pieceOnBoard) -> ScoreEvent(playerId, COMPLETED_CLOISTER_REWARD, setOf(pieceOnBoard)) }

    private fun pieceWithOwner(state: State, completedCloisterPosition: Position) =
        state.findPieces(completedCloisterPosition, Monk)

    private fun afterPiecePlaced(state: State, piece: Piece, role: Role): Collection<GameEvent> =
        when (role) {
            !is Monk -> emptySet()
            else -> when {
                isSurrounded(state, state.recentPosition()) -> playerScoredEvent(state, piece, role)
                else -> emptySet()
            }
        }

    private fun playerScoredEvent(state: State, piece: Piece, role: Role): Set<ScoreEvent> {
        val pieceOnBoard = PieceOnBoard(state.recentPosition(), piece, role)
        return setOf(ScoreEvent(state.currentPlayerId(), COMPLETED_CLOISTER_REWARD, setOf(pieceOnBoard)))
    }

    private fun isSurrounded(state: State, position: Position): Boolean =
        position.surrounding()
            .all { state.tileAt(it) !is NoTile }
}
