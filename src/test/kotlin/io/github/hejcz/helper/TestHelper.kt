package io.github.hejcz.helper

import io.github.hejcz.core.*

fun Collection<GameEvent>.shouldContainSelectPiece() =
    if (!this.contains(SelectPiece)) {
        throw AssertionError("Expected only SelectPiece event but was $this")
    } else {
        // ok
    }

fun <T> Collection<T>.shouldContainPlaceTileOnly() =
    if (this.size != 1 || this.iterator().next() !is PlaceTile) {
        throw AssertionError("Expected only PlaceTile event but was $this")
    } else {
        // ok
    }

infix fun Collection<GameEvent>.containsEvent(expected: PlayerScored) = when {
    this.any{ it is PlayerScored && it.playerId == expected.playerId && it.score == expected.score && it.returnedPieces.equalsAnyOrder(expected.returnedPieces) } ->
    {} // ok
    else -> throw AssertionError("Expected event not found: $expected")
}

infix fun Collection<GameEvent>.containsEvent(expected: PlayerDidNotScore) = when {
    this.any { it is PlayerDidNotScore && it.playerId == expected.playerId && it.returnedPieces.equalsAnyOrder(expected.returnedPieces) } ->
        {} // ok
    else -> throw AssertionError("Expected event not found: $expected")
}

infix fun Collection<GameEvent>.doesNotContainEvent(expected: PlayerScored) = when {
    this.none{ it is PlayerScored && it.playerId == expected.playerId && it.score == expected.score && it.returnedPieces.equalsAnyOrder(expected.returnedPieces) } ->
    {} // ok
    else -> throw AssertionError("Expected event not found: $expected")
}

infix fun Collection<GameEvent>.doesNotContainEvent(expected: PlayerDidNotScore) = when {
    this.none { it is PlayerDidNotScore && it.playerId == expected.playerId && it.returnedPieces.equalsAnyOrder(expected.returnedPieces) } ->
    {} // ok
    else -> throw AssertionError("Expected event not found: $expected")
}

private fun Collection<PieceOnBoard>.equalsAnyOrder(expected: Collection<PieceOnBoard>): Boolean =
    if (this.size != expected.size) {
        false
    } else {
        val elements = expected.toMutableList()
        this.onEach { elements.remove(it) }
        elements.size == 0
    }
