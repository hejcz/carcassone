package io.github.hejcz.core

interface IPlayer {
    val id: Long
    val order: Int
    fun isAvailable(piece: Piece): Boolean
    fun lockPiece(piece: Piece): IPlayer
    fun unlockPiece(piece: Piece): IPlayer
}