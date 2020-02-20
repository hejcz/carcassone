package io.github.hejcz.expansion.river

import io.github.hejcz.engine.setup.Extension
import io.github.hejcz.engine.setup.TilesSetup
import io.github.hejcz.engine.setup.ValidatorsSetup
import io.github.hejcz.expansion.river.tiles.*
import io.github.hejcz.engine.setup.*

object RiverExtension : Extension {
    override fun modify(deck: TilesSetup): Unit = deck.addOnTop(riverTiles())
    override fun modify(validatorsSetup: ValidatorsSetup): Unit = validatorsSetup.add(PutRiverTileValidator)
    private fun riverTiles() =
        listOf(TileBB6F1) +
            listOf(
                TileBB6F2, TileBB6F3, TileBB6F4, TileBB6F5, TileBB6F6, TileBB6F7, TileBB6F8, TileBB6F9, TileBB6F10,
                TileBB6F11
            ).shuffled() +
            listOf(TileBB6F12)
}
