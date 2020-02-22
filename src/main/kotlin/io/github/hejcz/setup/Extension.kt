package io.github.hejcz.setup

interface Extension {
    fun modify(deck: TilesSetup) = Unit
    fun modify(setup: ValidatorsSetup) = Unit
    fun modify(setup: PiecesSetup) = Unit
    fun modify(setup: ScoringSetup) = Unit
    fun modify(setup: CommandHandlersSetup) = Unit
    fun modify(setup: StateExtensionSetup) = Unit
}
