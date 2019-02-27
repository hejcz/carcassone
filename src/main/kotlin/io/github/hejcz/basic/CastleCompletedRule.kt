package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.helpers.CastleExplorer
import io.github.hejcz.helpers.ProcessedCastle
import io.github.hejcz.helpers.TestedCastle

object CastleCompletedRule : Rule {

    override fun afterCommand(command: Command, state: State): Collection<GameEvent> = when (command) {
        is PutTile -> afterTilePlaced(state.recentPosition, state)
        is PutPiece -> afterPiecePlaced(state, command.piece, command.pieceRole)
        else -> emptySet()
    }

    private fun afterTilePlaced(position: Position, state: State): Collection<GameEvent> {
        val importantFacts = listOf(Up, Right, Down, Left)
            .map { explore(state, position, it) }
            .filter { it.completed }
            .onEach { state.addCompletedCastle(CompletedCastle(it.elements)) }
            .filter { it.pieces.isNotEmpty() }
            .flatMap {
                it.pieces.groupBy { piece -> piece.playerId }
                    .mapValues { k -> k.value.size }
                    .map { e -> ProcessedCastle(it.completed, it.tilesCount, e.key, e.value, it.emblems) }
            }
        if (importantFacts.isEmpty()) {
            return emptySet()
        }
        val maxPiecesCount = importantFacts.maxBy { it.piecesCount }?.piecesCount!!
        val (losers, winners) = importantFacts.partition { it.piecesCount < maxPiecesCount }
        return winners.map { PlayerScored(it.playerId, score(it), (1..it.piecesCount).map<Int, Piece> { SmallPiece }) } +
            losers.map { OccupiedAreaCompleted(it.playerId, (1..it.piecesCount).map<Int, Piece> { SmallPiece }) }
    }

    private fun afterPiecePlaced(state: State, piece: Piece, pieceRole: PieceRole): Collection<GameEvent> {
        if (pieceRole !is Knight) {
            return emptySet()
        }
        val castle =
            explore(state, state.recentPosition, pieceRole.direction)
        if (!castle.completed) {
            return emptyList()
        }
        val processedCastle =
            ProcessedCastle(castle.completed, castle.tilesCount, state.currentPlayerId(), 1, castle.emblems)
        val score = score(processedCastle)
        return setOf(PlayerScored(processedCastle.playerId, score, (1..processedCastle.piecesCount).map { piece }))
    }

    private fun score(processedCastle: ProcessedCastle) = (processedCastle.tilesCount + processedCastle.emblems) * 2

    private fun explore(state: State, startingPosition: Position, startingDirection: Direction): TestedCastle {
        if (!state.tileAt(startingPosition).exploreCastle(startingDirection).contains(startingDirection)) {
            return TestedCastle.empty()
        }
        val exploredCastle = CastleExplorer(state, PositionedDirection(startingPosition, startingDirection))
        exploredCastle.explore()
        return TestedCastle.from(state, exploredCastle)
    }

}

