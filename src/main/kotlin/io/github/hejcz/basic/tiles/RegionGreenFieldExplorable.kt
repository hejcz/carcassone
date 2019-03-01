package io.github.hejcz.basic.tiles

import io.github.hejcz.core.*

class RegionGreenFieldExplorable(private vararg val regions: Locations) :
    GreenFieldExplorable {
    override fun exploreGreenFields(location: Location) = regions.find { location in it } ?: emptySet()
}
