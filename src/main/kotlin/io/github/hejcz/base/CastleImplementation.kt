package io.github.hejcz.base

import io.github.hejcz.api.Castle
import io.github.hejcz.api.PositionedDirection
import io.github.hejcz.api.State

data class UnresolvedCastle(
    override val completed: Boolean,
    override val parts: Set<PositionedDirection>,
    private val emblemsCount: Int
) : Castle {
    private val tilesCount by lazy {
        parts.map { (position, _) -> position }.distinct().size
    }

    override fun countEmblemsAndTiles(): Int = tilesCount + emblemsCount

    override fun countTiles(): Int = tilesCount

    override fun resolve(state: State): Castle.Resolved =
        ResolvedCastleImplementation(
            this,
            parts.flatMap { part ->
                state.findPieces(part.position, Knight(part.direction))
                    .map { (id, knight) ->
                        FoundPiece(
                            knight, part.position, part.direction, id
                        )
                    }
            }
        )

    companion object {
        fun from(state: State, parts: Set<PositionedDirection>, isCompleted: Boolean) =
            UnresolvedCastle(
                isCompleted,
                parts,
                parts
                    .groupBy { (position, _) -> position } // without grouping emblem would be counted multiple times
                    .map { (_, v) -> v.any { (position, direction) -> state.tileAt(position).hasEmblem(direction) } }
                    .count { it }
            )
    }
}

class ResolvedCastleImplementation(castle: Castle, private val pieces: List<FoundPiece>) :
    Castle.Resolved, Castle by castle {

    override fun pieces() = pieces
    override fun piecesOf(playerId: Long): Collection<PieceOnBoard> = pieces()
        .filter { it.playerId() == playerId }
        .map { it.pieceOnBoard }
}
