package io.github.hejcz.helpers

import io.github.hejcz.core.*

open class Castle(
    val completed: Boolean,
    val parts: Set<PositionedDirection>,
    private val state: State? = null
) {
    val pieces by lazy {
        parts.flatMap { part ->
            state!!.players.map { player -> Pair(player.id, player.pieceOn(part.position, Knight(part.direction))) }
                .filter { (_, knight) -> knight != null }
                .map { (id, knight) -> FoundPiece(id, knight!!, part.position, part.direction) }
        }.toSet()
    }

    private val tilesCount by lazy {
        parts.map { (position, _) -> position }.distinct().size
    }

    private val emblems by lazy {
        parts
            .groupBy { (position, _) -> position } // without grouping emblem would be counted multiple times
            .map { (_, v) -> v.any { (position, direction) -> state!!.tileAt(position).hasEmblem(direction) } }
            .count { it }
    }

    companion object {
        fun from(state: State, parts: Set<PositionedDirection>, isCompleted: Boolean) =
            Castle(isCompleted, parts, state)

        fun empty(): Castle = Castle(false, emptySet())
    }

    fun countEmblemsAndTiles() = emblems + tilesCount

    fun piecesOf(playerId: Long) = pieces.filter { piece -> piece.playerId == playerId }.map { it.pieceOnBoard }.toSet()
}

