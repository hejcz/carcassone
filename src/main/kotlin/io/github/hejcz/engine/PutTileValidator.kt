package io.github.hejcz.engine

import io.github.hejcz.*
import io.github.hejcz.placement.Down
import io.github.hejcz.placement.Left
import io.github.hejcz.placement.Right
import io.github.hejcz.placement.Up
import io.github.hejcz.tiles.basic.NoTile

object PutTileValidator : CommandValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> {
        when (command) {
            is PutTile -> {
                val isValid = setOf(Up, Down, Left, Right).all { direction ->
                    when (val tile = state.tileAt(direction.move(command.position))) {
                        is NoTile -> true
                        else -> tile.isValidNeighborFor(state.currentTile.rotate(command.rotation), direction.opposite())
                    }
                }
                return if (isValid) emptySet() else setOf(TilePlacedInInvalidPlace)
            }
            else -> return emptySet()
        }
    }
}