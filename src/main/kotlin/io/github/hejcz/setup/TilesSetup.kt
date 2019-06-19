package io.github.hejcz.setup

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*

class TilesSetup {

    private var deck = basicDeck.shuffled()

    fun remainingTiles(): RemainingTiles = RemainingTilesFromSequence(deck)

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

    companion object {
        private val basicDeck: List<Tile> = listOf(
            2 * TileA,
            4 * TileB,
            1 * TileC,
            4 * TileD,
            4 * TileE,
            1 * TileEWithGarden,
            2 * TileF,
            1 * TileG,
            2 * TileH,
            1 * TileHWithGarden,
            1 * TileI,
            1 * TileIWithGarden,
            3 * TileJ,
            3 * TileK,
            3 * TileL,
            1 * TileM,
            1 * TileMWithGarden,
            2 * TileN,
            1 * TileNWithGarden,
            2 * TileO,
            3 * TileP,
            1 * TileQ,
            2 * TileR,
            1 * TileRWithGarden,
            2 * TileS,
            1 * TileT,
            6 * TileU,
            2 * TileUWithGarden,
            8 * TileV,
            1 * TileVWithGarden,
            4 * TileW,
            1 * TileX
        ).flatten()
    }
}

private operator fun Int.times(tile: Tile): List<Tile> = (1..this).map { tile }
