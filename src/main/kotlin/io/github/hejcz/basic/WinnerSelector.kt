package io.github.hejcz.basic

import io.github.hejcz.helpers.*

inline class Winners(val ids: Collection<Long> = emptySet())

inline class Losers(val ids: Collection<Long> = emptySet())

object WinnerSelector {

    fun find(pieces: Collection<PieceOnObject>): Pair<Winners, Losers> = when {
        pieces.isEmpty() -> Pair(Winners(), Losers())
        else -> findWinner(pieces)
    }

    private fun findWinner(pieces: Collection<PieceOnObject>): Pair<Winners, Losers> {
        val totalScoreByPlayer = pieces.groupBy { piece -> piece.playerId }
            .mapValues { (_, pieces) -> pieces.sumBy { it.power() } }
        val maxScore = totalScoreByPlayer.maxBy { (_, totalScore) -> totalScore }!!.value
        return totalScoreByPlayer.entries.partition { (_, totalScore) -> totalScore == maxScore }
            .let { (winners, loser) -> Pair(Winners(winners.map { it.key }), Losers(loser.map { it.key })) }
    }

}
