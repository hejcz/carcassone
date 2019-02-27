package io.github.hejcz.rules.helpers

import io.github.hejcz.engine.State
import io.github.hejcz.mapples.Brigand

data class TestedRoad(
    val completed: Boolean,
    val tilesCount: Int,
    val pieces: Set<FoundPiece>
) {
    companion object {
        fun from(state: State, roadExplorer: RoadExplorer) =
            TestedRoad(
                roadExplorer.isCompleted(),
                roadExplorer.positions().size,
                roadExplorer.parts().flatMap { element ->
                    state.players.filter { player ->
                        player.isPieceOn(element.position, Brigand(element.direction))
                    }.map { player ->
                        FoundPiece(
                            player.id,
                            element.position,
                            element.direction
                        )
                    }
                }.toSet()
            )

        fun empty(): TestedRoad = TestedRoad(false, 0, emptySet())
    }
}