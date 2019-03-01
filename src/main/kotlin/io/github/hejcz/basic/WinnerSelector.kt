package io.github.hejcz.basic

import io.github.hejcz.helpers.*

object WinnerSelector {

    fun find(pieces: Collection<PieceOnObject>): Pair<Collection<Long>, Collection<Long>> {
        if (pieces.isEmpty()) {
            return Pair(emptySet(), emptySet())
        }
        val scores = pieces.groupBy { piece -> piece.playerId }
            .mapValues { (_, pieces) -> pieces.sumBy { foundPiece -> foundPiece.piece.power() } }
        val maxScore = scores.maxBy { (_, score) -> score }!!.value
        return scores.entries.partition { (_, score) -> score == maxScore }
            .let { (winners, loser) -> Pair(winners.map { (playerId, _) -> playerId }, loser.map { (playerId, _) -> playerId }) }
    }

}
