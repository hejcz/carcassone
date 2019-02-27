package io.github.hejcz.rules.helpers

import io.github.hejcz.engine.State
import io.github.hejcz.mapples.Knight
import io.github.hejcz.placement.PositionedDirection

data class TestedCastle(
    val completed: Boolean,
    val tilesCount: Int,
    val pieces: Set<FoundPiece>,
    val elements: Set<PositionedDirection>,
    val emblems: Int
) {
    companion object {
        fun from(state: State, castleExplorer: CastleExplorer) =
            TestedCastle(
                castleExplorer.isCompleted(),
                castleExplorer.positions().size,
                castleExplorer.parts().flatMap { element ->
                    state.players.filter { player ->
                        player.isPieceOn(element.position, Knight(element.direction))
                    }.map { player ->
                        FoundPiece(
                            player.id,
                            element.position,
                            element.direction
                        )
                    }
                }
                    .toSet(),
                castleExplorer.parts(),
                castleExplorer.positions().map { state.tileAt(it) }.count { it.hasEmblem() }
            )

        fun empty(): TestedCastle = TestedCastle(false, 0, emptySet(), emptySet(), 0)
    }
}