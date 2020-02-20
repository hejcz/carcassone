package io.github.hejcz.expansion.magic

import io.github.hejcz.core.GameEvent

data class CantPickUpPieceEvent(val magicTarget: MagicTarget) : GameEvent()