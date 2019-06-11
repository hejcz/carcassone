package io.github.hejcz.basic.rule

import io.github.hejcz.basic.*
import io.github.hejcz.core.*
import io.github.hejcz.helpers.*

class RewardCompletedRoad(private val scoring: RoadScoring) : Rule {

    override fun afterCommand(command: Command, state: State): Collection<GameEvent> = when (command) {
        is PutTile -> afterTilePlaced(state.recentPosition, state)
        is PutPiece -> afterPiecePlaced(state, command.piece, command.role)
        else -> emptySet()
    }

    private fun afterTilePlaced(position: Position, state: State): Collection<GameEvent> =
        listOf(Up, Right, Down, Left)
            .map { explore(state, position, it) }
            .filter { it.completed }
            .distinctBy { it.pieces }
            .filter { it.pieces.isNotEmpty() }
            .flatMap { generateEvents(it, state) }

    private fun generateEvents(road: Road, state: State): List<GameEvent> {
        val (winners, losers) = WinnerSelector.find(road.pieces)
        val score = scoring.score(state, road)
        return winners.ids.map { id -> road.createPlayerScoredEvent(id, score) } +
                losers.ids.map { id -> road.createOccupiedAreaCompletedEvent(id) }
    }

    private fun afterPiecePlaced(state: State, piece: Piece, role: Role): Collection<GameEvent> {
        if (role !is Brigand) {
            return emptySet()
        }
        val road = explore(state, state.recentPosition, role.direction)
        if (!road.completed) {
            return emptyList()
        }
        val processedRoad =
            ProcessedRoad(road.tilesCount, state.currentPlayerId(), 1)
        // if road is finished and player could put piece then this is the only one piece on this road
        return setOf(PlayerScored(processedRoad.playerId, score(processedRoad), setOf(PieceOnBoard(state.recentPosition, piece, role))))
    }

    private fun score(road: ProcessedRoad) = road.tilesCount

    private fun explore(state: State, startingPosition: Position, startingDirection: Direction): Road {
        val road = RoadExplorer(state, PositionedDirection(startingPosition, startingDirection))
        road.explore()
        return Road.from(state, road)
    }
}
