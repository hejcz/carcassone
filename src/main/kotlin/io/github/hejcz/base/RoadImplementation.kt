package io.github.hejcz.base

import io.github.hejcz.api.GameEvent
import io.github.hejcz.api.PositionedDirection
import io.github.hejcz.api.Road
import io.github.hejcz.api.State

data class RoadImplementation(
    override val completed: Boolean,
    override val parts: Set<PositionedDirection>,
    private val state: State
) : Road {

    override val tilesCount by lazy {
        parts.map { it.position }.distinct().count()
    }

    override fun pieces() =
        parts.flatMap { road ->
            state.findPieces(road.position, Brigand(road.direction))
                .map { (id, brigand) ->
                    FoundPiece(
                        brigand, road.position, road.direction, id
                    )
                }
        }

    override fun createPlayerScoredEvent(playerId: Long, score: Int) =
        ScoreEvent(playerId, score, piecesOf(playerId))

    override fun createPlayerScoredEventWithoutPieces(playerId: Long, score: Int): GameEvent =
        ScoreEvent(playerId, score, emptySet())

    override fun createOccupiedAreaCompletedEvent(playerId: Long): GameEvent =
        NoScoreEvent(playerId, piecesOf(playerId))

    override fun piecesOf(playerId: Long): Collection<PieceOnBoard> = pieces()
        .filter { it.playerId() == playerId }
        .map { it.pieceOnBoard }

    override fun newWith(state: State): Road = copy(state = state)

    companion object {
        fun from(state: State, parts: Set<PositionedDirection>, isCompleted: Boolean) =
            RoadImplementation(isCompleted, parts, state)
    }
}
