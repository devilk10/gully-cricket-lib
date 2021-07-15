package org.gali.cricket.domain

sealed class PlayerScore {
    abstract val id: Int
}

data class BatsmanScore(
    override val id: Int,
    val runs: Int,
    val balls: Int,
    var battingState: BattingState,
    val noOfFours: Int,
    val noOfSixes: Int
) : PlayerScore()
data class BowlerScore(override val id: Int, val runs: Int, val legalBalls: Int, val wickets: Int) : PlayerScore()

enum class BattingState {
    NOT_BATTED, BATTING, OUT
}