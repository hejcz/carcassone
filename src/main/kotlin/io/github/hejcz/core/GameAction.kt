package io.github.hejcz.core

import io.github.hejcz.core.tile.*

fun endTurn(game: Game, events: Collection<GameEvent> = emptySet()): Collection<GameEvent> {
    val state = game.state
    state.changeActivePlayer()
    return when (state.currentTile) {
        is NoTile -> game.endRules.flatMap { it.apply(state) } + events
        else -> setOf(PlaceTile(state.currentTile.name(), state.currentPlayerId())) + events
    }
}