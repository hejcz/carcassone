package io.github.hejcz.core

import io.github.hejcz.expansion.inn.tiles.*

data class Road(val completed: Boolean, val parts: Set<PositionedDirection>, private val state: State) {

    val tilesCount by lazy {
        parts.map { it.position }.distinct().count()
    }

    fun pieces() =
        parts.flatMap { road ->
            state.findPieces(road.position, Brigand(road.direction))
                .map { (id, brigand) -> FoundPiece(brigand, road.position, road.direction, id) }
        }

    fun createPlayerScoredEvent(playerId: Long, score: Int) =
        ScoreEvent(playerId, score, piecesOf(playerId))

    fun createPlayerScoredEventWithoutPieces(playerId: Long, score: Int) =
        ScoreEvent(playerId, score, emptySet())

    fun createOccupiedAreaCompletedEvent(playerId: Long) =
        NoScoreEvent(playerId, piecesOf(playerId))

    fun hasInn(state: State) = pieces().any {
        val tile = state.tileAt(it.position)
        tile is InnTile && tile.isInnOnRoad(it.direction)
    }

    val hasMage = state.getMageAndWitchState()
        ?.let { parts.any { part -> it.isMageOn(part.position, part.direction) } }
        ?: false

    val hasWitch = state.getMageAndWitchState()
        ?.let { parts.any { part -> it.isWitchOn(part.position, part.direction) } }
        ?: false

    fun piecesOf(playerId: Long): Collection<PieceOnBoard> = pieces()
        .filter { !it.isNPC }
        .filter { it.playerId() == playerId }
        .map { it.pieceOnBoard }

    companion object {
        fun from(state: State, parts: Set<PositionedDirection>, isCompleted: Boolean) =
            Road(isCompleted, parts, state)
    }
}
