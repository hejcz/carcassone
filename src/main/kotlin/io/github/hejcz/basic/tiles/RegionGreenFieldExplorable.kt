package io.github.hejcz.basic.tiles

import io.github.hejcz.core.Location

class RegionGreenFieldExplorable(private vararg val regions: Collection<Location>) :
    GreenFieldExplorable {
    override fun exploreGreenFields(location: Location) = regions.find { location in it } ?: emptySet()
}
