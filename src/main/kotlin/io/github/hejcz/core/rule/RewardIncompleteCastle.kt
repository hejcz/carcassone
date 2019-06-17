package io.github.hejcz.core.rule

import io.github.hejcz.core.*

class RewardIncompleteCastle(private val scoring: CastleScoring) : EndRule {

    override fun apply(state: State) = state.allKnights()
        .map { (_, piece) -> testCastle(state, piece.position, (piece.role as Knight).direction) }
        .distinct()
        .flatMap { castle ->
            when (val score = scoring(state, castle)) {
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
        val (positionsToDirections, isCompleted) = CastlesExplorer.explore(state, startingPosition, startingDirection)
        return Castle.from(state, positionsToDirections, isCompleted)
    }
}
