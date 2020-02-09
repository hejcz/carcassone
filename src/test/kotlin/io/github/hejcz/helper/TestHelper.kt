package io.github.hejcz.helper

import io.github.hejcz.core.*

fun Game.shouldContainSelectPiece() =
    if (!this.recentEvents().contains(SelectPiece)) {
        throw AssertionError("Expected only SelectPiece event but was $this")
    } else {
        // ok
    }

fun Game.shouldContainPlaceTileOnly() =
    if (this.recentEvents().size != 1 || this.recentEvents().iterator().next() !is PlaceTile) {
        throw AssertionError("Expected only PlaceTile event but was $this")
    } else {
        // ok
    }

infix fun Game.containsEvent(expected: GameEvent) = when {
    this.recentEvents().any { it == expected } ->
    {} // ok
    else -> throw AssertionError("Expected event not found: $expected")
}

infix fun Game.containsEvent(expected: PlayerScored) = when {
    this.recentEvents().any{ it is PlayerScored && it.playerId == expected.playerId && it.score == expected.score && it.returnedPieces.equalsAnyOrder(expected.returnedPieces) } ->
    {} // ok
    else -> throw AssertionError("Expected event not found: $expected")
}

infix fun Game.containsEvent(expected: PlayerDidNotScore) = when {
    this.recentEvents().any { it is PlayerDidNotScore && it.playerId == expected.playerId && it.returnedPieces.equalsAnyOrder(expected.returnedPieces) } ->
        {} // ok
    else -> throw AssertionError("Expected event not found: $expected")
}

infix fun Game.doesNotContainEvent(expected: GameEvent) = when {
    this.recentEvents().none{ it == expected } ->
    {} // ok
    else -> throw AssertionError("Expected event not found: $expected")
}

infix fun Game.doesNotContainEvent(expected: PlayerScored) = when {
    this.recentEvents().none{ it is PlayerScored && it.playerId == expected.playerId && it.score == expected.score && it.returnedPieces.equalsAnyOrder(expected.returnedPieces) } ->
    {} // ok
    else -> throw AssertionError("Expected event not found: $expected")
}

infix fun Game.doesNotContainEvent(expected: PlayerDidNotScore) = when {
    this.recentEvents().none { it is PlayerDidNotScore && it.playerId == expected.playerId && it.returnedPieces.equalsAnyOrder(expected.returnedPieces) } ->
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
