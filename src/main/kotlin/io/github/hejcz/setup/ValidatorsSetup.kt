package io.github.hejcz.setup

import io.github.hejcz.core.CommandValidator
import io.github.hejcz.basic.PieceAvailabilityValidator
import io.github.hejcz.basic.PiecePlacementValidator
import io.github.hejcz.basic.TilePlacementValidator
import io.github.hejcz.basic.SinglePieceInObjectValidator

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
