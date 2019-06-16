package io.github.hejcz.core.rule

import io.github.hejcz.core.*

class RewardIncompleteRoad(private val scoring: RoadScoring) : EndRule {

    override fun apply(state: State): Collection<GameEvent> {
        return state.filterPieces { it is Brigand }
            .map { explore(state, it.position, (it.role as Brigand).direction) }
            .distinct()
            .flatMap { road: Road ->
                val (winners, _) = WinnerSelector.find(road.pieces)
                val score = scoring(state, road)
                when (score) {
                    0 -> emptyList()
                    else -> winners.ids.map { playerId -> road.createPlayerScoredEventWithoutPieces(playerId, score) }
                }
            }
    }

    private fun explore(state: State, startingPosition: Position, startingDirection: Direction): Road {
        if (startingDirection !in state.tileAt(startingPosition).exploreRoad(startingDirection)) {
            return Road.empty()
        }
        val (parts, isCompleted) = RoadsExplorer.explore(state, startingPosition, startingDirection)
        return Road.from(state, parts, isCompleted)
    }
}
