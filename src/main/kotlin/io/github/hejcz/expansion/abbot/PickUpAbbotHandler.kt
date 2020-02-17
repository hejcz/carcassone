package io.github.hejcz.expansion.abbot

import io.github.hejcz.core.*

object PickUpAbbotHandler : CommandHandler {
    override fun isApplicableTo(command: Command): Boolean = command is PickUpAbbotCmd
    override fun apply(state: State, command: Command): State = state
}
