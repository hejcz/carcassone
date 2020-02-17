package io.github.hejcz.expansion.abbot

import io.github.hejcz.core.*

object PickUpAbbotValidator : CommandValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> =
        when {
            command is PickUpAbbotCmd && state.findPieces(command.position, Abbot).isEmpty() -> setOf(CantPickUpAbbotEvent)
            else -> emptySet()
        }
}
