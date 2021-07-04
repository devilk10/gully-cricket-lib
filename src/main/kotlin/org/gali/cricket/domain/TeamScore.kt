package org.gali.cricket.domain

data class TeamScore(
    val run: Int,
    val wickets: Int,
    val overNumber: Int,
    val ballNumber: Int,
    val inningState: InningState
)