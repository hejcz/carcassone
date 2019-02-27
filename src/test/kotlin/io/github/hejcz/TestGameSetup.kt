package io.github.hejcz

import io.github.hejcz.engine.Extension
import io.github.hejcz.engine.GameSetup
import io.github.hejcz.engine.RemainingTiles
import io.github.hejcz.engine.River

open class TestGameSetup(private val remainingTiles: RemainingTiles, extensions: List<Extension>) :
    GameSetup(*extensions.toTypedArray()) {
    constructor(remainingTiles: RemainingTiles) : this(remainingTiles, emptyList())

    override fun tiles(): RemainingTiles {
        return remainingTiles
    }
}

class RiverTestGameSetup(remainingTiles: RemainingTiles) : TestGameSetup(remainingTiles, listOf(River))