package io.github.hejcz.core.setup

import io.github.hejcz.core.*

class PiecesSetup {
    private var pieces = BASIC_PIECES

    fun add(piece: Piece) {
        pieces = pieces + piece
    }

    fun withExtensions(vararg extensions: Extension): PiecesSetup {
        extensions.forEach { it.modify(this) }
        return this
    }

    fun pieces() = pieces.toList()

    companion object {
        private val BASIC_PIECES: List<Piece> = (1..7).map { SmallPiece }
    }
}
