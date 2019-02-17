package io.github.hejcz.placement

sealed class Side {
    abstract fun opposite(): Side
}

object LeftSide : Side() {
    override fun opposite(): Side = RightSide
}

object RightSide : Side() {
    override fun opposite(): Side = LeftSide
}