package io.github.hejcz.core

import java.lang.RuntimeException

data class Player(val id: Long, val order: Int, val initialPieces: List<Piece> = emptyList()) {

    private val pools by lazy { initialPieces.groupBy { it }.mapValues { PiecePool(it.value.count()) } }

    fun isAvailable(piece: Piece) = pools[piece]?.isAvailable() ?: false

    fun lockPiece(piece: Piece) = pools[piece]?.lock() ?: throw RuntimeException("Piece not available")

    fun unlockPiece(piece: Piece) = pools[piece]?.unlock() ?: throw RuntimeException("Piece not available")

    companion object {

        class PiecePool(private val limit: Int) {
            private var currentAmount = limit

            fun lock() {
                if (currentAmount == 0) {
                    throw RuntimeException("Missing piece can't be locked")
                } else {
                    currentAmount--
                }
            }

            fun unlock() {
                if (currentAmount == limit) {
                    throw RuntimeException("Piece can't be unlocked above limit")
                } else {
                    currentAmount++
                }
            }

            fun isAvailable(): Boolean = currentAmount > 0
        }

    }
}
