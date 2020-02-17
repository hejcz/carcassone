package io.github.hejcz.core.setup

import io.github.hejcz.core.*
import io.github.hejcz.core.handler.*

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
