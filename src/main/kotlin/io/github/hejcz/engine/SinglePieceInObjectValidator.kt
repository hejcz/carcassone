package io.github.hejcz.engine

import io.github.hejcz.Command
import io.github.hejcz.GameEvent
import io.github.hejcz.PiecePlacedInInvalidPlace
import io.github.hejcz.PutPiece
import io.github.hejcz.mapples.Brigand
import io.github.hejcz.mapples.Knight
import io.github.hejcz.mapples.Peasant
import io.github.hejcz.placement.PositionedDirection
import io.github.hejcz.rules.helpers.CastleExplorer
import io.github.hejcz.rules.helpers.GreenFieldExplorer
import io.github.hejcz.rules.helpers.RoadExplorer

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
                        val explorer =
                            GreenFieldExplorer(state, state.recentPosition, role.location)
                        explorer.explore()
                        val pieceAlreadyPresentOnObject = explorer.parts()
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