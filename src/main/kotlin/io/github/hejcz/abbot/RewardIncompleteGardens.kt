package io.github.hejcz.abbot

import io.github.hejcz.basic.tiles.*
import io.github.hejcz.core.*

object RewardIncompleteGardens : EndRule {
    override fun apply(state: State): Collection<GameEvent> =
        state.players.flatMap { player -> player.pieces().map { piece -> Pair(player.id, piece) } }
            .filter { (_, piece) -> piece.role is Abbot }
            .map { (playerId, piece) ->
                PlayerScored(
                    playerId,
                    score(state, piece.position), emptySet()
                )
            }

    private fun score(state: State, cloisterPosition: Position): Int =
        1 + cloisterPosition.surrounding().count { state.tileAt(it) !is NoTile }
}
