package org.gali.cricket.domain

class Game(private val teams: Pair<Team, Team>, private val maxOver: Int) {
    private val innings = listOf(Inning(maxOver), Inning(maxOver))

    fun registerBall(ball: Ball): Over {
        return Over(number = 0, balls = listOf(ball))
    }
}

/**
 * Inning (maxOver, ballingTeamId, battingTeamId, bowlersId, battmensId)
 *
 *
 *   Over(number = 0, zerothBowlerId)
 *
 *
 *
 * */