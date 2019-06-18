package io.github.hejcz.corncircles

import io.github.hejcz.core.*

enum class CornCircleAction {
    ADD_PIECE, REMOVE_PIECE
}

data class ChooseCornCircleActionCommand(val action: CornCircleAction): Command

object ChooseCornCircleActionHandler : CommandHandler {
    override fun handle(game: Game, command: Command): Collection<GameEvent> {
        game.state.changeActivePlayer()
        return emptySet()
    }

    override fun isApplicableTo(command: Command): Boolean = command is ChooseCornCircleActionCommand
}