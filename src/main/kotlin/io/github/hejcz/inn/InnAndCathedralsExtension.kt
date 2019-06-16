package io.github.hejcz.inn

import io.github.hejcz.core.*
import io.github.hejcz.core.rule.*
import io.github.hejcz.setup.*

object InnAndCathedralsExtension : Extension {
    override fun modify(rulesSetup: RulesSetup) {
        rulesSetup.replace(RewardCompletedCastle::class, RewardCompletedCastle(scoreCompletedCastleWithCathedral))
        rulesSetup.replace(RewardIncompleteCastle::class, RewardIncompleteCastle(scoreIncompleteCastleWithCathedral))
        rulesSetup.replace(RewardCompletedRoad::class, RewardCompletedRoad(scoreRoadWithInn))
        rulesSetup.replace(RewardIncompleteRoad::class, RewardIncompleteRoad(scoreRoadWithInn))
    }

    override fun modify(piecesSetup: PiecesSetup) = piecesSetup.add(BigPiece)
}
