package io.github.hejcz.core.rule

import io.github.hejcz.core.*
import io.github.hejcz.engine.RoadScoring

class RewardIncompleteRoad(private val scoring: RoadScoring) : EndRule {

    override fun apply(state: State): Collection<GameEvent> {
        return state.all(Brigand::class)
            .mapNotNull { (_, piece) -> explore(state, piece.position, (piece.role as Brigand).direction) }
            .distinct()
            .flatMap { road: Road ->
                val (winners, _) = WinnerSelector.find(road.pieces())
                when (val score = scoring(state, road)) {
                    0 -> emptyList()
                    else -> winners.ids.map { playerId -> road.createPlayerScoredEventWithoutPieces(playerId, score) }
                }
            }
    }

    private fun explore(state: State, startingPosition: Position, startingDirection: Direction): Road? {
        if (startingDirection !in state.tileAt(startingPosition).exploreRoad(startingDirection)) {
            return null
        }
        val (parts, isCompleted) = RoadsExplorer.explore(state, startingPosition, startingDirection)
        return Road.from(state, parts, isCompleted)
    }
}
