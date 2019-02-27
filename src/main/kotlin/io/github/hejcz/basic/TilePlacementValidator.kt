package io.github.hejcz.basic

import io.github.hejcz.basic.tiles.NoTile
import io.github.hejcz.core.*

object TilePlacementValidator : CommandValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> {
        when (command) {
            is PutTile -> {
                val isValid = setOf(Up, Down, Left, Right).all { direction ->
                    when (val tile = state.tileAt(direction.move(command.position))) {
                        is NoTile -> true
                        else -> tile.isValidNeighborFor(
                            state.currentTile.rotate(command.rotation),
                            direction.opposite()
                        )
                    }
                }
                return if (isValid) emptySet() else setOf(TilePlacedInInvalidPlace)
            }
            else -> return emptySet()
        }
    }
}
