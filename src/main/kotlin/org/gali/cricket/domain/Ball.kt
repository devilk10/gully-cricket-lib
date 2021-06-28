package org.gali.cricket.domain

sealed class Ball {
    abstract val run: Int
    open val wicket: Wicket? = null
    abstract fun totalRun(): Int
    abstract fun playerScoredRun(): Int
    abstract fun isLegal(): Boolean
    abstract fun hasWicket(): Boolean
}

data class NoWicketBall(override val run: Int) : Ball() {
    override fun totalRun(): Int = run
    override fun playerScoredRun(): Int = run
    override fun isLegal(): Boolean = true
    override fun hasWicket(): Boolean = false
}

data class WideBall(override val run: Int = 0, override val wicket: Wicket? = null) : Ball() {
    override fun totalRun(): Int = run + 1
    override fun playerScoredRun(): Int = 0
    override fun isLegal(): Boolean = false
    override fun hasWicket(): Boolean = wicket != null
}

data class WicketBall(override val run: Int = 0, override val wicket: Wicket) : Ball() {
    override fun totalRun(): Int = run
    override fun playerScoredRun(): Int = run
    override fun isLegal(): Boolean = true
    override fun hasWicket(): Boolean = true
}
