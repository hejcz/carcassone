package io.github.hejcz.core

import io.github.hejcz.helpers.*
import io.github.hejcz.inn.tiles.*

interface CastleScoring {
    fun score(state: State, castle: Castle): Int
}

// Core game
object CompletedCastleScoring : CastleScoring {
    override fun score(state: State, castle: Castle): Int = 2 * castle.countEmblemsAndTiles()
}

// Core game
object IncompleteCastleScoring : CastleScoring {
    override fun score(state: State, castle: Castle): Int = castle.countEmblemsAndTiles()
}

// Inn extension
object IncompleteCastleWithCathedralScoring : CastleScoring {
    override fun score(state: State, castle: Castle): Int =
        if (castle.hasCathedral(state)) 0 else castle.countEmblemsAndTiles()
}

// Inn extension
object CompletedCastleWithCathedralScoring : CastleScoring {
    override fun score(state: State, castle: Castle): Int =
        castle.countEmblemsAndTiles() * if (castle.hasCathedral(state)) 3 else 2
}

// Inn extension
private fun Castle.hasCathedral(state: State) =
    this.pieces.asSequence()
        .map { state.tileAt(it.position) }
        .any { it is InnTile && it.hasCathedral() }
