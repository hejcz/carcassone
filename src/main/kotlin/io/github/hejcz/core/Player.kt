package io.github.hejcz.core

import java.lang.RuntimeException

data class Player(
    override val id: Long,
    override val order: Int,
    override val initialPieces: List<Piece>,
    private val pieces: Map<Piece, PiecePool>
) : IPlayer {

    constructor(id: Long, order: Int) :
            this(id, order, emptyList())

    constructor(id: Long, order: Int, initialPieces: List<Piece>) :
            this(id, order, initialPieces, initialPieces.groupBy { it }.mapValues { PiecePool(it.value.count()) })

    override fun isAvailable(piece: Piece) = pieces[piece]?.isAvailable() ?: false

    override fun lockPiece(piece: Piece): Player =
        Player(id, order, initialPieces, pieces + (piece to (pieces[piece]?.lock() ?: throw RuntimeException("No such piece: $piece"))))

    override fun unlockPiece(piece: Piece): Player =
        Player(id, order, initialPieces, pieces + (piece to (pieces[piece]?.unlock() ?: throw RuntimeException("No such piece: $piece"))))

    companion object {

        class PiecePool(private val max: Int, private val currentAmount: Int) {

            constructor(max: Int) : this(max, max)

            fun lock(): PiecePool =
                when (currentAmount) {
                    0 -> throw RuntimeException("Missing piece can't be locked")
                    else -> PiecePool(max, currentAmount - 1)
                }

            fun unlock(): PiecePool =
                when (currentAmount) {
                    max -> throw RuntimeException("Piece can't be unlocked above limit")
                    else -> PiecePool(max, currentAmount + 1)
                }

            fun isAvailable(): Boolean = currentAmount > 0
        }
    }
}
