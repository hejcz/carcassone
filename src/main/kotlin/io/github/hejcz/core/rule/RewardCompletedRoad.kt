package io.github.hejcz.core.rule

import io.github.hejcz.core.*

class RewardCompletedRoad(private val scoring: RoadScoring) : Rule {

    override fun afterCommand(command: Command, state: State): Collection<GameEvent> = when (command) {
        is PutTile -> afterTilePlaced(state.recentPosition, state)
        is PutPiece -> afterPiecePlaced(state, command.role)
        else -> emptySet()
    }

    private fun afterTilePlaced(position: Position, state: State): Collection<GameEvent> =
        listOf(Up, Right, Down, Left)
            .map { explore(state, position, it) }
            .filter { it.completed }
            .distinct()
            .filter { it.pieces.isNotEmpty() }
            .flatMap { generateEvents(it, state) }

    private fun generateEvents(road: Road, state: State): List<GameEvent> {
        val (winners, losers) = WinnerSelector.find(road.pieces)
        val score = scoring(state, road)
        returnPieces(state, road)
        return winners.ids.map { id -> road.createPlayerScoredEvent(id, score) } +
                losers.ids.map { id -> road.createOccupiedAreaCompletedEvent(id) }
    }

    private fun afterPiecePlaced(state: State, role: Role): Collection<GameEvent> {
        if (role !is Brigand) {
            return emptySet()
        }
        val road = explore(state, state.recentPosition, role.direction)
        if (!road.completed) {
            return emptyList()
        }
        // if road is finished and player could put piece then this is the only one piece on this road
        val returnedPieces = returnPieces(state, road)
        return setOf(PlayerScored(state.currentPlayerId(), scoring(state, road), returnedPieces.mapTo(mutableSetOf()) { it.pieceOnBoard }))
    }

    private fun returnPieces(state: State, road: Road) =
        state.returnPieces(road.pieces.map { it.toPieceWithOwner() })

    private fun explore(state: State, startingPosition: Position, startingDirection: Direction): Road {
        val (parts, isCompleted) = RoadsExplorer.explore(state, startingPosition, startingDirection)
        return Road.from(state, parts, isCompleted)
    }
}
