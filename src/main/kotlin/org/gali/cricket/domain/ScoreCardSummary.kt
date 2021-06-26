package org.gali.cricket.domain

data class ScoreCardSummary(
    val teamScore: TeamScore,
    val strikerScore: PlayerScore,
    val nonStrikerScore: PlayerScore,
    val bowlerScore: BowlerScore
)

data class TeamScore(val run: Int, val wickets: Int, val overNumber: Int, val ballNumber: Int)

data class Over(val number: Int, val balls: List<Ball>) {
    fun isCompleted(): Boolean = balls.filter { it.isLegal() }.count() == 6
    fun totalRuns(): Int = balls.sumBy { it.totalRun() }
    fun totalWickets(): Int = balls.filter { it.hasWicket() }.count()
    fun numberOfLegalBalls(): Int = balls.filter { it.isLegal() }.count()
}