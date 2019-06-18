package io.github.hejcz.river

import io.github.hejcz.core.*
import io.github.hejcz.river.tiles.*

object PutRiverTileValidator : CommandValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> =
        when {
            command is PutTile && state.currentTile is RiverTile -> when {
                state.recentTile !is RiverTile -> setOf(InvalidTileLocation)
                else -> {
                    val recentTileDirection: Direction = state.recentPosition.relativeDirectionTo(command.position)
                    val currentTile = state.currentTile.rotate(command.rotation) as RiverTile
                    val recentTile = state.recentTile as RiverTile
                    val recentTileRiver = recentTile.exploreRiver()
                    val currentTileRiver = currentTile.exploreRiver()
                    when {
                        // is not extending river but is adjacent e.g. by green field
                        recentTileDirection.opposite() !in recentTileRiver -> setOf(InvalidTileLocation)
                        riverTurnsInSameDirection(recentTileDirection, recentTileRiver, currentTileRiver) -> setOf(
                            InvalidTileLocation
                        )
                        else -> emptySet()
                    }
                }
            }
            else -> emptySet()
        }

    private fun riverTurnsInSameDirection(
        recentTileDirection: Direction,
        recentRiver: Directions,
        currentRiver: Directions
    ): Boolean =
        recentTileDirection.opposite().left() in recentRiver && recentTileDirection.right() in currentRiver ||
            recentTileDirection.opposite().right() in recentRiver && recentTileDirection.left() in currentRiver
}
