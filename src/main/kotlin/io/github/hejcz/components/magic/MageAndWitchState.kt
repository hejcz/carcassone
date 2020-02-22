package io.github.hejcz.components.magic

import io.github.hejcz.api.PositionedDirection
import io.github.hejcz.api.State
import io.github.hejcz.api.StateExtensionId

interface MageAndWitchState {
    fun getMagePosition(): PositionedDirection?
    fun getWitchPosition(): PositionedDirection?
    fun mageOrWitchMustBeInstantlyMoved(state: State): Boolean

    companion object {
        val ID = StateExtensionId(
            MageAndWitchExtension::class.java.simpleName
        )
    }
}