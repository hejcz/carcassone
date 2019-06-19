package io.github.hejcz.helper

import io.github.hejcz.abbot.AbbotExtension
import io.github.hejcz.core.RemainingTiles
import io.github.hejcz.corncircles.*
import io.github.hejcz.inn.InnAndCathedralsExtension
import io.github.hejcz.river.RiverExtension
import io.github.hejcz.setup.Extension
import io.github.hejcz.setup.GameSetup

open class TestGameSetup(
    private val remainingTiles: RemainingTiles,
    extensions: List<Extension>
) : GameSetup(*extensions.toTypedArray()) {
    constructor(remainingTiles: RemainingTiles) : this(remainingTiles, emptyList())
    override fun tiles(): RemainingTiles = remainingTiles
}

class RiverTestGameSetup(remainingTiles: RemainingTiles) :
    TestGameSetup(remainingTiles, listOf(RiverExtension))

class AbbotTestGameSetup(remainingTiles: RemainingTiles) :
    TestGameSetup(remainingTiles, listOf(AbbotExtension))

class InnAndCathedralsTestGameSetup(remainingTiles: RemainingTiles) :
    TestGameSetup(remainingTiles, listOf(InnAndCathedralsExtension))

class CornCirclesGameSetup(remainingTiles: RemainingTiles) :
    TestGameSetup(remainingTiles, listOf(CornCirclesExtension))
