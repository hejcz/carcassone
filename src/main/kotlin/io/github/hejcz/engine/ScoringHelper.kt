package io.github.hejcz.engine

import io.github.hejcz.api.Castle
import io.github.hejcz.api.PositionedDirection
import io.github.hejcz.api.Road
import io.github.hejcz.api.State
import io.github.hejcz.components.inn.tiles.InnTile
import io.github.hejcz.components.magic.MageAndWitchState

fun State.isMageIn(parts: Set<PositionedDirection>) = this.getWizardState()
    ?.getMagePosition()?.let { it in parts } ?: false

fun State.isWitchIn(parts: Set<PositionedDirection>) = this.getWizardState()
    ?.getWitchPosition()?.let { it in parts } ?: false

fun State.hasInnOn(road: Road) = road.parts.any {
    val tile = this.tileAt(it.position)
    tile is InnTile && tile.isInnOnRoad(it.direction)
}

fun State.isCathedralIn(castle: Castle) =
    castle.parts.asSequence().map { this.tileAt(it.position) }.any { it is InnTile && it.hasCathedral() }