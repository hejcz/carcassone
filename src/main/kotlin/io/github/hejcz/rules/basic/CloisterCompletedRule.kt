package io.github.hejcz.rules.basic

import io.github.hejcz.GameEvent
import io.github.hejcz.PlayerScored
import io.github.hejcz.engine.State
import io.github.hejcz.mapples.Mapple
import io.github.hejcz.mapples.Monk
import io.github.hejcz.mapples.PieceId
import io.github.hejcz.mapples.PieceRole
import io.github.hejcz.placement.Position
import io.github.hejcz.rules.Rule
import io.github.hejcz.tiles.basic.NoTile

object CloisterCompletedRule : Rule {
    override fun afterTilePlaced(position: Position, state: State): Collection<GameEvent> =
        position.surrounding()
            .filter { state.tileAt(it).hasCloister() }
            .filter { isSurrounded(state, it) }
            .mapNotNull { completedCloisterPosition ->
                state.players.firstOrNull { player ->
                    player.isPieceOn(
                        completedCloisterPosition,
                        Monk
                    )
                }
            }
            .map { player -> PlayerScored(player.id, 9, setOf(Mapple)) }

    override fun afterPiecePlaced(state: State, pieceId: PieceId, pieceRole: PieceRole): Collection<GameEvent> =
        when (pieceRole) {
            !is Monk -> emptySet()
            else -> when {
                isSurrounded(state, state.recentPosition) -> setOf(
                    PlayerScored(
                        state.currentPlayerId(),
                        9,
                        setOf(Mapple)
                    )
                )
                else -> emptySet()
            }
        }

    private fun isSurrounded(state: State, position: Position): Boolean =
        position.surrounding()
            .all { state.tileAt(it) !is NoTile }
}