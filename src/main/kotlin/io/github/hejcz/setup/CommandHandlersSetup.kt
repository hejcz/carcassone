package io.github.hejcz.setup

import io.github.hejcz.api.CmdHandler

class CommandHandlersSetup {
    private var handlers = emptySet<CmdHandler>()

    fun add(handler: CmdHandler) {
        handlers = handlers + handler
    }

    fun withExtensions(vararg extensions: Extension): CommandHandlersSetup {
        extensions.forEach { it.modify(this) }
        return this
    }

    fun handlers() = handlers.toList()
}
