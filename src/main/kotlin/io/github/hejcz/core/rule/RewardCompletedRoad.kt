package io.github.hejcz.core.rule

import io.github.hejcz.core.*
import io.github.hejcz.expansion.magic.MoveMagicianOrWitchCmd

class RewardCompletedRoad(private val scoring: RoadScoring) : Rule {

    override fun afterCommand(command: Command, state: State): Collection<GameEvent> = when (command) {
        is TileCmd -> afterTilePlaced(state)
        is PieceCmd -> afterPiecePlaced(state, command.role)
        is MoveMagicianOrWitchCmd -> afterTilePlaced(state)
        else -> emptySet()
    }

    private fun afterTilePlaced(state: State): Collection<GameEvent> =
        listOf(Up, Right, Down, Left)
            .map { explore(state, state.recentPosition(), it) }
            .filter { it.completed }
            .filter { it.parts.isNotEmpty() }
            .distinct()
            .flatMap { road ->
                setOf(RoadClosedEvent(CompletedRoad(road.parts))) + when {
                    road.pieces.isNotEmpty() -> generateEvents(road, state)
                    else -> emptySet()
                }
            }

    private fun generateEvents(road: Road, state: State): List<GameEvent> {
        val (winners, losers) = WinnerSelector.find(road.pieces)
        val score = scoring(state, road)
        return winners.ids.map { id -> road.createPlayerScoredEvent(id, score) } +
            losers.ids.map { id -> road.createOccupiedAreaCompletedEvent(id) }
    }

    private fun afterPiecePlaced(state: State, role: Role): Collection<GameEvent> {
        if (role !is Brigand) {
            return emptySet()
        }
        val road = explore(state, state.recentPosition(), role.direction)
        if (!road.completed) {
            return emptyList()
        }
        // if road is finished and player could put piece then this is the only one piece on this road
        return setOf(
            ScoreEvent(
                state.currentPlayerId(), scoring(state, road), road.piecesOf(state.currentPlayerId())
            )
        )
    }

    private fun explore(state: State, startingPosition: Position, startingDirection: Direction): Road {
        val (parts, isCompleted) = RoadsExplorer.explore(state, startingPosition, startingDirection)
        return Road.from(state, parts, isCompleted)
    }
}
