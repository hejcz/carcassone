package io.github.hejcz.abbot;

import io.github.hejcz.core.Command
import io.github.hejcz.core.Position

data class PickUpAbbot(val position: Position) : Command
