package io.github.hejcz.api

import io.github.hejcz.base.FoundPiece
import io.github.hejcz.base.PieceOnBoard

interface Castle {
    val completed: Boolean
    val parts: Set<PositionedDirection>
    fun pieces(): List<FoundPiece>
    fun countEmblemsAndTiles(): Int
    fun countTiles(): Int
    fun piecesOf(playerId: Long): Collection<PieceOnBoard>
    fun newWith(state: State): Castle
}
