package io.github.hejcz.components.magic

import io.github.hejcz.api.Command
import io.github.hejcz.api.Direction
import io.github.hejcz.api.Position

/**
 * Move mage or witch to target place.
 */
data class MoveMageOrWitchCmd(val position: Position, val direction: Direction, val magicTarget: MagicTarget) :
    Command

/**
 * Pick up mage or witch when both are on the board and they can't be moved anywhere.
 */
data class PickUpMageOrWitchCmd(val magicTarget: MagicTarget) : Command
