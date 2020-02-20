package io.github.hejcz.expansion.magic

import io.github.hejcz.core.Direction
import io.github.hejcz.core.Position
import io.github.hejcz.core.State
import io.github.hejcz.core.StateExtensionId

interface MageAndWitchState {
    fun isMageOn(position: Position, direction: Direction): Boolean
    fun isWitchOn(position: Position, direction: Direction): Boolean
    fun mageOrWitchMustBeInstantlyMoved(state: State): Boolean

    companion object {
        val ID = StateExtensionId(
            MageAndWitchExtension::class.java.simpleName
        )
    }
}