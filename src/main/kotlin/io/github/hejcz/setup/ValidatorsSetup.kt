package io.github.hejcz.setup

import io.github.hejcz.core.CmdValidator
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

    fun add(validator: CmdValidator) {
        validators = validators + validator
    }

    fun validators(): List<CmdValidator> = validators.toList()

    companion object {
        private fun defaultValidators(): Set<CmdValidator> = setOf(
            TilePlacementValidator, PiecePlacementValidator, SinglePieceInObjectValidator, PieceAvailabilityValidator
        )
    }
}
