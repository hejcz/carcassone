package io.github.hejcz.corncircles

import io.github.hejcz.core.*

data class CornCircleBegin(val cornSymbol: CornSymbol): GameEvent()

data class AddPieceEvent(val cornSymbol: CornSymbol): GameEvent()

data class RemovePieceEvent(val cornSymbol: CornSymbol): GameEvent()