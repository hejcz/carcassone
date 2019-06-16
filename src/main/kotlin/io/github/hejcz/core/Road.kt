package io.github.hejcz.core

data class Road(
    val completed: Boolean,
    private val parts: Set<PositionedDirection>,
    private val state: State? = null
) {

    val tilesCount by lazy {
        parts.map { it.position }.distinct().count()
    }

    val pieces by lazy {
        parts.flatMap { road ->
            state!!.players.map { player -> Pair(player.id, player.pieceOn(road.position, Brigand(road.direction))) }
                .filter { (_, brigand) -> brigand != null }
                .map { (id, brigand) -> FoundPiece(id, brigand!!, road.position, road.direction) }
        }.toSet()
    }

    fun createPlayerScoredEvent(playerId: Long, score: Int) =
        PlayerScored(playerId, score, pieces.filter { it.playerId == playerId }.map { it.pieceOnBoard }.toSet())

    fun createPlayerScoredEventWithoutPieces(playerId: Long, score: Int) =
        PlayerScored(playerId, score, emptySet())

    fun createOccupiedAreaCompletedEvent(playerId: Long) =
        OccupiedAreaCompleted(playerId, pieces.filter { it.playerId == playerId }.map { it.pieceOnBoard }.toSet())

    companion object {
        fun from(state: State, parts: Set<PositionedDirection>, isCompleted: Boolean) =
            Road(isCompleted, parts, state)

        fun empty(): Road = Road(false, emptySet())
    }
}