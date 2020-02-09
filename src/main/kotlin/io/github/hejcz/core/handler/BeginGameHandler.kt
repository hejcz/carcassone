package io.github.hejcz.core.handler

import io.github.hejcz.core.*

object BeginGameHandler : CommandHandler {
    override fun isApplicableTo(command: Command): Boolean = command is Begin
    override fun beforeScoring(state: State, command: Command): GameChanges = GameChanges.noEvents(state)
}
