package io.github.hejcz.components.inn

import io.github.hejcz.base.BigPiece
import io.github.hejcz.setup.Extension
import io.github.hejcz.setup.PiecesSetup
import io.github.hejcz.setup.TilesSetup
import io.github.hejcz.components.inn.tiles.*

object InnAndCathedralsExtension : Extension {
    override fun modify(setup: PiecesSetup) = setup.add(BigPiece)

    override fun modify(deck: TilesSetup) {
        deck.addAndShuffle(TileEA, TileEB, TileEC, TileED, TileEE, TileEF, TileEG, TileEH, TileEI, TileEJ,
            TileEK, TileEL, TileEM, TileEN, TileEO, TileEP, TileEQ)
    }
}
