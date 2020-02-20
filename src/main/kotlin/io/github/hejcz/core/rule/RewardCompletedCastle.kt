package io.github.hejcz.core.rule

import io.github.hejcz.core.*
import io.github.hejcz.engine.CastleScoring

class RewardCompletedCastle(private val castleScoring: CastleScoring) : Rule {

    override fun afterCommand(command: Command, state: State): Collection<GameEvent> = when (command) {
        is PieceCmd -> afterTilePlaced(state) + afterPiecePlaced(state, command.role)
        is SkipPieceCmd -> afterTilePlaced(state)
        else -> emptySet()
    }.distinct()

    private fun afterTilePlaced(state: State): Collection<GameEvent> =
        state.getNewCompletedCastles().flatMap {
            when {
                it.pieces().isNotEmpty() -> generateEvents(it, state)
                else -> emptySet<GameEvent>()
            }
        }

    private fun generateEvents(castle: Castle, state: State): List<GameEvent> {
        val (winners, losers) = WinnerSelector.find(castle.pieces())
        val score = castleScoring(state, castle)
        return winners.ids.map { id -> ScoreEvent(id, score, castle.piecesOf(id)) } +
            losers.ids.map { id -> NoScoreEvent(id, castle.piecesOf(id)) }
    }

    private fun afterPiecePlaced(state: State, role: Role): Collection<GameEvent> {
        if (role !is Knight) {
            return emptySet()
        }
        return state.getNewCompletedCastles()
            .find { it.parts.contains(PositionedDirection(state.recentPosition(), role.direction)) }
            // if castle is finished and player is allowed to put piece inside then this is the only one piece on castle
            ?.let { setOf(ScoreEvent(state.currentPlayerId(), castleScoring(state, it), it.piecesOf(state.currentPlayerId()))) }
            ?: emptySet()
    }
}
