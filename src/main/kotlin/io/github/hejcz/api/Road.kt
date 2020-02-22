package io.github.hejcz.api

import io.github.hejcz.base.*

interface Road {
    val completed: Boolean
    val parts: Set<PositionedDirection>
    val tilesCount: Int
    fun pieces(): List<FoundPiece>
    fun createPlayerScoredEvent(playerId: Long, score: Int): GameEvent
    fun createPlayerScoredEventWithoutPieces(playerId: Long, score: Int): GameEvent
    fun createOccupiedAreaCompletedEvent(playerId: Long): GameEvent
    fun piecesOf(playerId: Long): Collection<PieceOnBoard>
    fun newWith(state: State): Road
}
