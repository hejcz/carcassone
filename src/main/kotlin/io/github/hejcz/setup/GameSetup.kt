package io.github.hejcz.setup

import io.github.hejcz.api.CastleScoring
import io.github.hejcz.api.GreenFieldScoring
import io.github.hejcz.api.RoadScoring
import io.github.hejcz.api.StateExtension

open class GameSetup(private vararg val extensions: Extension) {
    open fun tiles() = TilesSetup().withExtensions(*extensions).remainingTiles()

    fun validators() = ValidatorsSetup().withExtensions(*extensions).validators()

    fun pieces() = PiecesSetup().withExtensions(*extensions).pieces()

    fun rules(roadScoring: RoadScoring, castleScoring: CastleScoring, greenFieldScoring: GreenFieldScoring) =
        ScoringSetup(castleScoring, roadScoring, greenFieldScoring).withExtensions(*extensions).rules()

    fun endRules(roadScoring: RoadScoring, castleScoring: CastleScoring, greenFieldScoring: GreenFieldScoring) =
        ScoringSetup(castleScoring, roadScoring, greenFieldScoring).withExtensions(*extensions).endRules()

    fun handlers() = CommandHandlersSetup().withExtensions(*extensions).handlers()

    fun stateExtensions(): Set<StateExtension> = StateExtensionSetup().withExtensions(*extensions).states()
}
