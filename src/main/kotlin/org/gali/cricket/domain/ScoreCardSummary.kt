package org.gali.cricket.domain

data class ScoreCardSummary(
    val teamScore: TeamScore,
    val strikerScore: BatsmanScore,
    val nonStrikerScore: BatsmanScore,
    val bowlerScore: BowlerScore,
    val target: Int?
)