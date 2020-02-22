package io.github.hejcz.api

import io.github.hejcz.base.*

interface Road {
    val completed: Boolean
    val parts: Set<PositionedDirection>
    val tilesCount: Int
    fun resolve(state: State): ResolvedRoad
}

/**
 * Pieces must be resolved against specific state because after the road is completed
 * player may still add his piece on it according to rules so pieces can't be resolved immediately.
 */
interface ResolvedRoad : Road {
    fun pieces(): List<FoundPiece>
    fun createPlayerScoredEvent(playerId: Long, score: Int): GameEvent
    fun createPlayerScoredEventWithoutPieces(playerId: Long, score: Int): GameEvent
    fun createOccupiedAreaCompletedEvent(playerId: Long): GameEvent
    fun piecesOf(playerId: Long): Collection<PieceOnBoard>
}