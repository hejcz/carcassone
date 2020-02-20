package io.github.hejcz.core.handler

import io.github.hejcz.core.BeginCmd
import io.github.hejcz.core.Command
import io.github.hejcz.core.CommandHandler
import io.github.hejcz.core.State

object BeginGameHandler : CommandHandler {
    override fun isApplicableTo(command: Command): Boolean = command is BeginCmd
    override fun apply(state: State, command: Command): State = state
}
