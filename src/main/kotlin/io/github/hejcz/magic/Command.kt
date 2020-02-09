package io.github.hejcz.magic

import io.github.hejcz.core.Command
import io.github.hejcz.core.Direction
import io.github.hejcz.core.Position

/**
 * Move magician or witch to target place.
 */
data class MoveMagicianOrWitchCommand(val position: Position, val direction: Direction, val magicTarget: MagicTarget) : Command

/**
 * Pick up magician or witch when both are on the board and they can't be moved anywhere.
 */
data class PickUpMagicianOrWitch(val magicTarget: MagicTarget) : Command
