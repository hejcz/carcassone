package io.github.hejcz.basic

import io.github.hejcz.basic.tile.*
import io.github.hejcz.core.*

fun endTurn(game: Game, events: Collection<GameEvent> = emptySet()): Collection<GameEvent> {
    val state = game.state
    state.endTurn()
    return when (state.currentTile) {
        is NoTile -> game.endRules.flatMap { it.apply(state) } + events
        else -> setOf(PlaceTile(state.currentTile.name(), state.currentPlayerId())) + events
    }
}