package io.github.hejcz.components.abbot

import io.github.hejcz.api.Command
import io.github.hejcz.api.Position

data class PickUpAbbotCmd(val position: Position) : Command
