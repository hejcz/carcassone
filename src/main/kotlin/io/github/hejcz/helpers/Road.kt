package io.github.hejcz.helpers

import io.github.hejcz.core.*

data class Road(
    val completed: Boolean,
    val tilesCount: Int,
    val pieces: Set<FoundPiece>
) {

    fun createPlayerScoredEvent(playerId: Long, score: Int) =
        PlayerScored(playerId, score, pieces.filter { it.playerId == playerId }.map { it.pieceOnBoard }.toSet())

    fun createPlayerScoredEventWithoutPieces(playerId: Long, score: Int) =
        PlayerScored(playerId, score, emptySet())

    fun createOccupiedAreaCompletedEvent(playerId: Long) =
        OccupiedAreaCompleted(playerId, pieces.filter { it.playerId == playerId }.map { it.pieceOnBoard }.toSet())

    companion object {
        fun from(state: State, roadExplorer: RoadExplorer) =
            Road(
                roadExplorer.isCompleted(),
                roadExplorer.positions().size,
                roadExplorer.parts().flatMap { road ->
                    state.players.map { player -> Pair(player.id, player.brigandOn(road)) }
                        .filter { (_, brigand) -> brigand != null }
                        .map { (id, brigand) -> FoundPiece(id, brigand!!, road.position, road.direction) }
                    }.toSet()
            )

        fun empty(): Road = Road(false, 0, emptySet())
    }
}

private fun Player.brigandOn(element: PositionedDirection): PieceOnBoard? =
    this.pieceOn(element.position, Brigand(element.direction))
