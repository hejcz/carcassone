package io.github.hejcz.corncircles

import io.github.hejcz.core.*
import io.github.hejcz.util.*

enum class CornCircleAction {
    ADD_PIECE, REMOVE_PIECE
}

data class ChooseCornCircleActionCommand(val action: CornCircleAction): Command

val ChooseCornCircleActionHandler = eventHandlerNoEvents<ChooseCornCircleActionCommand> { game, _ ->
    game.state.changeActivePlayer()
}