package io.github.hejcz.helper

import io.github.hejcz.core.*
import org.amshove.kluent.shouldEqual

class GameScenario(private val game: Game) {
    fun then(command: Command) =
        GameScenario(game.dispatch(command))

    fun shouldContainSelectPiece() = thenReceivedEventShouldBe(SelectPiece)

    fun shouldContainPlaceTileOnly() = check(Game::shouldContainPlaceTileOnly)

    fun thenReceivedEventShouldBe(event: GameEvent) = check { it containsEvent event }

    fun thenReceivedEventShouldBe(event: PlayerScored) = check { it containsEvent event }

    fun thenReceivedEventShouldBe(event: PlayerDidNotScore) = check { it containsEvent event }

    fun thenShouldNotReceiveEvent(event: GameEvent) = check { it doesNotContainEvent event }

    fun thenShouldNotReceiveEvent(event: PlayerScored) = check { it doesNotContainEvent event }

    fun thenShouldNotReceiveEvent(event: PlayerDidNotScore) = check { it doesNotContainEvent event }

    fun thenNoEventsShouldBePublished() = check { it.recentEvents() shouldEqual emptyList() }

    private fun check(check: (Game) -> Unit): GameScenario {
        check(game)
        return this
    }

    fun thenReceivedEventsShouldBe(expected: List<PlayerScored>) = check { it.recentEvents() == expected }
}