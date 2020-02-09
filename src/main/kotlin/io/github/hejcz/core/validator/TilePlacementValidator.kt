package io.github.hejcz.core.validator

import io.github.hejcz.core.*
import io.github.hejcz.core.tile.*

object TilePlacementValidator : CommandValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> = when (command) {
        is PutTile -> {
            val isValid = setOf(Up, Down, Left, Right).all { direction ->
                when (val tile = state.tileAt(direction.move(command.position))) {
                    is NoTile -> true
                    else -> tile.isValidNeighborFor(
                        state.currentTile().rotate(command.rotation),
                        direction.opposite()
                    )
                }
            }
            if (isValid) emptySet() else setOf(InvalidTileLocation)
        }
        else -> emptySet()
    }
}
