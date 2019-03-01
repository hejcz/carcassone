package io.github.hejcz.helpers

import io.github.hejcz.core.*

data class ExploredRoad(
    val completed: Boolean,
    val tilesCount: Int,
    val pieces: Set<FoundPiece>
) {

    fun createPlayerScoredEvent(playerId: Long, score: Int) =
        PlayerScored(playerId, score, List(pieces.count { it.playerId == playerId }) { SmallPiece })

    fun createPlayerScoredEventWithoutPieces(playerId: Long, score: Int) =
        PlayerScored(playerId, score, emptySet())

    fun createOccupiedAreaCompletedEvent(playerId: Long) =
        OccupiedAreaCompleted(playerId, List(pieces.count { it.playerId == playerId }) { SmallPiece })

    companion object {
        fun from(state: State, roadExplorer: RoadExplorer) =
            ExploredRoad(
                roadExplorer.isCompleted(),
                roadExplorer.positions().size,
                roadExplorer.parts().flatMap { road ->
                    state.players.map { player -> Pair(player.id, player.brigandOn(road)) }
                        .filter { (_, brigand) -> brigand != null }
                        .map { (id, brigand) -> FoundPiece(id, brigand!!.piece, road.position, road.direction) }
                    }.toSet()
            )

        fun empty(): ExploredRoad = ExploredRoad(false, 0, emptySet())
    }
}

private fun Player.brigandOn(element: PositionedDirection): PieceOnBoard? =
    this.pieceOn(element.position, Brigand(element.direction))
