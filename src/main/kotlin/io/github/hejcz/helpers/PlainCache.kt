package io.github.hejcz.helpers

import io.github.hejcz.core.Location

class PlainCache {
    val visited = mutableSetOf<Pair<io.github.hejcz.core.Position, Location>>()
    val missing = mutableSetOf<Pair<io.github.hejcz.core.Position, Location>>()
    val reachableCastles = mutableSetOf<io.github.hejcz.core.PositionedDirection>()
}
