package io.github.hejcz.engine

import io.github.hejcz.api.GreenField
import io.github.hejcz.api.GreenFieldScoring
import io.github.hejcz.api.OwnedPiece
import io.github.hejcz.api.State
import io.github.hejcz.components.buildersandtraders.BuildersAndTradersExtension.PigRole

val greenFieldScoring: GreenFieldScoring = { state: State, field: GreenField, playerId: Long ->
    val pig = state.all(PigRole::class).find { it.playerId == playerId }
    when {
        isOnField(pig, field) -> 4 * field.completedCastles()
        else -> 3 * field.completedCastles()
    }
}

private fun isOnField(pig: OwnedPiece?, field: GreenField) =
    pig != null && field.parts.any {
        pig.pieceOnBoard.position == it.position && (pig.pieceOnBoard.role as PigRole).location == it.location }
