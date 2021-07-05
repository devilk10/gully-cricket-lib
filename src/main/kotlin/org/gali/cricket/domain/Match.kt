package org.gali.cricket.domain

class Match(private val teams: Pair<Team, Team>, private val maxOvers: Int) {
    private var currentInning: Inning =
        Inning(maxOvers, teams.first.players.map { it.id }, teams.second.players.map { it.id })

    fun scoreCard() = currentInning.scoreCard()

    fun registerBall(ball: Ball): Over {
        return currentInning.registerBall(ball)
    }

    fun startSecondInning() {
        if (currentInning.isCompleted())
            currentInning = Inning(maxOvers, teams.second.players.map { it.id }, teams.first.players.map { it.id })
    }
}