package io.github.hejcz.expansion.abbot

import io.github.hejcz.core.*

object GardenCompletedRule : Rule {

    override fun afterCommand(command: Command, state: State): Collection<GameEvent> = when (command) {
        is TileCmd -> afterTilePlaced(state.recentPosition(), state)
        is PieceCmd -> afterPiecePlaced(state, command.role)
        else -> emptySet()
    }

    private fun afterTilePlaced(position: Position, state: State): Collection<GameEvent> =
        position.surrounding()
            .filter { state.tileAt(it).hasGarden() && it.isSurrounded(state) }
            .flatMap { gardenPosition -> pieceWithOwner(state, gardenPosition) }
            .map { (playerId, pieceOnBoard) -> ScoreEvent(playerId, 9, setOf(pieceOnBoard)) }

    private fun pieceWithOwner(state: State, completedCloisterPosition: Position) =
        state.findPieces(completedCloisterPosition, Abbot)

    private fun afterPiecePlaced(state: State, role: Role): Collection<GameEvent> = when {
        role is Abbot && state.recentPosition().isSurrounded(state) ->
            setOf(
                ScoreEvent(state.currentPlayerId(), 9, setOf(PieceOnBoard(state.recentPosition(), AbbotPiece, Abbot)))
            )
        else -> emptySet()
    }
}
