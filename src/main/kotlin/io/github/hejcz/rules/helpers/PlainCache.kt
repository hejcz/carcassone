package io.github.hejcz.rules.helpers

import io.github.hejcz.placement.Location
import io.github.hejcz.placement.Position
import io.github.hejcz.placement.PositionedDirection

class PlainCache {
    val visited = mutableSetOf<Pair<Position, Location>>()
    val missing = mutableSetOf<Pair<Position, Location>>()
    val reachableCastles = mutableSetOf<PositionedDirection>()
}