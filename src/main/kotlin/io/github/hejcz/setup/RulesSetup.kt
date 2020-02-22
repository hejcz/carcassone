package io.github.hejcz.setup

import io.github.hejcz.core.*
import io.github.hejcz.core.rule.*
import io.github.hejcz.engine.castleScoring
import io.github.hejcz.engine.incompleteCastleScoring
import io.github.hejcz.engine.roadScoring

class RulesSetup {
    private var rules = DEFAULT_SCORINGS

    private var endRules = DEFAULT_END_GAME_SCORINGS

    fun add(vararg newScorings: Scoring) {
        rules = rules + newScorings
    }

    fun add(endGameScoring: EndGameScoring) {
        endRules = endRules + endGameScoring
    }

    fun withExtensions(vararg extensions: Extension): RulesSetup {
        extensions.forEach { it.modify(this) }
        return this
    }

    fun rules(): Collection<Scoring> = rules

    fun endRules(): Collection<EndGameScoring> = endRules

    companion object {
        private val DEFAULT_SCORINGS: List<Scoring> =
            listOf(RewardCompletedCastle(castleScoring), RewardCompletedRoad(roadScoring), RewardCompletedCloister)

        private val DEFAULT_END_GAME_SCORINGS: List<EndGameScoring> =
            listOf(
                RewardIncompleteCastle(incompleteCastleScoring),
                RewardIncompleteRoad(roadScoring),
                RewardIncompleteCloister,
                RewardPeasants
            )
    }
}
