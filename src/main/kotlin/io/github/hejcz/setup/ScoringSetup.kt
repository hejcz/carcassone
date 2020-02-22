package io.github.hejcz.setup

import io.github.hejcz.api.CastleScoring
import io.github.hejcz.api.EndGameScoring
import io.github.hejcz.api.RoadScoring
import io.github.hejcz.api.Scoring

class ScoringSetup(private val castleScoring: CastleScoring, private val roadScoring: RoadScoring) {
    private var scoring = emptySet<Scoring>()

    private var endGameScoring = emptySet<EndGameScoring>()

    fun add(vararg newScorings: Scoring) {
        scoring = scoring + newScorings
    }

    fun add(endGameScoring: EndGameScoring) {
        this.endGameScoring = this.endGameScoring + endGameScoring
    }

    fun withExtensions(vararg extensions: Extension): ScoringSetup {
        extensions.forEach { it.modify(this) }
        return this
    }

    fun rules(): Collection<Scoring> = scoring

    fun endRules(): Collection<EndGameScoring> = endGameScoring

    fun completedCastleScoring(): CastleScoring = castleScoring

    fun completedRoadScoring(): RoadScoring = roadScoring

    fun incompleteCastleScoring(): CastleScoring = castleScoring

    fun incompleteRoadScoring(): RoadScoring = roadScoring
}
