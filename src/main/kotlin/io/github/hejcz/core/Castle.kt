package io.github.hejcz.core

import io.github.hejcz.expansion.inn.tiles.*

data class Castle(val completed: Boolean, val parts: Set<PositionedDirection>, private val state: State) {
    val pieces by lazy {
        parts.flatMap { part ->
            state.findPieces(part.position, Knight(part.direction))
                .map { (id, knight) -> FoundPiece(knight, part.position, part.direction, id) }
        }
    }

    val hasMage = parts.any { part -> state.exists(part.position, part.direction, MagePiece) }

    val hasWitch = parts.any { part -> state.exists(part.position, part.direction, WitchPiece) }

    private val tilesCount by lazy {
        parts.map { (position, _) -> position }.distinct().size
    }

    private val emblems by lazy {
        parts
            .groupBy { (position, _) -> position } // without grouping emblem would be counted multiple times
            .map { (_, v) -> v.any { (position, direction) -> state.tileAt(position).hasEmblem(direction) } }
            .count { it }
    }

    companion object {
        fun from(state: State, parts: Set<PositionedDirection>, isCompleted: Boolean) =
            Castle(isCompleted, parts, state)
    }

    fun countEmblemsAndTiles() = emblems + tilesCount

    fun countTiles() = tilesCount

    fun piecesOf(playerId: Long): Collection<PieceOnBoard> = pieces
        .filter { !it.isNPC }
        .filter { it.playerId() == playerId }
        .map { it.pieceOnBoard }

    fun hasCathedral(state: State) =
        this.parts.asSequence().map { state.tileAt(it.position) }.any { it is InnTile && it.hasCathedral() }
}
