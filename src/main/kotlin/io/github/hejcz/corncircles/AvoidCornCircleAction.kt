package io.github.hejcz.corncircles

import io.github.hejcz.core.*
import io.github.hejcz.util.*

object AvoidCornCircleAction : Command

val AvoidCornCircleActionHandler = eventHandlerNoEvents<AvoidCornCircleAction> { game, _ ->
    game.state.changeActivePlayer()
}

val AvoidCornCircleActionValidator = commandValidator<AvoidCornCircleAction> { state, _ ->
    when (val tile = state.recentTile) {
        is CornCircleTile -> when {
            state.currentPlayerPieces(tile.cornCircleEffect()).isNotEmpty() -> setOf(PieceCanNotBeSkipped)
            else -> emptySet()
        }
        else -> emptySet()
    }
}
