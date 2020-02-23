package io.github.hejcz.api

interface GreenField {
    val parts: Set<PositionedLocation>
    fun completedCastles(): Int
}
