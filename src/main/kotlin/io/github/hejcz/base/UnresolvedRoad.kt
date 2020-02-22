package io.github.hejcz.base

import io.github.hejcz.api.*

data class UnresolvedRoad(
    override val completed: Boolean,
    override val parts: Set<PositionedDirection>
) : Road {

    override val tilesCount by lazy {
        parts.map { it.position }.distinct().count()
    }

    override fun resolve(state: State): ResolvedRoad =
        ResolvedRoadImplementation(
            this,
            parts.flatMap { road ->
                state.findPieces(road.position, Brigand(road.direction))
                    .map { (id, brigand) ->
                        FoundPiece(
                            brigand, road.position, road.direction, id
                        )
                    }
            }
        )

    companion object {
        fun from(parts: Set<PositionedDirection>, isCompleted: Boolean) =
            UnresolvedRoad(isCompleted, parts)
    }
}

class ResolvedRoadImplementation(road: UnresolvedRoad, private val pieces: List<FoundPiece>) : ResolvedRoad, Road by road {

    override val tilesCount by lazy {
        parts.map { it.position }.distinct().count()
    }

    override fun pieces() = pieces

    override fun createPlayerScoredEvent(playerId: Long, score: Int) =
        ScoreEvent(playerId, score, piecesOf(playerId))

    override fun createPlayerScoredEventWithoutPieces(playerId: Long, score: Int): GameEvent =
        ScoreEvent(playerId, score, emptySet())

    override fun createOccupiedAreaCompletedEvent(playerId: Long): GameEvent =
        NoScoreEvent(playerId, piecesOf(playerId))

    override fun piecesOf(playerId: Long): Collection<PieceOnBoard> = pieces
        .filter { it.playerId() == playerId }
        .map { it.pieceOnBoard }
}
