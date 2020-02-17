package io.github.hejcz.expansion.magic

import io.github.hejcz.core.Command
import io.github.hejcz.core.Direction
import io.github.hejcz.core.Position

/**
 * Move mage or witch to target place.
 */
data class MoveMageOrWitchCmd(val position: Position, val direction: Direction, val magicTarget: MagicTarget) : Command

/**
 * Pick up mage or witch when both are on the board and they can't be moved anywhere.
 */
data class PickUpMageOrWitchCmd(val magicTarget: MagicTarget) : Command
