package io.github.hejcz.engine

import io.github.hejcz.tiles.basic.*

class DefaultDeck {

    private var deck = basicDeck.shuffled()

    fun remainingTiles(): RemainingTiles = RemainingTilesFromSequence(deck)

    fun withExtensions(vararg extension: Extension): DefaultDeck {
        extension.forEach { it.modify(this) }
        return this
    }

    companion object {
        private val basicDeck: List<Tile> = listOf(
            2 * TileA,
            4 * TileB,
            1 * TileC,
            4 * TileD,
            5 * TileE,
            2 * TileF,
            1 * TileG,
            3 * TileH,
            2 * TileI,
            3 * TileJ,
            3 * TileK,
            3 * TileL,
            2 * TileM,
            3 * TileN,
            2 * TileO,
            3 * TileP,
            1 * TileQ,
            3 * TileR,
            2 * TileS,
            1 * TileT,
            8 * TileU,
            9 * TileV,
            4 * TileW,
            1 * TileX
        ).flatten()
    }

}

operator fun Int.times(tile: Tile): List<Tile> = (1..this).map { tile }