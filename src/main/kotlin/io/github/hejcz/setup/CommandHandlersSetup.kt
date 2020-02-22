package io.github.hejcz.setup

import io.github.hejcz.core.CmdHandler
import io.github.hejcz.core.handler.BeginGameHandler
import io.github.hejcz.core.handler.PutPieceHandler
import io.github.hejcz.core.handler.PutTileHandler
import io.github.hejcz.core.handler.SkipPieceHandler

class CommandHandlersSetup {
    private var handlers = BASIC_HANDLERS

    fun add(handler: CmdHandler) {
        handlers = handlers + handler
    }

    fun withExtensions(vararg extensions: Extension): CommandHandlersSetup {
        extensions.forEach { it.modify(this) }
        return this
    }

    fun handlers() = handlers.toList()

    companion object {
        private val BASIC_HANDLERS: List<CmdHandler> =
            listOf(PutTileHandler, PutPieceHandler, BeginGameHandler, SkipPieceHandler)
    }
}
