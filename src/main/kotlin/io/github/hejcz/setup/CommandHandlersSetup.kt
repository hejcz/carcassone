package io.github.hejcz.setup

import io.github.hejcz.core.CommandHandler
import io.github.hejcz.basic.BeginGameHandler
import io.github.hejcz.basic.PutPieceHandler
import io.github.hejcz.basic.PutTileHandler
import io.github.hejcz.basic.SkipPieceHandler

class CommandHandlersSetup {
    private var handlers = BASIC_HANDLERS

    fun add(handler: CommandHandler) {
        handlers = handlers + handler
    }

    fun withExtensions(vararg extensions: Extension): CommandHandlersSetup {
        extensions.forEach { it.modify(this) }
        return this
    }

    fun handlers() = handlers.toList()

    companion object {
        private val BASIC_HANDLERS: List<CommandHandler> =
            listOf(PutTileHandler, PutPieceHandler, BeginGameHandler, SkipPieceHandler)
    }
}
