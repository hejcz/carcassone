package io.github.hejcz.basic.rule

import io.github.hejcz.basic.*
import io.github.hejcz.core.*
import io.github.hejcz.helpers.*

class RewardIncompleteCastle(private val castleScoring: CastleScoring) : EndRule {

    override fun apply(state: State) = state.filterPieces { it is Knight }
        .map { testCastle(state, it.position, (it.role as Knight).direction) }
        .distinctBy { it.elements }
        .flatMap { castle ->
            when (val score = castleScoring.score(state, castle)) {
                0 -> emptyList()
                else -> {
                    val (winners, _) = WinnerSelector.find(castle.pieces)
                    winners.ids.map { id -> PlayerScored(id, score, emptySet()) }
                }
            }
        }

    private fun testCastle(state: State, startingPosition: Position, startingDirection: Direction): Castle {
        if (startingDirection !in state.tileAt(startingPosition).exploreCastle(startingDirection)) {
            return Castle.empty()
        }
        val castle = CastleExplorer(state, PositionedDirection(startingPosition, startingDirection))
        castle.explore()
        return Castle.from(state, castle)
    }
}
