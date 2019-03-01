package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.helpers.*

class RewardCompletedRoad(private val scoringStrategy: RoadScoringStrategy) : Rule {

    override fun afterCommand(command: Command, state: State): Collection<GameEvent> = when (command) {
        is PutTile -> afterTilePlaced(state.recentPosition, state)
        is PutPiece -> afterPiecePlaced(state, command.piece, command.pieceRole)
        else -> emptySet()
    }

    private fun afterTilePlaced(position: Position, state: State): Collection<GameEvent> {
        return listOf(Up, Right, Down, Left)
            .map { explore(state, position, it) }
            .filter { it.completed }
            .distinctBy { it.pieces }
            .filter { it.pieces.isNotEmpty() }
            .flatMap { road: ExploredRoad ->
                val (winners, losers) = WinnerSelector.find(road.pieces)
                val score = scoringStrategy.score(state, road)
                winners.map { playerId -> road.createPlayerScoredEvent(playerId, score) } +
                    losers.map { playerId -> road.createOccupiedAreaCompletedEvent(playerId) }
            }
    }

    private fun afterPiecePlaced(state: State, piece: Piece, pieceRole: PieceRole): Collection<GameEvent> {
        if (pieceRole !is Brigand) {
            return emptySet()
        }
        val road =
            explore(state, state.recentPosition, pieceRole.direction)
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

    private fun explore(state: State, startingPosition: Position, startingDirection: Direction): ExploredRoad {
        val exploredRoad = RoadExplorer(state, PositionedDirection(startingPosition, startingDirection))
        exploredRoad.explore()
        return ExploredRoad.from(state, exploredRoad)
    }

}
