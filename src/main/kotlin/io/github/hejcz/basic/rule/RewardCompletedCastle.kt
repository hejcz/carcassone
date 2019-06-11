package io.github.hejcz.basic.rule

import io.github.hejcz.basic.*
import io.github.hejcz.core.*
import io.github.hejcz.helpers.*

class RewardCompletedCastle(private val castleScoring: CastleScoring) : Rule {

    override fun afterCommand(command: Command, state: State): Collection<GameEvent> = when (command) {
        is PutTile -> afterTilePlaced(state)
        is PutPiece -> afterPiecePlaced(state, command.piece, command.role)
        else -> emptySet()
    }

    private fun afterTilePlaced(state: State): Collection<GameEvent> =
        state.castlesDirections(state.recentPosition)
            .asSequence()
            .map { explore(state, state.recentPosition, it) }
            .distinctBy { it.elements }
            .filter { it.completed }
            .onEach { state.addCompletedCastle(CompletedCastle(it.elements)) }
            .filter { it.pieces.isNotEmpty() }
            .toList()
            .flatMap { generateEvents(it, state) }

    private fun generateEvents(castle: Castle, state: State): List<GameEvent> {
        val (winners, losers) = WinnerSelector.find(castle.pieces)
        val score = castleScoring.score(state, castle)
        return winners.ids.map { id -> PlayerScored(id, score, castle.piecesOf(id)) } +
            losers.ids.map { id -> OccupiedAreaCompleted(id, castle.piecesOf(id)) }
    }

    private fun State.castlesDirections(position: Position) = listOf(Up, Right, Down, Left)
        .flatMap { this.tileAt(position).exploreCastle(it) }
        .distinct()

    private fun afterPiecePlaced(state: State, piece: Piece, role: Role): Collection<GameEvent> {
        if (role !is Knight) {
            return emptySet()
        }
        val castle = explore(state, state.recentPosition, role.direction)
        if (!castle.completed) {
            return emptyList()
        }
        val processedCastle =
            ProcessedCastle(castle.completed, castle.tilesCount, state.currentPlayerId(), 1, castle.emblems)
        val score = score(processedCastle)
        // if castle is finished and player could put piece then this is the only one piece on castle
        return setOf(PlayerScored(processedCastle.playerId, score, setOf(PieceOnBoard(state.recentPosition, piece, role))))
    }

    private fun score(processedCastle: ProcessedCastle) = (processedCastle.tilesCount + processedCastle.emblems) * 2

    private fun explore(state: State, startingPosition: Position, startingDirection: Direction): Castle {
        val castle = CastleExplorer(state, PositionedDirection(startingPosition, startingDirection))
        castle.explore()
        return Castle.from(state, castle)
    }
}