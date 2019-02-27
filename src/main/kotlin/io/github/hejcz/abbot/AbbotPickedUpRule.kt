package io.github.hejcz.abbot

import io.github.hejcz.basic.tiles.NoTile
import io.github.hejcz.core.*

object AbbotPickedUpRule : Rule {

    override fun afterCommand(command: Command, state: State): Collection<GameEvent> = when (command) {
        is PickUpAbbot -> afterAbbotPicked(state, command.position)
        else -> emptySet()
    }

    private fun afterAbbotPicked(state: State, position: Position): Collection<GameEvent> =
        state.players.map { player -> Pair(player, player.pieceOn(position, Abbot)) }
            .filter { (_, piece) -> piece != null }
            .map { (player, piece) -> PlayerScored(player.id, score(state, piece!!.position), setOf(AbbotPiece)) }

    private fun score(state: State, cloisterPosition: Position): Int =
        1 + cloisterPosition.surrounding().count { state.tileAt(it) !is NoTile }
}
