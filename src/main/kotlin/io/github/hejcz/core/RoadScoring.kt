package io.github.hejcz.core

typealias RoadScoring = (State, Road) -> Int

val scoreRoad = { _: State, road: Road -> road.tilesCount }

val scoreRoadWithInn = { state: State, road: Road ->
    road.tilesCount * when {
        road.hasInn(state) -> if (road.completed) 2 else 0
        else -> 1
    }
}
