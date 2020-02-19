package io.github.hejcz.core.handler

import io.github.hejcz.core.*

object SkipPieceHandler : CommandHandler {
    override fun isApplicableTo(command: Command): Boolean = command is SkipPieceCmd
    override fun apply(state: State, command: Command): State = state
}
