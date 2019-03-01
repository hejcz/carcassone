package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.helpers.*

class RewardCompletedCastle(private val castleScoringStrategy: CastleScoringStrategy) : Rule {

    override fun afterCommand(command: Command, state: State): Collection<GameEvent> = when (command) {
        is PutTile -> afterTilePlaced(state)
        is PutPiece -> afterPiecePlaced(state, command.piece, command.pieceRole)
        else -> emptySet()
    }

    private fun afterTilePlaced(state: State): Collection<GameEvent> {
        return state.castlesDirections(state.recentPosition)
            .asSequence()
            .map { explore(state, state.recentPosition, it) }
            .distinctBy { it.elements }
            .filter { it.completed }
            .onEach { state.addCompletedCastle(CompletedCastle(it.elements)) }
            .filter { it.pieces.isNotEmpty() }
            .toList()
            .flatMap { exploredCastle ->
                val (winners, losers) = WinnerSelector.find(exploredCastle.pieces)
                val score = castleScoringStrategy.score(state, exploredCastle)
                winners.map { id ->
                    PlayerScored(id, score, exploredCastle.pieces.filter { piece -> piece.playerId == id }.map { it.piece })} +
                        losers.map { id -> OccupiedAreaCompleted(id, exploredCastle.pieces.filter { piece -> piece.playerId == id }.map { it.piece })}
            }
    }

    private fun State.castlesDirections(position: Position) = listOf(Up, Right, Down, Left)
        .flatMap { this.tileAt(position).exploreCastle(it) }
        .distinct()

    private fun afterPiecePlaced(state: State, piece: Piece, pieceRole: PieceRole): Collection<GameEvent> {
        if (pieceRole !is Knight) {
            return emptySet()
        }
        val castle = explore(state, state.recentPosition, pieceRole.direction)
        if (!castle.completed) {
            return emptyList()
        }
        val processedCastle =
            ProcessedCastle(castle.completed, castle.tilesCount, state.currentPlayerId(), 1, castle.emblems)
        val score = score(processedCastle)
        return setOf(PlayerScored(processedCastle.playerId, score, (1..processedCastle.piecesCount).map { piece }))
    }

    private fun score(processedCastle: ProcessedCastle) = (processedCastle.tilesCount + processedCastle.emblems) * 2

    private fun explore(state: State, startingPosition: Position, startingDirection: Direction): ExploredCastle {
        val exploredCastle = CastleExplorer(state, PositionedDirection(startingPosition, startingDirection))
        exploredCastle.explore()
        return ExploredCastle.from(state, exploredCastle)
    }

}

