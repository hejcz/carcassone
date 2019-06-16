package io.github.hejcz.setup

import io.github.hejcz.*
import io.github.hejcz.core.*
import io.github.hejcz.core.validator.*

class ValidatorsSetup {

    private var validators = defaultValidators()

    fun withExtensions(vararg extension: Extension): ValidatorsSetup {
        extension.forEach { it.modify(this) }
        return this
    }

    fun add(validator: CommandValidator) {
        validators = validators + validator
    }

    fun validators(): List<CommandValidator> = validators.toList()

    companion object {
        private fun defaultValidators(): Set<CommandValidator> = setOf(
            ExpectedCommandValidator(), TilePlacementValidator, PiecePlacementValidator, SinglePieceInObjectValidator, PieceAvailabilityValidator
        )
    }
}
