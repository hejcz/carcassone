package io.github.hejcz.expansion.abbot

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*

object RewardIncompleteGardens : EndRule {
    override fun apply(state: State): Collection<GameEvent> =
        state.all(Abbot::class)
            .map { (playerId, piece) -> ScoreEvent(playerId, score(state, piece.position), emptySet()) }

    private fun score(state: State, cloisterPosition: Position): Int =
        1 + cloisterPosition.surrounding().count { state.tileAt(it) !is NoTile }
}
