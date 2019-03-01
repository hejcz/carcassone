package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.helpers.*

class RewardIncompleteCastles(private val castleScoringStrategy: IncompleteCastleScoringStrategy) : EndRule {

    override fun apply(state: State) = state.filterPieces { it is Knight }
        .map { testCastle(state, it.position, (it.role as Knight).direction) }
        .distinctBy { it.elements }
        .flatMap { exploredCastle ->
            val score = castleScoringStrategy.score(state, exploredCastle)
            when (score) {
                0 -> emptyList()
                else -> {
                    val (winners, _) = WinnerSelector.find(exploredCastle.pieces)
                    winners.map { id -> PlayerScored(id, score, emptySet()) }
                }
            }
        }

    private fun testCastle(state: State, startingPosition: Position, startingDirection: Direction): ExploredCastle {
        if (startingDirection !in state.tileAt(startingPosition).exploreCastle(startingDirection)) {
            return ExploredCastle.empty()
        }
        val exploredCastle = CastleExplorer(state, PositionedDirection(startingPosition, startingDirection))
        exploredCastle.explore()
        return ExploredCastle.from(state, exploredCastle)
    }

}
