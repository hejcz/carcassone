package io.github.hejcz.setup

import io.github.hejcz.basic.validator.*
import io.github.hejcz.core.*

class ValidatorsSetup {

    private var validators = defaultValidators

    fun withExtensions(vararg extension: Extension): ValidatorsSetup {
        extension.forEach { it.modify(this) }
        return this
    }

    fun add(validator: CommandValidator) {
        validators = validators + validator
    }

    fun validators(): List<CommandValidator> = validators.toList()

    companion object {
        private val defaultValidators: Set<CommandValidator> = setOf(
            TilePlacementValidator, PiecePlacementValidator, SinglePieceInObjectValidator, PieceAvailabilityValidator
        )
    }
}
