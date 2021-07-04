package org.gali.cricket.domain

data class ScoreCardSummary(
    val teamScore: TeamScore,
    val strikerScore: PlayerScore,
    val nonStrikerScore: PlayerScore,
    val bowlerScore: BowlerScore
)