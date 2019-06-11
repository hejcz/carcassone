package io.github.hejcz.core

data class Player(val id: Long, val order: Int, private val initialPieces: List<Piece>) {
    private val unavailablePieces: MutableSet<PieceOnBoard> = mutableSetOf()
    private val availablePieces: MutableList<Piece> = initialPieces.toMutableList()

    fun putPiece(position: Position, piece: Piece, role: Role) {
        unavailablePieces.add(PieceOnBoard(position, piece, role))
        availablePieces.remove(piece)
    }

    fun isAvailable(piece: Piece) = piece in availablePieces

    fun pieceOn(position: Position, role: Role): PieceOnBoard? =
        unavailablePieces.firstOrNull { position == it.position && role == it.role }

    fun piecesOn(position: Position): Collection<PieceOnBoard> =
        unavailablePieces.filter { position == it.position }

    fun isPieceOn(position: Position, role: Role) =
        unavailablePieces.any { position == it.position && role == it.role }

    fun pieces(): Set<PieceOnBoard> = unavailablePieces.toSet()

    fun returnPieces(returnedPieces: Collection<PieceOnBoard>) {
        availablePieces.addAll(returnedPieces.map { it.piece })
        unavailablePieces.removeAll(returnedPieces)
    }
}
