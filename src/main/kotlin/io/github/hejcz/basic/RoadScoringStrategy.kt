package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.helpers.*

interface RoadScoringStrategy {

    fun score(state: State, road: ExploredRoad): Int

}
