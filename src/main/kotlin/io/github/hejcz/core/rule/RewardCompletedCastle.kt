package io.github.hejcz.core.rule

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*

class RewardCompletedCastle(private val castleScoring: CastleScoring) : Rule {

    override fun afterCommand(command: Command, state: State): Collection<GameEvent> = when (command) {
        is PutTile -> afterTilePlaced(state)
        is PutPiece -> afterPiecePlaced(state, command.role)
        else -> emptySet()
    }

    private fun afterTilePlaced(state: State): Collection<GameEvent> =
        castlesDirections(state.tileAt(state.recentPosition()))
            .asSequence()
            .map { explore(state, state.recentPosition(), it) }
            .distinct()
            .filter { it.completed }
            .toList()
            .flatMap { castle ->
                setOf(CastleFinished(CompletedCastle(castle.parts))) + when {
                    castle.pieces.isNotEmpty() -> generateEvents(castle, state)
                    else -> emptySet()
                }
            }

    private fun castlesDirections(tile: Tile) = listOf(Up, Right, Down, Left)
        .flatMap { tile.exploreCastle(it) }
        .distinct()

    private fun generateEvents(castle: Castle, state: State): List<GameEvent> {
        val (winners, losers) = WinnerSelector.find(castle.pieces)
        val score = castleScoring(state, castle)
        return winners.ids.map { id -> PlayerScored(id, score, castle.piecesOf(id)) } +
                losers.ids.map { id -> PlayerDidNotScore(id, castle.piecesOf(id)) }
    }

    private fun afterPiecePlaced(state: State, role: Role): Collection<GameEvent> {
        if (role !is Knight) {
            return emptySet()
        }
        val castle = explore(state, state.recentPosition(), role.direction)
        if (!castle.completed) {
            return emptyList()
        }
        val score = castleScoring(state, castle)
        // if castle is finished and player is allowed to put piece inside then this is the only one piece on castle
        return setOf(PlayerScored(state.currentPlayerId(), score, castle.piecesOf(state.currentPlayerId())))
    }

    private fun explore(state: State, startingPosition: Position, startingDirection: Direction): Castle {
        val (positionsToDirections, isCompleted) = CastlesExplorer.explore(state, startingPosition, startingDirection)
        return Castle.from(state, positionsToDirections, isCompleted)
    }
}
