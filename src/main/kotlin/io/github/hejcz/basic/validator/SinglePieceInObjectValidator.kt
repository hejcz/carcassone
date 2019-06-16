package io.github.hejcz.basic.validator

import io.github.hejcz.core.*
import io.github.hejcz.helpers.*

object SinglePieceInObjectValidator : CommandValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> {
        return when (command) {
            is PutPiece -> {
                when (val role = command.role) {
                    is Knight -> {
                        val (positionsToDirections, isCompleted) = CastlesExplorer.explore(state, state.recentPosition, role.direction)
                        val explorer = Castle.from(state, positionsToDirections, isCompleted)
                        val pieceAlreadyPresentOnObject = explorer.parts
                            .any { part ->
                                state.players.any { player ->
                                    player.isPieceOn(
                                        part.position,
                                        Knight(part.direction)
                                    )
                                }
                            }
                        return if (pieceAlreadyPresentOnObject) setOf(PiecePlacedInInvalidPlace) else emptySet()
                    }
                    is Brigand -> {
                        val (parts, _) =
                            RoadsExplorer.explore(state, state.recentPosition, role.direction)
                        val pieceAlreadyPresentOnObject = parts.any { part ->
                            state.players.any { player ->
                                player.isPieceOn(
                                    part.position,
                                    Brigand(part.direction)
                                )
                            }
                        }
                        return if (pieceAlreadyPresentOnObject) setOf(PiecePlacedInInvalidPlace) else emptySet()
                    }
                    is Peasant -> {
                        val pieceAlreadyPresentOnObject = GreenFieldsExplorer.explore(state, state.recentPosition, role.location)
                            .any { part ->
                                state.players.any { player ->
                                    player.isPieceOn(
                                        part.first,
                                        Peasant(part.second)
                                    )
                                }
                            }
                        return if (pieceAlreadyPresentOnObject) setOf(PiecePlacedInInvalidPlace) else emptySet()
                    }
                    else -> return emptySet()
                }
            }
            else -> emptySet()
        }
    }
}
