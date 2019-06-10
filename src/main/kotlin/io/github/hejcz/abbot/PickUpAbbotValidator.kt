package io.github.hejcz.abbot

import io.github.hejcz.core.*

object PickUpAbbotValidator : CommandValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> =
        when {
            command is PickUpAbbot && state.piecesOnPosition(command.position).none { it.piece is AbbotPiece } ->
                setOf(NoAbbotToPickUp)
            else -> emptySet()
        }
}
