package io.github.hejcz.engine

class DefaultValidators {

    private var validators = defaultValidators

    fun withExtensions(vararg extension: Extension): DefaultValidators {
        extension.forEach { it.modify(this) }
        return this
    }

    fun add(validator: CommandValidator) {
        validators = validators + validator
    }

    fun validators(): List<CommandValidator> = validators.toList()

    companion object {
        private val defaultValidators: Set<CommandValidator> = setOf(
            PutTileValidator, PiecePlacementValidator, SinglePieceInObjectValidator, MappleAvailabilityValidator
        )
    }

}
