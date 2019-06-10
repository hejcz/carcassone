package io.github.hejcz.core

import io.github.hejcz.helpers.*
import io.github.hejcz.inn.tiles.*

interface RoadScoring {
    fun score(state: State, road: Road): Int
}

// Core game
object BasicRoadScoring : RoadScoring {
    override fun score(state: State, road: Road): Int = road.tilesCount
}

// Inn extension
object InnRoadScoring : RoadScoring {
    override fun score(state: State, road: Road): Int = road.tilesCount * when {
        road.hasInn(state) -> if (road.completed) 2 else 0
        else -> 1
    }
}

// Inn extension
private fun Road.hasInn(state: State) = this.pieces.any {
    val tile = state.tileAt(it.position)
    tile is InnTile && tile.isInnOnRoad(it.direction)
}