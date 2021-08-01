package org.gali.cricket.domain

sealed class Run {
    abstract val run: Int
    abstract fun batsmanScoredRuns(): Int
}

class NormalRuns(override val run: Int) : Run() {
    override fun batsmanScoredRuns(): Int = run
}

class LegByes(override val run: Int) : Run() {
    override fun batsmanScoredRuns(): Int = 0
}

class Byes(override val run: Int) : Run() {
    override fun batsmanScoredRuns(): Int = 0
}