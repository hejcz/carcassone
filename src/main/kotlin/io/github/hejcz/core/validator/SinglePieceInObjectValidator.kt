package io.github.hejcz.core.validator

import io.github.hejcz.core.*

object SinglePieceInObjectValidator : CmdValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> = when (command) {
        is PieceCmd -> when (val role = command.role) {
            is Knight -> {
                val (parts, _) = CastlesExplorer.explore(state, state.recentPosition(), role.direction)
                val pieceAlreadyPresentOnObject = parts
                    .any { part -> state.anyPlayerHasPiece(part.position, Knight(part.direction)) }
                if (pieceAlreadyPresentOnObject) setOf(InvalidPieceLocationEvent) else emptySet()
            }
            is Brigand -> {
                val (parts, _) = RoadsExplorer.explore(state, state.recentPosition(), role.direction)
                val pieceAlreadyPresentOnObject =
                    parts.any { part -> state.anyPlayerHasPiece(part.position, Brigand(part.direction)) }
                if (pieceAlreadyPresentOnObject) setOf(InvalidPieceLocationEvent) else emptySet()
            }
            is Peasant -> {
                val pieceAlreadyPresentOnObject =
                    GreenFieldsExplorer.explore(state, state.recentPosition(), role.location)
                        .any { (position, location) -> state.anyPlayerHasPiece(position, Peasant(location)) }
                if (pieceAlreadyPresentOnObject) setOf(InvalidPieceLocationEvent) else emptySet()
            }
            else -> emptySet()
        }
        else -> emptySet()
    }
}
