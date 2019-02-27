package io.github.hejcz.engine

import io.github.hejcz.tiles.river.*

sealed class Extension {
    abstract fun modify(deck: DefaultDeck)
    abstract fun modify(validators: DefaultValidators)
}

object River : Extension() {
    override fun modify(deck: DefaultDeck) {
        deck.addTop(
            listOf(TileBB6F1) +
                listOf(
                    TileBB6F2,
                    TileBB6F2,
                    TileBB6F3,
                    TileBB6F4,
                    TileBB6F5,
                    TileBB6F6,
                    TileBB6F7,
                    TileBB6F8,
                    TileBB6F9,
                    TileBB6F11
                ).shuffled() +
                listOf(TileBB6F12)
        )
    }

    override fun modify(validators: DefaultValidators) {
        validators.add(PutRiverTileValidator)
    }
}
