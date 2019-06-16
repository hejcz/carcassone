package io.github.hejcz.core.tile

import io.github.hejcz.core.*

class GreenFields(private vararg val regions: Locations) : TileWithGreenFields {
    override fun exploreGreenFields(location: Location) = regions.find { location in it } ?: emptySet()
}
