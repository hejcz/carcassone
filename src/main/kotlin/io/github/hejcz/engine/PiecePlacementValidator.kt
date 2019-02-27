package io.github.hejcz.engine

import io.github.hejcz.Command
import io.github.hejcz.GameEvent
import io.github.hejcz.PiecePlacedInInvalidPlace
import io.github.hejcz.PutPiece
import io.github.hejcz.mapples.Brigand
import io.github.hejcz.mapples.Knight
import io.github.hejcz.mapples.Monk
import io.github.hejcz.mapples.Peasant

object PiecePlacementValidator : CommandValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> {
        return when (command) {
            is PutPiece -> {
                val role = command.pieceRole
                val tile = state.recentTile
                val isValid: Boolean = when (role) {
                    is Knight -> tile.exploreCastle(role.direction).contains(role.direction)
                    is Brigand -> tile.exploreRoad(role.direction).contains(role.direction)
                    is Peasant -> tile.exploreGreenFields(role.location).contains(role.location)
                    is Monk -> tile.hasCloister()
                }
                if (isValid) emptySet() else setOf(PiecePlacedInInvalidPlace)
            }
            else -> emptySet()
        }
    }
}