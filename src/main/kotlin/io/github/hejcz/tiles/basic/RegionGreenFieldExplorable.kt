package io.github.hejcz.tiles.basic

import io.github.hejcz.placement.Location

class RegionGreenFieldExplorable(private vararg val regions: Collection<Location>) : GreenFieldExplorable {
    override fun exploreGreenFields(location: Location) = regions.find { location in it } ?: emptySet()
}