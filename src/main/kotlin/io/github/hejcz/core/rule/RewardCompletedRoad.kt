package io.github.hejcz.core.rule

import io.github.hejcz.core.*
import io.github.hejcz.expansion.magic.MoveMageOrWitchCmd

class RewardCompletedRoad(private val scoring: RoadScoring) : Rule {

    override fun afterCommand(command: Command, state: State): Collection<GameEvent> = when (command) {
        is PieceCmd -> afterTilePlaced(state) + afterPiecePlaced(state, command.role)
        is SkipPieceCmd -> afterTilePlaced(state)
        else -> emptySet()
    }.distinct()

    private fun afterTilePlaced(state: State): Collection<GameEvent> =
        state.getNewCompletedRoads().flatMap { road ->
            when {
                road.pieces().isNotEmpty() -> generateEvents(road, state)
                else -> emptySet<GameEvent>()
            }
        }

    private fun generateEvents(road: Road, state: State): List<GameEvent> {
        val (winners, losers) = WinnerSelector.find(road.pieces())
        val score = scoring(state, road)
        return winners.ids.map { id -> road.createPlayerScoredEvent(id, score) } +
            losers.ids.map { id -> road.createOccupiedAreaCompletedEvent(id) }
    }

    private fun afterPiecePlaced(state: State, role: Role): Collection<GameEvent> {
        if (role !is Brigand) {
            return emptySet()
        }
        return state.getNewCompletedRoads()
            .find { it.parts.contains(PositionedDirection(state.recentPosition(), role.direction)) }
            // if road is finished and player could put piece then this is the only one piece on this road
            ?.let { setOf(ScoreEvent(state.currentPlayerId(), scoring(state, it), it.piecesOf(state.currentPlayerId()))) }
            ?: emptySet()
    }
}
