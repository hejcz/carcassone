package io.github.hejcz.helper

import io.github.hejcz.core.*
import io.github.hejcz.helper.GameScenario.Companion.shouldContainPlaceTileOnly
import org.amshove.kluent.shouldEqual

class GameScenario(private val game: Game) {
    fun then(command: Command): GameScenario {
        val newGame = game.dispatch(command)
        if (newGame.recentEvents().contains(UnexpectedCommandEvent)) {
            throw RuntimeException("Test might not use unexpected command: $command")
        }
        return GameScenario(newGame)
    }

    fun thenReceivedEventShouldBeOnlyPlaceTile() = _check { it.shouldContainPlaceTileOnly() }

    fun thenReceivedEventShouldBe(event: GameEvent) = _check { it containsEvent event }

    fun thenReceivedEventShouldBe(event: ScoreEvent) = _check { it containsEvent event }

    fun thenReceivedEventShouldBe(event: NoScoreEvent) = _check { it containsEvent event }

    fun thenShouldNotReceiveEvent(event: GameEvent) = _check { it doesNotContainEvent event }

    fun thenShouldNotReceiveEvent(event: ScoreEvent) = _check { it doesNotContainEvent event }

    fun thenShouldNotReceiveEvent(event: NoScoreEvent) = _check { it doesNotContainEvent event }

    fun thenNoEventsShouldBePublished() = _check { it.recentEvents() shouldEqual emptyList() }

    fun _check(check: (Game) -> Unit): GameScenario {
        check(game)
        return this
    }

    fun thenReceivedEventsShouldBe(expected: List<ScoreEvent>) = _check { it.recentEvents() == expected }

    fun thenEventsCountShouldBe(number: Int) = _check { it.recentEvents().size == number }

    inline fun <reified T> thenReceivedEventsShouldHaveType() = _check { it.recentEvents() is T }

    companion object {
        fun Game.shouldContainPlaceTileOnly() =
            if (this.recentEvents().size != 1 || this.recentEvents().iterator().next() !is TileEvent) {
                throw AssertionError("Expected only PlaceTile event but was ${this.recentEvents()}")
            } else {
                // ok
            }
    }

    private infix fun Game.containsEvent(expected: GameEvent) = when {
        this.recentEvents().any { it == expected } -> {
        } // ok
        else -> throw AssertionError("Expected event not found: $expected.\nAvailable: ${this.recentEvents()}")
    }

    private infix fun Game.containsEvent(expected: ScoreEvent) = when {
        this.recentEvents().any {
            it is ScoreEvent && it.playerId == expected.playerId && it.score == expected.score && it.returnedPieces.equalsAnyOrder(
                expected.returnedPieces
            )
        } -> {
        } // ok
        else -> throw AssertionError("Expected event not found: $expected\nAvailable events: ${this.recentEvents()}\n")
    }

    private infix fun Game.containsEvent(expected: NoScoreEvent) = when {
        this.recentEvents().any {
            it is NoScoreEvent && it.playerId == expected.playerId && it.returnedPieces.equalsAnyOrder(
                expected.returnedPieces
            )
        } -> {
        } // ok
        else -> throw AssertionError("Expected event not found: $expected")
    }

    private infix fun Game.doesNotContainEvent(expected: GameEvent) = when {
        this.recentEvents().none { it == expected } -> {
        } // ok
        else -> throw AssertionError("Not expected event found: $expected")
    }

    private infix fun Game.doesNotContainEvent(expected: ScoreEvent) = when {
        this.recentEvents().none {
            it is ScoreEvent && it.playerId == expected.playerId && it.score == expected.score && it.returnedPieces.equalsAnyOrder(
                expected.returnedPieces
            )
        } -> {
        } // ok
        else -> throw AssertionError("Expected event not found: $expected")
    }

    private infix fun Game.doesNotContainEvent(expected: NoScoreEvent) = when {
        this.recentEvents().none {
            it is NoScoreEvent && it.playerId == expected.playerId && it.returnedPieces.equalsAnyOrder(
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
