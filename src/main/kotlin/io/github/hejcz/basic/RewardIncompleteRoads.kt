package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.helpers.*

class RewardIncompleteRoads(private val scoringStrategy: RoadScoringStrategy) : EndRule {

    override fun apply(state: State): Collection<GameEvent> {
        return state.filterPieces { it is Brigand }
            .map { explore(state, it.position, (it.role as Brigand).direction) }
            .distinct()
            .flatMap { road: ExploredRoad ->
                val (winners, _) = WinnerSelector.find(road.pieces)
                val score = scoringStrategy.score(state, road)
                when (score) {
                    0 -> emptyList()
                    else -> winners.map { playerId -> road.createPlayerScoredEventWithoutPieces(playerId, score) }
                }
            }
    }

    private fun explore(state: State, startingPosition: Position, startingDirection: Direction): ExploredRoad {
        if (startingDirection !in state.tileAt(startingPosition).exploreRoad(startingDirection)) {
            return ExploredRoad.empty()
        }
        val exploredCastle = RoadExplorer(state, PositionedDirection(startingPosition, startingDirection))
        exploredCastle.explore()
        return ExploredRoad.from(state, exploredCastle)
    }

}
