package org.gali.cricket.domain

data class Over(val number: Int, val balls: List<Ball>) {
    fun isCompleted(): Boolean = balls.filter { it.isLegal() }.count() == 6
    fun totalRuns(): Int = balls.sumOf { it.totalRun() }
    fun totalWickets(): Int = balls.filter { it.hasWicket() }.count()
    fun numberOfLegalBalls(): Int = balls.filter { it.isLegal() }.count()
}