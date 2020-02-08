package io.github.hejcz.corncircles

import io.github.hejcz.core.*

enum class CornCircleAction {
    ADD_PIECE, REMOVE_PIECE
}

data class ChooseCornCircleActionCommand(val action: CornCircleAction): Command

val ChooseCornCircleActionHandler = object : CommandHandler {
    override fun isApplicableTo(command: Command): Boolean = command is ChooseCornCircleActionCommand

    override fun beforeScoring(state: State, command: Command): GameChanges =
        (command as ChooseCornCircleActionCommand).let {
            GameChanges.noEvents(
                state.changeActivePlayer()
            )
        }

}
