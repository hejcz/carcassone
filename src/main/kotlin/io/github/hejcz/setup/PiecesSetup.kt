package io.github.hejcz.setup

import io.github.hejcz.api.Piece

class PiecesSetup {
    private var pieces = emptyList<Piece>()

    fun add(piece: Piece) {
        pieces = pieces + piece
    }

    fun withExtensions(vararg extensions: Extension): PiecesSetup {
        extensions.forEach { it.modify(this) }
        return this
    }

    fun pieces() = pieces.toList()
}
