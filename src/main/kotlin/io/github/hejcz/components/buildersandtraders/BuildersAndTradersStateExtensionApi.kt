package io.github.hejcz.components.buildersandtraders

import io.github.hejcz.api.GameChanges
import io.github.hejcz.api.State
import io.github.hejcz.base.PieceRemoved

interface BuildersAndTradersStateExtensionApi {
    fun maybeRemoveBuilderOrPig(state: State, event: PieceRemoved): GameChanges
    fun extendedBuilder(state: State): Boolean
}