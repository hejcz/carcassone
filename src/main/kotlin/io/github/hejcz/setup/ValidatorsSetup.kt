package io.github.hejcz.setup

import io.github.hejcz.api.CmdValidator

class ValidatorsSetup {
    private var validators = setOf<CmdValidator>()

    fun withExtensions(vararg extension: Extension): ValidatorsSetup {
        extension.forEach { it.modify(this) }
        return this
    }

    fun add(validator: CmdValidator) {
        validators = validators + validator
    }

    fun validators(): List<CmdValidator> = validators.toList()
}
