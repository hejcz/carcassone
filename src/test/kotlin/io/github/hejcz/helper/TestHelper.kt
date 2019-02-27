package io.github.hejcz.helper

import io.github.hejcz.core.PlaceTile
import io.github.hejcz.core.SelectPiece

fun <T> Collection<T>.shouldContainSelectPieceOnly() =
    if (this.size != 1 || this.iterator().next() != SelectPiece) {
        throw AssertionError("Expected only SelectPiece event but was ${this}")
    } else {
        // ok
    }

fun <T> Collection<T>.shouldContainPlaceTileOnly() =
    if (this.size != 1 || this.iterator().next() !is PlaceTile) {
        throw AssertionError("Expected only PlaceTile event but was ${this}")
    } else {
        // ok
    }
