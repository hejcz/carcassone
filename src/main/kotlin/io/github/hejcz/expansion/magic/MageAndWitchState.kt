package io.github.hejcz.expansion.magic

import io.github.hejcz.core.*

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