package io.github.hejcz.core

interface CommandHandler {
    fun isApplicableTo(command: Command): Boolean
    fun beforeScoring(state: State, command: Command): GameChanges

    fun afterScoring(state: State, scoreEvents: Collection<GameEvent>): GameChanges {
        // clean up pieces
        val state1 = scoreEvents
            .mapNotNull { it as? PlayerScored }
            .fold(state) { s, event -> s.returnPieces(event.returnedPieces.map { OwnedPiece(it, event.playerId) }) }
        val state2 = scoreEvents
            .mapNotNull { it as? PlayerDidNotScore }
            .fold(state1) { s, event -> s.returnPieces(event.returnedPieces.map { OwnedPiece(it, event.playerId) }) }
        val state3 = scoreEvents
            .mapNotNull { it as? CastleFinished }
            .fold(state2) { s, event -> s.addCompletedCastle(event.castle) }
        return GameChanges.noEvents(state3)
    }
}
