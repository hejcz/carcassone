package io.github.hejcz.base.tile

import io.github.hejcz.api.TileWithGreenFields
import io.github.hejcz.api.Location
import io.github.hejcz.api.Locations

class GreenFields(private vararg val regions: Locations) : TileWithGreenFields {
    override fun exploreGreenFields(location: Location) = regions.find { location in it } ?: emptySet()
}
