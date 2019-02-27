package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.helpers.CastleExplorer
import io.github.hejcz.helpers.ProcessedCastle
import io.github.hejcz.helpers.TestedCastle
import io.github.hejcz.helpers.filterPieces

object IncompleteCastleRule : EndRule {

    override fun apply(state: State): Collection<GameEvent> {
        val importantFacts = state.filterPieces { it is Knight }
            .map {
                testCastle(
                    state,
                    it.position,
                    (it.role as Knight).direction
                )
            }
            .distinct()
            .flatMap {
                it.pieces.groupBy { piece -> piece.playerId }
                    .mapValues { k -> k.value.size }
                    .map { e -> ProcessedCastle(it.completed, it.tilesCount, e.key, e.value, it.emblems) }
            }
        if (importantFacts.isEmpty()) {
            return emptySet()
        }
        val maxPiecesCount = importantFacts.maxBy { it.piecesCount }?.piecesCount!!
        return importantFacts.filter { it.piecesCount >= maxPiecesCount }
            .map { PlayerScored(it.playerId, score = it.tilesCount + it.emblems, returnedPieces = emptySet()) }
    }

    private fun testCastle(state: State, startingPosition: Position, startingDirection: Direction): TestedCastle {
        if (!state.tileAt(startingPosition).exploreCastle(startingDirection).contains(startingDirection)) {
            return TestedCastle.empty()
        }
        val exploredCastle = CastleExplorer(state, PositionedDirection(startingPosition, startingDirection))
        exploredCastle.explore()
        return TestedCastle.from(state, exploredCastle)
    }

}
