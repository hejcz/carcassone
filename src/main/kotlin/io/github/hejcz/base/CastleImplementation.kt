package io.github.hejcz.base

import io.github.hejcz.api.Castle
import io.github.hejcz.api.PositionedDirection
import io.github.hejcz.api.State

data class CastleImplementation(
    override val completed: Boolean,
    override val parts: Set<PositionedDirection>,
    private val state: State
) : Castle {

    override fun pieces() =
        parts.flatMap { part ->
            state.findPieces(part.position, Knight(part.direction))
                .map { (id, knight) ->
                    FoundPiece(
                        knight, part.position, part.direction, id
                    )
                }
        }

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
            CastleImplementation(isCompleted, parts, state)
    }

    override fun countEmblemsAndTiles() = emblems + tilesCount

    override fun countTiles() = tilesCount

    override fun piecesOf(playerId: Long): Collection<PieceOnBoard> = pieces()
        .filter { it.playerId() == playerId }
        .map { it.pieceOnBoard }

    override fun newWith(state: State): Castle = copy(state = state)
}
