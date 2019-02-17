package io.github.hejcz.engine

import io.github.hejcz.GameEvent
import io.github.hejcz.OccupiedAreaCompleted
import io.github.hejcz.PlayerScored

object ReturnPieceListener : EventListener {
    override fun handle(state: State, event: GameEvent) = when (event) {
        is PlayerScored -> state.players.first { it.id == event.playerId }.returnPieces(event.returnedPiecesIds)
        is OccupiedAreaCompleted -> state.players.first { it.id == event.playerId }.returnPieces(event.returnedPiecesIds)
        else -> {}
    }
}