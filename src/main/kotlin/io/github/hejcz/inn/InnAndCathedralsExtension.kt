package io.github.hejcz.inn

import io.github.hejcz.core.*
import io.github.hejcz.core.rule.*
import io.github.hejcz.setup.*

object InnAndCathedralsExtension : Extension {
    override fun modify(rulesSetup: RulesSetup) {
        rulesSetup.replace(RewardCompletedCastle::class, RewardCompletedCastle(castleScoring))
        rulesSetup.replace(RewardIncompleteCastle::class, RewardIncompleteCastle(incompleteCastleScoring))
        rulesSetup.replace(RewardCompletedRoad::class, RewardCompletedRoad(roadScoring))
        rulesSetup.replace(RewardIncompleteRoad::class, RewardIncompleteRoad(roadScoring))
    }

    override fun modify(piecesSetup: PiecesSetup) = piecesSetup.add(BigPiece)
}
