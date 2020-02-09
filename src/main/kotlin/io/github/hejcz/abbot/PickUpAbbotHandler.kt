package io.github.hejcz.abbot

import io.github.hejcz.core.*

object PickUpAbbotHandler : CommandHandler {
    override fun isApplicableTo(command: Command): Boolean = command is PickUpAbbot
    override fun beforeScoring(state: State, command: Command) = GameChanges.noEvents(state)
}