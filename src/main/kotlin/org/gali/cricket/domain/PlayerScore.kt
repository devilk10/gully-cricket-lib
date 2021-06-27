package org.gali.cricket.domain

sealed class PlayerScore {
    abstract val id: Int
}

data class BatsmanScore(override val id: Int, val run: Int, val balls: Int) : PlayerScore()
data class BowlerScore(override val id: Int, val runs: Int, val legalBalls: Int, val wickets: Int) : PlayerScore()