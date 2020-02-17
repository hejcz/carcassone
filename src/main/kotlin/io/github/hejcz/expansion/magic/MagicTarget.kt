package io.github.hejcz.expansion.magic

import io.github.hejcz.core.MagePiece
import io.github.hejcz.core.WitchPiece

enum class MagicTarget {
    WITCH, MAGICIAN;

    fun toPiece() = when (this) {
        MagicTarget.MAGICIAN -> MagePiece
        MagicTarget.WITCH -> WitchPiece
    }
}
