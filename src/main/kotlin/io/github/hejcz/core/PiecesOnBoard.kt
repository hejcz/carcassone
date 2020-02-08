package io.github.hejcz.core

import io.github.hejcz.corncircles.*

data class PiecesOnBoard(
    private val knights: List<Pair<Long, PieceOnBoard>> = emptyList(),
    private val brigands: List<Pair<Long, PieceOnBoard>> = emptyList(),
    private val abbots: List<Pair<Long, PieceOnBoard>> = emptyList(),
    private val monks: List<Pair<Long, PieceOnBoard>> = emptyList(),
    private val peasants: List<Pair<Long, PieceOnBoard>> = emptyList()
) {

    fun put(player: IPlayer, recentPosition: Position, piece: Piece, role: Role): PiecesOnBoard {
        val pieceOnBoard = player.id to PieceOnBoard(recentPosition, piece, role)
        return when (role) {
            is Knight -> copy(knights = knights + pieceOnBoard)
            is Brigand -> copy(brigands = brigands + pieceOnBoard)
            is Peasant -> copy(peasants = peasants + pieceOnBoard)
            Monk -> copy(monks = monks + pieceOnBoard)
            Abbot -> copy(abbots = abbots + pieceOnBoard)
        }
    }

    fun remove(player: IPlayer, position: Position, piece: Piece, role: Role): PiecesOnBoard {
        val pieceOnBoard = player.id to PieceOnBoard(position, piece, role)
        return when (role) {
            is Knight -> copy(knights = knights - pieceOnBoard)
            is Brigand -> copy(brigands = brigands - pieceOnBoard)
            is Peasant -> copy(peasants = peasants - pieceOnBoard)
            Monk -> copy(monks = monks - pieceOnBoard)
            Abbot -> copy(abbots = abbots - pieceOnBoard)
        }
    }

    fun piecesOn(position: Position, role: Role): List<Pair<Long, PieceOnBoard>> = when (role) {
        is Knight -> piecesOn(knights, position, role)
        is Brigand -> piecesOn(brigands, position, role)
        is Peasant -> piecesOn(peasants, position, role)
        Monk -> piecesOn(monks, position, role)
        Abbot -> piecesOn(abbots, position, role)
    }

    fun allKnights(): List<Pair<Long, PieceOnBoard>> = knights

    fun allBrigands(): List<Pair<Long, PieceOnBoard>> = brigands

    fun allMonks(): List<Pair<Long, PieceOnBoard>> = monks

    fun allAbbots(): List<Pair<Long, PieceOnBoard>> = abbots

    fun allPeasants(): List<Pair<Long, PieceOnBoard>> = peasants

    private fun piecesOn(list: List<Pair<Long, PieceOnBoard>>, position: Position, role: Role) =
        list.filter { it.second.position == position && it.second.role == role }

    fun playerPieces(player: IPlayer, symbol: CornSymbol): List<Pair<Long, PieceOnBoard>> = when (symbol) {
        CornSymbol.KNIGHT -> knights.filter { it.first == player.id }
        CornSymbol.BRIGAND -> brigands.filter { it.first == player.id }
        CornSymbol.PEASANT -> peasants.filter { it.first == player.id }
    }

}