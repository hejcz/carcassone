package io.github.hejcz.core

import io.github.hejcz.inn.tiles.*

data class Road(val completed: Boolean, private val parts: Set<PositionedDirection>, private val state: State) {

    val tilesCount by lazy {
        parts.map { it.position }.distinct().count()
    }

    val pieces by lazy {
        parts.flatMap { road ->
            state.findPieceAsSet(road.position, Brigand(road.direction))
                .map { (id, brigand) -> FoundPiece(id, brigand, road.position, road.direction) }
        }.toSet()
    }

    fun createPlayerScoredEvent(playerId: Long, score: Int) =
        PlayerScored(playerId, score, pieces.filter { it.playerId == playerId }.map { it.pieceOnBoard }.toSet())

    fun createPlayerScoredEventWithoutPieces(playerId: Long, score: Int) =
        PlayerScored(playerId, score, emptySet())

    fun createOccupiedAreaCompletedEvent(playerId: Long) =
        PlayerDidNotScore(playerId, pieces.filter { it.playerId == playerId }.map { it.pieceOnBoard }.toSet())

    fun hasInn(state: State) = this.pieces.any {
        val tile = state.tileAt(it.position)
        tile is InnTile && tile.isInnOnRoad(it.direction)
    }

    companion object {
        fun from(state: State, parts: Set<PositionedDirection>, isCompleted: Boolean) =
            Road(isCompleted, parts, state)
    }
}