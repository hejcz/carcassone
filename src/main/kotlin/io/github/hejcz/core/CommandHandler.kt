package io.github.hejcz.core

interface CommandHandler {
    fun isApplicableTo(command: Command): Boolean
    fun beforeScoring(state: State, command: Command): GameChanges

    fun afterScoring(state: State, scoreEvents: Collection<GameEvent>): GameChanges {
        // clean up pieces
        val state1 = scoreEvents
            .mapNotNull { it as? PlayerScored }
            .fold(state) { s, event -> s.returnPieces(event.returnedPieces.map { OwnedPiece(event.playerId, it) }) }
        val state2 = scoreEvents
            .mapNotNull { it as? PlayerDidNotScore }
            .fold(state1) { s, event -> s.returnPieces(event.returnedPieces.map { OwnedPiece(event.playerId, it) }) }
        val state3 = scoreEvents
            .mapNotNull { it as? CastleFinished }
            .fold(state2) { s, event -> s.addCompletedCastle(event.castle) }
        val state4 = scoreEvents
            .mapNotNull { it as? RoadFinished }
            .fold(state3) { s, event -> s.addCompletedRoad(event.road) }
        return GameChanges.noEvents(state4)
    }
}
