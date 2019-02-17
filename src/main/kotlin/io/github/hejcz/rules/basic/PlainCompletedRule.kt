package io.github.hejcz.rules.basic

import io.github.hejcz.*
import io.github.hejcz.engine.State
import io.github.hejcz.mapples.Peasant
import io.github.hejcz.placement.Location
import io.github.hejcz.placement.Position
import io.github.hejcz.placement.PositionedDirection
import io.github.hejcz.rules.EndRule
import io.github.hejcz.rules.helpers.GreenFieldExplorer

object PlainCompletedRule : EndRule {

    override fun apply(state: State): Collection<GameEvent> {
        return state.players.flatMap { player -> player.pieces().map { piece -> Pair(player.id, piece) } }
            .filter { (_, piece) -> piece.role is Peasant }
            .map { (playerId, piece) -> Pair(playerId,
                testPlain(
                    state,
                    piece.position,
                    (piece.role as Peasant).location
                )
            ) }
            .groupBy { (_, fieldParts) -> fieldParts }
            .values
            .flatMap { fieldParts ->
                val completedCastles =
                    fieldParts.first()
                        .second
                        .flatMap {
                            reachableCastles(
                                it.first,
                                it.second
                            )
                        }
                        .mapNotNull { state.completedCastle(it) }
                        .distinct()
                        .count()
                if (completedCastles == 0) {
                    return emptySet()
                }
                val countedPieces = fieldParts.groupingBy { it.first }.eachCount()
                val maxElement = countedPieces.maxBy { it.value }!!.value
                countedPieces.filter { it.value == maxElement }
                    .map { PlayerScored(it.key, 3 * completedCastles, emptySet()) }
            }
    }

    private fun testPlain(state: State, position: Position, location: Location): Set<Pair<Position, Location>> {
        val explorer = GreenFieldExplorer(state, position, location)
        explorer.explore()
        return explorer.parts()
    }

    private fun reachableCastles(position: Position, location: Location) =
        setOf(PositionedDirection(position, location.direction)) +
            if (location.side == null)
                setOf(
                        PositionedDirection(position, location.direction.left()),
                        PositionedDirection(position, location.direction.right())
                )
            else emptySet()
}

