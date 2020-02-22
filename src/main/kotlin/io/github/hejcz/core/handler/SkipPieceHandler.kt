package io.github.hejcz.core.handler

import io.github.hejcz.core.Command
import io.github.hejcz.core.CmdHandler
import io.github.hejcz.core.SkipPieceCmd
import io.github.hejcz.core.State

object SkipPieceHandler : CmdHandler {
    override fun isApplicableTo(command: Command): Boolean = command is SkipPieceCmd
    override fun apply(state: State, command: Command): State = state
}
