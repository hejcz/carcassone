package io.github.hejcz.helpers

import io.github.hejcz.core.*

open class Castle(
    val completed: Boolean,
    val pieces: Set<FoundPiece>,
    val elements: Set<PositionedDirection>,
    private val tilesCount: Int,
    private val emblems: Int
) {
    companion object {
        fun from(state: State, castleExplorer: CastleExplorer) =
            Castle(
                castleExplorer.isCompleted(),
                castleExplorer.parts().flatMap { castle ->
                    state.players.map { player -> Pair(player.id, player.knightOn(castle)) }
                        .filter { (_, knight) -> knight != null }
                        .map { (id, knight) -> FoundPiece(id, knight!!, castle.position, castle.direction) }
                }.toSet(),
                castleExplorer.parts(),
                castleExplorer.positions().size,
                castleExplorer.parts()
                    .groupBy { it.position } // without grouping emblem would be counted multiple times
                    .map { (_, v) -> v.any { state.tileAt(it.position).hasEmblem(it.direction) } }
                    .count { it }
            )

        fun empty(): Castle = Castle(false, emptySet(), emptySet(), 0, 0)
    }

    fun countEmblemsAndTiles() = emblems + tilesCount

    fun piecesOf(playerId: Long) = pieces.filter { piece -> piece.playerId == playerId }.map { it.pieceOnBoard }.toSet()
}

private fun Player.knightOn(element: PositionedDirection): PieceOnBoard? =
    this.pieceOn(element.position, Knight(element.direction))
