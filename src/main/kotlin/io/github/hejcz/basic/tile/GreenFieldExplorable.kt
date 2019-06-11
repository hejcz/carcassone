package io.github.hejcz.basic.tile

import io.github.hejcz.core.*

interface GreenFieldExplorable {
    fun exploreGreenFields(location: Location): Locations
}
