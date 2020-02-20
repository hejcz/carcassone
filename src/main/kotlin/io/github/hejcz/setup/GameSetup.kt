package io.github.hejcz.setup

import io.github.hejcz.core.StateExtension

open class GameSetup(private vararg val extensions: Extension) {

    open fun tiles() = TilesSetup().withExtensions(*extensions).remainingTiles()

    fun validators() = ValidatorsSetup().withExtensions(*extensions).validators()

    fun pieces() = PiecesSetup().withExtensions(*extensions).pieces()

    fun rules() = RulesSetup().withExtensions(*extensions).rules()

    fun endRules() = RulesSetup().withExtensions(*extensions).endRules()

    fun handlers() = CommandHandlersSetup().withExtensions(*extensions).handlers()

    fun stateExtensions(): Set<StateExtension> = StateExtensionSetup().withExtensions(*extensions).states()

}
