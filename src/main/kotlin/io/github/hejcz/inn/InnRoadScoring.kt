package io.github.hejcz.inn

import io.github.hejcz.basic.*
import io.github.hejcz.core.*
import io.github.hejcz.helpers.*
import io.github.hejcz.inn.tiles.*

object InnRoadScoring : RoadScoringStrategy {
    override fun score(state: State, road: ExploredRoad): Int {
        val isInnNearRoad = road.pieces.any {
            val tile = state.tileAt(it.position)
            tile is InnTile && tile.isInnOnRoad(it.direction)
        }
        return when {
            road.completed -> road.tilesCount * if (isInnNearRoad) 2 else 1
            else -> road.tilesCount * if (isInnNearRoad) 0 else 1
        }
    }
}
