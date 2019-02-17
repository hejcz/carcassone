package io.github.hejcz.rules.basic

import io.github.hejcz.*
import io.github.hejcz.engine.State
import io.github.hejcz.mapples.Brigand
import io.github.hejcz.mapples.Mapple
import io.github.hejcz.mapples.PieceId
import io.github.hejcz.mapples.PieceRole
import io.github.hejcz.placement.*
import io.github.hejcz.rules.Rule
import io.github.hejcz.rules.helpers.*

object RoadCompletedRule : Rule {

    override fun afterTilePlaced(position: Position, state: State): Collection<GameEvent> {
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
        return winners.map { PlayerScored(it.playerId, score(it), (1..it.piecesCount).map<Int, PieceId> { Mapple }) } +
                losers.map { OccupiedAreaCompleted(it.playerId, (1..it.piecesCount).map<Int, PieceId> { Mapple }) }
    }

    override fun afterPiecePlaced(state: State, pieceId: PieceId, pieceRole: PieceRole): Collection<GameEvent> {
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
        return setOf(PlayerScored(processedRoad.playerId,
            score(processedRoad), (1..processedRoad.piecesCount).map { pieceId }))
    }

    private fun score(road: ProcessedRoad) = road.tilesCount

    private fun explore(state: State, startingPosition: Position, startingDirection: Direction) : TestedRoad {
        if (!state.tileAt(startingPosition).exploreRoad(startingDirection).contains(startingDirection)) {
            return TestedRoad.empty()
        }
        val exploredCastle = RoadExplorer(state, PositionedDirection(startingPosition, startingDirection))
        exploredCastle.explore()
        return TestedRoad.from(state, exploredCastle)
    }

}
