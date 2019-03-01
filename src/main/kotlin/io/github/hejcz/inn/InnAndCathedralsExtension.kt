package io.github.hejcz.inn

import io.github.hejcz.basic.*
import io.github.hejcz.core.*
import io.github.hejcz.setup.*

object InnAndCathedralsExtension : Extension {
    override fun modify(rulesSetup: RulesSetup) {
        rulesSetup.replace(RewardCompletedCastle::class, RewardCompletedCastle(CathedralCastleScoring))
        rulesSetup.replace(RewardIncompleteCastles::class, RewardIncompleteCastles(CathedralIncompleteCastleScoring))
        rulesSetup.replace(RewardCompletedRoad::class, RewardCompletedRoad(InnRoadScoring))
        rulesSetup.replace(RewardIncompleteRoads::class, RewardIncompleteRoads(InnRoadScoring))
    }

    override fun modify(piecesSetup: PiecesSetup) = piecesSetup.add(BigPiece)
}
