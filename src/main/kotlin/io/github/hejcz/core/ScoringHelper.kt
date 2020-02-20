package io.github.hejcz.core

import io.github.hejcz.expansion.magic.MageAndWitchState

fun hasMage(state: State, parts: Set<PositionedDirection>) = state.getWizardState()
    ?.let { parts.any { part -> it.isMageOn(part.position, part.direction) } }
    ?: false

fun hasWitch(state: State, parts: Set<PositionedDirection>) = state.getWizardState()
    ?.let { parts.any { part -> it.isWitchOn(part.position, part.direction) } }
    ?: false

fun State.getWizardState() = this.get(MageAndWitchState.ID)?.let { it as MageAndWitchState }