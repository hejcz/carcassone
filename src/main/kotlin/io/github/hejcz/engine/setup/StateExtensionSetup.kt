package io.github.hejcz.engine.setup

import io.github.hejcz.core.StateExtension

class StateExtensionSetup {
    private var extensions = emptySet<StateExtension>()

    fun add(ext: StateExtension) {
        extensions = extensions + ext
    }

    fun states() = extensions

    fun withExtensions(vararg extensions: Extension): StateExtensionSetup {
        extensions.forEach { it.modify(this) }
        return this
    }

}
