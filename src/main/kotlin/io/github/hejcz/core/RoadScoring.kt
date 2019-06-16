package io.github.hejcz.core

import io.github.hejcz.inn.tiles.*

typealias RoadScoring = (State, Road) -> Int

val scoreRoad = { _: State, road: Road -> road.tilesCount }

val scoreRoadWithInn = { state: State, road: Road ->
    road.tilesCount * when {
        road.hasInn(state) -> if (road.completed) 2 else 0
        else -> 1
    }
}

// Inn extension
private fun Road.hasInn(state: State) = this.pieces.any {
    val tile = state.tileAt(it.position)
    tile is InnTile && tile.isInnOnRoad(it.direction)
}