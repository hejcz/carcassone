package io.github.hejcz.core

import io.github.hejcz.expansion.magic.MageAndWitchState

fun State.isMageIn(parts: Set<PositionedDirection>) = this.getWizardState()
    ?.getMagePosition()?.let { it in parts } ?: false

fun State.isWitchIn(parts: Set<PositionedDirection>) = this.getWizardState()
    ?.getWitchPosition()?.let { it in parts } ?: false

fun State.getWizardState() = this.get(MageAndWitchState.ID)?.let { it as MageAndWitchState }