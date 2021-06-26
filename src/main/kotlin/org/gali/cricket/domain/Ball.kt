package org.gali.cricket.domain

sealed class Ball {
    abstract fun totalRun(): Int
    abstract fun playerScoredRun(): Int
    abstract fun isLegal(): Boolean
    abstract fun hasWicket(): Boolean
}

data class NoWicketBall(val run: Int) : Ball() {
    override fun totalRun(): Int = run
    override fun playerScoredRun(): Int = run
    override fun isLegal(): Boolean = true
    override fun hasWicket(): Boolean = false
}

data class WideBall(val extraRun: Int = 0, val wicket: Wicket? = null) : Ball() {
    override fun totalRun(): Int = extraRun + 1
    override fun playerScoredRun(): Int = 0
    override fun isLegal(): Boolean = false
    override fun hasWicket(): Boolean = wicket != null
}

data class WicketBall(val runs: Int = 0, val wicket: Wicket) : Ball() {
    override fun totalRun(): Int = runs
    override fun playerScoredRun(): Int = 0
    override fun isLegal(): Boolean = true
    override fun hasWicket(): Boolean = true
}
