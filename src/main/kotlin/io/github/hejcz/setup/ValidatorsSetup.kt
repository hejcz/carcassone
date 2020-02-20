package io.github.hejcz.setup

import io.github.hejcz.core.CommandValidator
import io.github.hejcz.core.validator.PieceAvailabilityValidator
import io.github.hejcz.core.validator.PiecePlacementValidator
import io.github.hejcz.core.validator.SinglePieceInObjectValidator
import io.github.hejcz.core.validator.TilePlacementValidator

class ValidatorsSetup {

    private var validators =
        defaultValidators()

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
            TilePlacementValidator, PiecePlacementValidator, SinglePieceInObjectValidator, PieceAvailabilityValidator
        )
    }
}
