package io.github.hejcz.tiles.basic

import io.github.hejcz.placement.Location

interface GreenFieldExplorable {
    fun exploreGreenFields(location: Location): Collection<Location>
}