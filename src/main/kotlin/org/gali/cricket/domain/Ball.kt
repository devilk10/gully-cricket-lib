package org.gali.cricket.domain

sealed class Ball {
    abstract val run: Run
    open val wicket: Wicket? = null
    abstract fun runsWithPenalty(): Int
    abstract fun playerScoredRun(): Int
    abstract fun isLegal(): Boolean
    abstract fun hasWicket(): Boolean
    fun isFour(): Boolean = playerScoredRun() == 4
    fun isSix(): Boolean = playerScoredRun() == 6
}

data class NoWicketBall(override val run: Run) : Ball() {
    override fun runsWithPenalty(): Int = run.run
    override fun playerScoredRun(): Int = run.batsmanScoredRuns()
    override fun isLegal(): Boolean = true
    override fun hasWicket(): Boolean = false
}

data class WideBall(override val run: Run = Byes(0), override val wicket: Wicket? = null) : Ball() {
    override fun runsWithPenalty(): Int = run.run + 1
    override fun playerScoredRun(): Int = 0
    override fun isLegal(): Boolean = false
    override fun hasWicket(): Boolean = wicket != null
}

data class NoBall(override val run: Run, override val wicket: Wicket? = null) : Ball() {
    override fun runsWithPenalty(): Int = run.run + 1
    override fun playerScoredRun(): Int = run.batsmanScoredRuns()
    override fun isLegal(): Boolean = false
    override fun hasWicket(): Boolean = wicket != null
}

data class WicketBall(override val run: Run = NormalRuns(0), override val wicket: Wicket) : Ball() {
    override fun runsWithPenalty(): Int = run.run
    override fun playerScoredRun(): Int = run.batsmanScoredRuns()
    override fun isLegal(): Boolean = true
    override fun hasWicket(): Boolean = true
}