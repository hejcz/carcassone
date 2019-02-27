package io.github.hejcz.rules.basic

import io.github.hejcz.GameEvent
import io.github.hejcz.OccupiedAreaCompleted
import io.github.hejcz.PlayerScored
import io.github.hejcz.engine.CompletedCastle
import io.github.hejcz.engine.State
import io.github.hejcz.mapples.Knight
import io.github.hejcz.mapples.Mapple
import io.github.hejcz.mapples.PieceId
import io.github.hejcz.mapples.PieceRole
import io.github.hejcz.placement.*
import io.github.hejcz.rules.Rule
import io.github.hejcz.rules.helpers.CastleExplorer
import io.github.hejcz.rules.helpers.ProcessedCastle
import io.github.hejcz.rules.helpers.TestedCastle

object CastleCompletedRule : Rule {

    override fun afterTilePlaced(position: Position, state: State): Collection<GameEvent> {
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
        return winners.map { PlayerScored(it.playerId, score(it), (1..it.piecesCount).map<Int, PieceId> { Mapple }) } +
            losers.map { OccupiedAreaCompleted(it.playerId, (1..it.piecesCount).map<Int, PieceId> { Mapple }) }
    }

    override fun afterPiecePlaced(state: State, pieceId: PieceId, pieceRole: PieceRole): Collection<GameEvent> {
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
        return setOf(PlayerScored(processedCastle.playerId, score, (1..processedCastle.piecesCount).map { pieceId }))
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

