package io.github.hejcz.rules.basic

import io.github.hejcz.*
import io.github.hejcz.engine.State
import io.github.hejcz.mapples.Monk
import io.github.hejcz.placement.Position
import io.github.hejcz.rules.EndRule
import io.github.hejcz.tiles.basic.NoTile

object IncompleteCloisterRule : EndRule {
    override fun apply(state: State): Collection<GameEvent> =
        state.players.flatMap { player -> player.pieces().map { piece -> Pair(player.id, piece) } }
            .filter { (_, piece) -> piece.role is Monk }
            .map { (playerId, piece) -> PlayerScored(playerId,
                score(state, piece.position), emptySet()) }

    private fun score(state: State, cloisterPosition: Position): Int =
        1 + cloisterPosition.surrounding().count { state.tileAt(it) !is NoTile }
}