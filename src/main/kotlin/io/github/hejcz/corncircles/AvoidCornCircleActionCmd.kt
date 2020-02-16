package io.github.hejcz.corncircles

import io.github.hejcz.core.*
import io.github.hejcz.util.*

object AvoidCornCircleActionCmd : Command

val AvoidCornCircleActionHandler = object : CommandHandler {
    override fun isApplicableTo(command: Command): Boolean = command is AvoidCornCircleActionCmd

    override fun beforeScoring(state: State, command: Command): GameChanges =
        (command as AvoidCornCircleActionCmd).let {
            GameChanges.noEvents(
                state.changeActivePlayer()
            )
        }
}

val AvoidCornCircleActionValidator = commandValidator<AvoidCornCircleActionCmd> { state, _ ->
    when (val tile = state.recentTile()) {
        is CornCircleTile -> when {
            state.currentPlayerPieces(tile.cornCircleEffect()).isNotEmpty() -> setOf(PieceCanNotBeSkipped)
            else -> emptySet()
        }
        else -> emptySet()
    }
}
