package io.github.hejcz.basic.tiles

import io.github.hejcz.core.Location

interface GreenFieldExplorable {
    fun exploreGreenFields(location: Location): Collection<io.github.hejcz.core.Location>
}
