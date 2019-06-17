package io.github.hejcz.core

class PiecesOnBoard {

    private val knights: MutableList<Pair<Long, PieceOnBoard>> = mutableListOf()
    private val brigands: MutableList<Pair<Long, PieceOnBoard>> = mutableListOf()
    private val abbots: MutableList<Pair<Long, PieceOnBoard>> = mutableListOf()
    private val monks: MutableList<Pair<Long, PieceOnBoard>> = mutableListOf()
    private val peasants: MutableList<Pair<Long, PieceOnBoard>> = mutableListOf()

    fun put(player: Player, recentPosition: Position, piece: Piece, role: Role) {
        run(role) {
            player.lockPiece(piece)
            val pieceOnBoard = player.id to PieceOnBoard(recentPosition, piece, role)
            add(pieceOnBoard)
        }
    }

    fun remove(player: Player, position: Position, piece: Piece, role: Role) {
        run(role) {
            player.unlockPiece(piece)
            val pieceOnBoard = player.id to PieceOnBoard(position, piece, role)
            remove(pieceOnBoard)
        }
    }

    fun pieceOn(position: Position, role: Role): Pair<Long, PieceOnBoard>? = run(role) {
        pieceOn(this, position, role)
    }

    private fun <T> run(role: Role, action: MutableList<Pair<Long, PieceOnBoard>>.() -> T): T {
        return when (role) {
            is Knight -> knights.action()
            is Brigand -> brigands.action()
            is Peasant -> peasants.action()
            Monk -> monks.action()
            Abbot -> abbots.action()
        }
    }

    fun allKnights(): List<Pair<Long, PieceOnBoard>> = knights.toList()

    fun allBrigands(): List<Pair<Long, PieceOnBoard>> = brigands.toList()

    fun allMonks(): List<Pair<Long, PieceOnBoard>> = monks.toList()

    fun allAbbots(): List<Pair<Long, PieceOnBoard>> = abbots.toList()

    fun allPeasants(): List<Pair<Long, PieceOnBoard>> = peasants.toList()

    private fun pieceOn(list: List<Pair<Long, PieceOnBoard>>, position: Position, role: Role) =
        list.find { it.second.position == position && it.second.role == role }

}