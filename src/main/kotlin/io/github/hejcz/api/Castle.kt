package io.github.hejcz.api

import io.github.hejcz.base.FoundPiece
import io.github.hejcz.base.PieceOnBoard

interface Castle {
    val completed: Boolean
    val parts: Set<PositionedDirection>
    fun countEmblemsAndTiles(): Int
    fun countTiles(): Int
    fun resolve(state: State): Resolved

    /**
     * Pieces must be resolved against specific state because after the castle is completed
     * player may still add his piece on it according to rules so pieces can't be resolved immediately.
     */
    interface Resolved : Castle {
        fun pieces(): List<FoundPiece>
        fun piecesOf(playerId: Long): Collection<PieceOnBoard>
    }
}
