package io.github.hejcz.inn

import io.github.hejcz.core.*
import io.github.hejcz.core.rule.*
import io.github.hejcz.inn.tiles.*
import io.github.hejcz.setup.*

object InnAndCathedralsExtension : Extension {
    override fun modify(piecesSetup: PiecesSetup) = piecesSetup.add(BigPiece)

    override fun modify(deck: TilesSetup) {
        deck.addAndShuffle(TileEA, TileEB, TileEC, TileED, TileEE, TileEF, TileEG, TileEH, TileEI, TileEJ,
            TileEK, TileEL, TileEM, TileEN, TileEO, TileEP, TileEQ)
    }
}
