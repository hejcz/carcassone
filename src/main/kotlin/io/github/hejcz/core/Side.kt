package io.github.hejcz.core

/**
 * Represents side of the road that is required for valid green field discovery.
 */
sealed class Side {
    abstract fun opposite(): Side
}

object LeftSide : Side() {
    override fun opposite(): Side = RightSide
}

object RightSide : Side() {
    override fun opposite(): Side = LeftSide
}
