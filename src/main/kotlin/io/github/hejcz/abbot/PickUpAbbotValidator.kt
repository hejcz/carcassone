package io.github.hejcz.abbot

import io.github.hejcz.core.*

object PickUpAbbotValidator : CommandValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> =
        when {
            command is PickUpAbbot && state.findPiece(command.position, Abbot) == null ->
                setOf(NoAbbotToPickUp)
            else -> emptySet()
        }
}
