package io.github.hejcz.basic

import io.github.hejcz.core.*
import io.github.hejcz.helpers.*

object SinglePieceInObjectValidator : CommandValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> {
        return when (command) {
            is PutPiece -> {
                when (val role = command.pieceRole) {
                    is Knight -> {
                        val explorer =
                            CastleExplorer(state, PositionedDirection(state.recentPosition, role.direction))
                        explorer.explore()
                        val pieceAlreadyPresentOnObject = explorer.parts()
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
                        val explorer =
                            RoadExplorer(state, PositionedDirection(state.recentPosition, role.direction))
                        explorer.explore()
                        val pieceAlreadyPresentOnObject = explorer.parts()
                            .any { part ->
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
                        val pieceAlreadyPresentOnObject = GreenFieldExplorer(state, state.recentPosition, role.location)
                            .explore()
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
