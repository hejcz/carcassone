package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.helpers.*

class RewardCompletedRoad(private val scoring: RoadScoring) : Rule {

    override fun afterCommand(command: Command, state: State): Collection<GameEvent> = when (command) {
        is PutTile -> afterTilePlaced(state.recentPosition, state)
        is PutPiece -> afterPiecePlaced(state, command.piece, command.role)
        else -> emptySet()
    }

    private fun afterTilePlaced(position: Position, state: State): Collection<GameEvent> {
        return listOf(Up, Right, Down, Left)
            .map { explore(state, position, it) }
            .filter { it.completed }
            .distinctBy { it.pieces }
            .filter { it.pieces.isNotEmpty() }
            .flatMap { road: Road ->
                val (winners, losers) = WinnerSelector.find(road.pieces)
                val score = scoring.score(state, road)
                winners.ids.map { playerId -> road.createPlayerScoredEvent(playerId, score) } +
                    losers.ids.map { playerId -> road.createOccupiedAreaCompletedEvent(playerId) }
            }
    }

    private fun afterPiecePlaced(state: State, piece: Piece, role: Role): Collection<GameEvent> {
        if (role !is Brigand) {
            return emptySet()
        }
        val road =
            explore(state, state.recentPosition, role.direction)
        if (!road.completed) {
            return emptyList()
        }
        val processedRoad =
            ProcessedRoad(road.tilesCount, state.currentPlayerId(), 1)
        return setOf(
            PlayerScored(processedRoad.playerId,
                score(processedRoad), (1..processedRoad.piecesCount).map { piece })
        )
    }

    private fun score(road: ProcessedRoad) = road.tilesCount

    private fun explore(state: State, startingPosition: Position, startingDirection: Direction): Road {
        val exploredRoad = RoadExplorer(state, PositionedDirection(startingPosition, startingDirection))
        exploredRoad.explore()
        return Road.from(state, exploredRoad)
    }

}
