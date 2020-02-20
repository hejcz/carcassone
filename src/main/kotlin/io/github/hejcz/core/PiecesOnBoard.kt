package io.github.hejcz.core

import kotlin.reflect.KClass

data class PiecesOnBoard(
    private val pieces: Map<KClass<out Role>, List<OwnedPiece>> = emptyMap()
) {
    fun put(player: IPlayer, position: Position, piece: Piece, role: Role): PiecesOnBoard =
        copy(pieces = pieces + (role::class to
                get(role::class) + OwnedPiece(player.id, PieceOnBoard(position, piece, role))))

    fun remove(player: IPlayer, position: Position, piece: Piece, role: Role): PiecesOnBoard =
        copy(pieces = pieces + (role::class to
                get(role::class) - OwnedPiece(player.id, PieceOnBoard(position, piece, role))))

    fun piecesOn(position: Position, role: Role): List<OwnedPiece> =
        get(role::class).filter { it.pieceOnBoard.position == position && it.pieceOnBoard.role == role }

    fun get(kClass: KClass<out Role>) = pieces.getOrDefault(kClass, emptyList())
}
