package io.github.hejcz

import org.junit.ComparisonFailure
import java.lang.AssertionError

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