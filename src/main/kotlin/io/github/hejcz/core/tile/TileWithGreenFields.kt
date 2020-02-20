package io.github.hejcz.core.tile

import io.github.hejcz.core.Location
import io.github.hejcz.core.Locations

interface TileWithGreenFields {
    fun exploreGreenFields(location: Location): Locations
}
