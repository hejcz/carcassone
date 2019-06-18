package io.github.hejcz.corncircles

import io.github.hejcz.core.*

object AvoidCornCircleAction : Command

object AvoidCornCircleActionHandler : CommandHandler {
    override fun handle(game: Game, command: Command): Collection<GameEvent> = handle(game)

    private fun handle(game: Game): Collection<GameEvent> {
        game.state.changeActivePlayer()
        return emptySet()
    }

    override fun isApplicableTo(command: Command): Boolean = command is AvoidCornCircleAction
}

object AvoidCornCircleActionValidator : CommandValidator {
    override fun validate(state: State, command: Command): Collection<GameEvent> =
        (command as? AvoidCornCircleAction)?.let { validate(state) } ?: emptySet()

    private fun validate(state: State): Collection<GameEvent> = when (val tile = state.recentTile) {
        is CornCircleTile -> when {
            state.currentPlayerPieces(tile.cornCircleEffect()).isNotEmpty() -> setOf(PieceCanNotBeSkipped)
            else -> emptySet()
        }
        else -> emptySet()
    }
}