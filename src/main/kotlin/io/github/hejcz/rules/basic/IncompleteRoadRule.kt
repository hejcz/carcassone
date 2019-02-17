package io.github.hejcz.rules.basic

import io.github.hejcz.*
import io.github.hejcz.engine.State
import io.github.hejcz.mapples.Brigand
import io.github.hejcz.placement.Direction
import io.github.hejcz.placement.Position
import io.github.hejcz.placement.PositionedDirection
import io.github.hejcz.rules.EndRule
import io.github.hejcz.rules.helpers.*

object IncompleteRoadRule : EndRule {

    override fun apply(state: State): Collection<GameEvent> {
        val importantFacts = state.filterPieces { it is Brigand }
            .map {
                explore(
                    state,
                    it.position,
                    (it.role as Brigand).direction
                )
            }
            .distinct()
            .flatMap {
                it.pieces.groupBy { piece -> piece.playerId }
                    .mapValues { k -> k.value.size }
                    .map { e -> ProcessedRoad(it.completed, it.tilesCount, e.key, e.value) }
            }
        if (importantFacts.isEmpty()) {
            return emptySet()
        }
        val maxPiecesCount = importantFacts.maxBy { it.piecesCount }?.piecesCount!!
        val (_, winners) = importantFacts.partition { it.piecesCount < maxPiecesCount }
        return winners.map { PlayerScored(it.playerId, it.tilesCount, emptySet()) }
    }

    private fun explore(state: State, startingPosition: Position, startingDirection: Direction): TestedRoad {
        if (!state.tileAt(startingPosition).exploreRoad(startingDirection).contains(startingDirection)) {
            return TestedRoad.empty()
        }
        val exploredCastle = RoadExplorer(state, PositionedDirection(startingPosition, startingDirection))
        exploredCastle.explore()
        return TestedRoad.from(state, exploredCastle)
    }

}