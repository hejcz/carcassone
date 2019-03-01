package io.github.hejcz.helpers

import io.github.hejcz.core.*

open class ExploredCastle(
    val completed: Boolean,
    val tilesCount: Int,
    val pieces: Set<FoundPiece>,
    val elements: Set<PositionedDirection>,
    val emblems: Int
) {
    companion object {
        fun from(state: State, castleExplorer: CastleExplorer) =
            ExploredCastle(
                castleExplorer.isCompleted(),
                castleExplorer.positions().size,
                castleExplorer.parts().flatMap { castle ->
                    state.players.map { player -> Pair(player.id, player.knightOn(castle)) }
                        .filter { (_, knight) -> knight != null }
                        .map { (id, knight) -> FoundPiece(id, knight!!.piece, castle.position, castle.direction) }
                }.toSet(),
                castleExplorer.parts(),
                castleExplorer.positions().map { state.tileAt(it) }.count { it.hasEmblem() }
            )

        fun empty(): ExploredCastle = ExploredCastle(false, 0, emptySet(), emptySet(), 0)
    }


}

private fun Player.knightOn(element: PositionedDirection): PieceOnBoard? =
    this.pieceOn(element.position, Knight(element.direction))
