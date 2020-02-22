package io.github.hejcz.setup

import io.github.hejcz.api.Tile

class TilesSetup {

    private var deck = emptyList<Tile>()

    fun remainingTiles(): List<Tile> = deck

    fun addOnTop(topTiles: List<Tile>) {
        deck = topTiles + deck
    }

    fun withExtensions(vararg extension: Extension): TilesSetup {
        extension.forEach { it.modify(this) }
        return this
    }

    fun addAndShuffle(vararg tiles: Tile) {
        deck = (deck + tiles).shuffled()
    }
}
