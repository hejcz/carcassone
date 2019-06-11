package io.github.hejcz.helpers

import io.github.hejcz.core.*

open class Castle(
    val completed: Boolean,
    val tilesCount: Int,
    val pieces: Set<FoundPiece>,
    val elements: Set<PositionedDirection>,
    val emblems: Int
) {
    companion object {
        fun from(state: State, castleExplorer: CastleExplorer) =
            Castle(
                castleExplorer.isCompleted(),
                castleExplorer.positions().size,
                castleExplorer.parts().flatMap { castle ->
                    state.players.map { player -> Pair(player.id, player.knightOn(castle)) }
                        .filter { (_, knight) -> knight != null }
                        .map { (id, knight) -> FoundPiece(id, knight!!, castle.position, castle.direction) }
                }.toSet(),
                castleExplorer.parts(),
                castleExplorer.positions().map { state.tileAt(it) }.count { it.hasEmblem() }
            )

        fun empty(): Castle = Castle(false, 0, emptySet(), emptySet(), 0)
    }

    fun countEmblemsAndTiles() = emblems + tilesCount

    fun piecesOf(playerId: Long) = pieces.filter { piece -> piece.playerId == playerId }.map { it.pieceOnBoard }.toSet()
}

private fun Player.knightOn(element: PositionedDirection): PieceOnBoard? =
    this.pieceOn(element.position, Knight(element.direction))
