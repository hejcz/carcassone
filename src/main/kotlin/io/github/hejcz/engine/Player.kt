package io.github.hejcz.engine

import io.github.hejcz.mapples.Mapple
import io.github.hejcz.mapples.PieceId
import io.github.hejcz.mapples.PieceOnBoard
import io.github.hejcz.mapples.PieceRole
import io.github.hejcz.placement.Position

data class Player(val id: Long, val order: Int, private val mapples: List<PieceId> = (1..7).map { Mapple }) {
    private val pieces = mutableSetOf<PieceOnBoard>()
    private val mutableMapples = mapples.toMutableList()

    fun putPiece(position: Position, pieceId: PieceId, role: PieceRole) {
        pieces.add(PieceOnBoard(position, pieceId, role))
        mutableMapples.remove(Mapple)
    }

    fun isMappleAvailable() = mutableMapples.isNotEmpty()

    fun isPieceOn(position: Position, role: PieceRole) =
        pieces.any { position == it.position && role == it.role }

    fun pieces(): Set<PieceOnBoard> = pieces.toSet()

    fun returnPieces(returnedPiecesIds: Collection<PieceId>) {
        mutableMapples.addAll(returnedPiecesIds.map { Mapple })
    }
}