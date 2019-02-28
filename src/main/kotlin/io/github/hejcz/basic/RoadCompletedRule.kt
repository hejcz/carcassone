package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.helpers.ProcessedRoad
import io.github.hejcz.helpers.RoadExplorer
import io.github.hejcz.helpers.TestedRoad

object RoadCompletedRule : Rule {

    override fun afterCommand(command: Command, state: State): Collection<GameEvent> = when (command) {
        is PutTile -> afterTilePlaced(state.recentPosition, state)
        is PutPiece -> afterPiecePlaced(state, command.piece, command.pieceRole)
        else -> emptySet()
    }

    private fun afterTilePlaced(position: Position, state: State): Collection<GameEvent> {
        val importantFacts = listOf(Up, Right, Down, Left)
            .map { explore(state, position, it) }
            .filter { it.completed }
            .filter { it.pieces.isNotEmpty() }
            .flatMap {
                it.pieces.groupBy { piece -> piece.playerId }
                    .mapValues { k -> k.value.size }
                    .map { e -> ProcessedRoad(it.completed, it.tilesCount, e.key, e.value) }
            }
        if (importantFacts.isEmpty()) {
            return emptySet()
        }
        val maxPiecesCount = importantFacts.maxBy { it.piecesCount }?.piecesCount!!
        val (losers, winners) = importantFacts.partition { it.piecesCount < maxPiecesCount }
        return winners.map { PlayerScored(it.playerId, score(it), (1..it.piecesCount).map<Int, Piece> { SmallPiece }) } +
            losers.map { OccupiedAreaCompleted(it.playerId, (1..it.piecesCount).map<Int, Piece> { SmallPiece }) }
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
            ProcessedRoad(road.completed, road.tilesCount, state.currentPlayerId(), 1)
        return setOf(
            PlayerScored(processedRoad.playerId,
                score(processedRoad), (1..processedRoad.piecesCount).map { piece })
        )
    }

    private fun score(road: ProcessedRoad) = road.tilesCount

    private fun explore(state: State, startingPosition: Position, startingDirection: Direction): TestedRoad {
        if (!state.tileAt(startingPosition).exploreRoad(startingDirection).contains(startingDirection)) {
            return TestedRoad.empty()
        }
        val exploredCastle = RoadExplorer(state, PositionedDirection(startingPosition, startingDirection))
        exploredCastle.explore()
        return TestedRoad.from(state, exploredCastle)
    }

}
