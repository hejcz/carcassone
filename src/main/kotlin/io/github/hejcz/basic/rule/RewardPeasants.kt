package io.github.hejcz.basic.rule

import io.github.hejcz.core.*
import io.github.hejcz.helpers.*

object RewardPeasants : EndRule {

    override fun apply(state: State): Collection<GameEvent> {
        return state.players.flatMap { player -> player.piecesOnBoard().map { piece -> Pair(player.id, piece) } }
            .filter { (_, piece) -> piece.role is Peasant }
            .map { (playerId, piece) ->
                Pair(
                    playerId,
                    TailRecGreenFieldExplorer.explore(state, piece.position, (piece.role as Peasant).location)
                )
            }
            .groupBy { (_, fieldParts) -> fieldParts }
            .values
            .flatMap { fieldParts ->
                val completedCastles =
                    fieldParts.first()
                        .second
                        .flatMap { (position, direction) -> reachableCastles(position, direction) }
                        .mapNotNull { state.completedCastle(it) }
                        .distinct()
                        .count()
                if (completedCastles == 0) {
                    return emptySet()
                }
                val countedPieces = fieldParts.groupingBy { (playerId, _) -> playerId }.eachCount()
                val maxElement = countedPieces.maxBy { it.value }!!.value
                countedPieces.filter { it.value == maxElement }
                    .map { PlayerScored(it.key, 3 * completedCastles, emptySet()) }
            }
    }

    private fun reachableCastles(position: Position, location: Location) =
        setOf(PositionedDirection(position, location.direction)) +
                when {
                    location.side == null -> setOf(
                        PositionedDirection(position, location.direction.left()),
                        PositionedDirection(position, location.direction.right())
                    )
                    else -> emptySet()
                }
}
