package io.github.hejcz.basic

import io.github.hejcz.basic.tiles.NoTile
import io.github.hejcz.core.Game
import io.github.hejcz.core.GameEvent
import io.github.hejcz.core.PlaceTile

object GameAction {
    fun endTurn(game: Game, events: Collection<GameEvent> = emptySet()): Collection<GameEvent> {
        val state = game.state
        state.endTurn()
        return when (state.currentTile) {
            is NoTile -> game.endRules.flatMap { it.apply(state) } + events
            else -> setOf(PlaceTile(state.currentTile.name(), state.currentPlayerId())) + events
        }
    }
}
