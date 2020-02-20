package io.github.hejcz.engine

import io.github.hejcz.core.Castle
import io.github.hejcz.core.PositionedDirection
import io.github.hejcz.core.Road
import io.github.hejcz.core.State
import io.github.hejcz.expansion.inn.tiles.InnTile
import io.github.hejcz.expansion.magic.MageAndWitchState

fun State.isMageIn(parts: Set<PositionedDirection>) = this.getWizardState()
    ?.getMagePosition()?.let { it in parts } ?: false

fun State.isWitchIn(parts: Set<PositionedDirection>) = this.getWizardState()
    ?.getWitchPosition()?.let { it in parts } ?: false

fun State.getWizardState() = this.get(MageAndWitchState.ID)?.let { it as MageAndWitchState }

fun State.hasInnOn(road: Road) = road.parts.any {
    val tile = this.tileAt(it.position)
    tile is InnTile && tile.isInnOnRoad(it.direction)
}

fun State.isCathedralIn(castle: Castle) =
    castle.parts.asSequence().map { this.tileAt(it.position) }.any { it is InnTile && it.hasCathedral() }