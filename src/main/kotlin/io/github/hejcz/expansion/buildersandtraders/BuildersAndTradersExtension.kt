package io.github.hejcz.expansion.buildersandtraders

import io.github.hejcz.core.*
import io.github.hejcz.setup.Extension
import io.github.hejcz.setup.StateExtensionSetup

interface BuildersAndTradersTile

object BuildersAndTradersExtension : Extension {

    // TODO
    // new tiles
    // goods tokens
    // builder
    // pig

    override fun modify(setup: StateExtensionSetup) {
        setup.add(BuildersAndTradersStateExtension())
    }

    private data class PositionedLocation(val position: Position, val location: Location)

    private data class GoodsTokens(val fabric: Int, val corn: Int, val barrel: Int)

    interface ChangeActivePlayerRule {
        fun apply(state: State, command: Command): State
    }

    private data class BuildersAndTradersStateExtension(
        val pigs: Map<Long, PositionedLocation> = emptyMap(),
        val builders: Map<Long, PositionedDirection> = emptyMap(),
        val tokens: Map<Long, PositionedDirection> = emptyMap(),
        val shouldHaveAnotherTurn: Boolean = false
    ) : StateExtension {
        override fun id(): StateExtensionId = ID

        companion object {
            val ID = StateExtensionId("BUILDERS_AND_TRADERS")
        }
    }

}
