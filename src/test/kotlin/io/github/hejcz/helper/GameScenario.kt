package io.github.hejcz.helper

import io.github.hejcz.core.*
import org.amshove.kluent.shouldEqual

class GameScenario(private val game: Game) {
    fun then(command: Command) =
        GameScenario(game.dispatch(command))

    fun thenReceivedEventShouldBeOnlyPlaceTile() = _check { it.shouldContainPlaceTileOnly() }

    fun thenReceivedEventShouldBe(event: GameEvent) = _check { it containsEvent event }

    fun thenReceivedEventShouldBe(event: PlayerScored) = _check { it containsEvent event }

    fun thenReceivedEventShouldBe(event: PlayerDidNotScore) = _check { it containsEvent event }

    fun thenShouldNotReceiveEvent(event: GameEvent) = _check { it doesNotContainEvent event }

    fun thenShouldNotReceiveEvent(event: PlayerScored) = _check { it doesNotContainEvent event }

    fun thenShouldNotReceiveEvent(event: PlayerDidNotScore) = _check { it doesNotContainEvent event }

    fun thenNoEventsShouldBePublished() = _check { it.recentEvents() shouldEqual emptyList() }

    fun _check(check: (Game) -> Unit): GameScenario {
        check(game)
        return this
    }

    fun thenReceivedEventsShouldBe(expected: List<PlayerScored>) = _check { it.recentEvents() == expected }

    fun thenEventsCountShouldBe(number: Int) = _check { it.recentEvents().size == number }

    inline fun <reified T> thenReceivedEventsShouldHaveType() = _check { it.recentEvents() is T }

    companion object {
        fun Game.shouldContainPlaceTileOnly() =
            if (this.recentEvents().size != 1 || this.recentEvents().iterator().next() !is PlaceTile) {
                throw AssertionError("Expected only PlaceTile event but was ${this.recentEvents()}")
            } else {
                // ok
            }
    }

    private infix fun Game.containsEvent(expected: GameEvent) = when {
        this.recentEvents().any { it == expected } -> {
        } // ok
        else -> throw AssertionError("Expected event not found: $expected")
    }

    private infix fun Game.containsEvent(expected: PlayerScored) = when {
        this.recentEvents().any {
            it is PlayerScored && it.playerId == expected.playerId && it.score == expected.score && it.returnedPieces.equalsAnyOrder(
                expected.returnedPieces
            )
        } -> {
        } // ok
        else -> throw AssertionError("Expected event not found: $expected\nAvailable events: ${this.recentEvents()}\n")
    }

    private infix fun Game.containsEvent(expected: PlayerDidNotScore) = when {
        this.recentEvents().any {
            it is PlayerDidNotScore && it.playerId == expected.playerId && it.returnedPieces.equalsAnyOrder(
                expected.returnedPieces
            )
        } -> {
        } // ok
        else -> throw AssertionError("Expected event not found: $expected")
    }

    private infix fun Game.doesNotContainEvent(expected: GameEvent) = when {
        this.recentEvents().none { it == expected } -> {
        } // ok
        else -> throw AssertionError("Expected event not found: $expected")
    }

    private infix fun Game.doesNotContainEvent(expected: PlayerScored) = when {
        this.recentEvents().none {
            it is PlayerScored && it.playerId == expected.playerId && it.score == expected.score && it.returnedPieces.equalsAnyOrder(
                expected.returnedPieces
            )
        } -> {
        } // ok
        else -> throw AssertionError("Expected event not found: $expected")
    }

    private infix fun Game.doesNotContainEvent(expected: PlayerDidNotScore) = when {
        this.recentEvents().none {
            it is PlayerDidNotScore && it.playerId == expected.playerId && it.returnedPieces.equalsAnyOrder(
                expected.returnedPieces
            )
        } -> {
        } // ok
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
}