package io.github.hejcz.components.magic

import io.github.hejcz.api.GameEvent

data class CantPickUpPieceEvent(val magicTarget: MagicTarget) : GameEvent