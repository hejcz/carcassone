package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.helpers.*

object BasicRoadScoring : RoadScoringStrategy {
    override fun score(state: State, road: ExploredRoad): Int = road.tilesCount
}
