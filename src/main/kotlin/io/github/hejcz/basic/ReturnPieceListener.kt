package io.github.hejcz.basic

import io.github.hejcz.core.*

object ReturnPieceListener : EventListener {
    override fun handle(state: State, event: GameEvent) = when (event) {
        is PlayerScored -> state.players.first { it.id == event.playerId }.returnPieces(event.returnedPieces)
        is OccupiedAreaCompleted -> state.players.first { it.id == event.playerId }.returnPieces(event.returnedPieces)
        else -> {
        }
    }
}
