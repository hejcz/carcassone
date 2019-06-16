package io.github.hejcz.core.tile

import io.github.hejcz.core.*

interface TileWithGreenFields {
    fun exploreGreenFields(location: Location): Locations
}
