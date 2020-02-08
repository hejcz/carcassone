package io.github.hejcz.core

import java.lang.RuntimeException

data class Player(override val id: Long, override val order: Int, val initialPieces: List<Piece> = emptyList()) : IPlayer {

    private val pools by lazy { initialPieces.groupBy { it }.mapValues { PiecePool(it.value.count()) } }

    override fun isAvailable(piece: Piece) = pools[piece]?.isAvailable() ?: false

    override fun lockPiece(piece: Piece): Player {
        pools[piece]?.lock() ?: throw RuntimeException("Piece not available")
        return this
    }

    override fun unlockPiece(piece: Piece): Player {
        pools[piece]?.unlock() ?: throw RuntimeException("Piece not available")
        return this
    }

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
