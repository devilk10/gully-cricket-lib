package org.gali.cricket.domain

class Match(private val teams: Pair<Team, Team>, private val maxOvers: Int) {
    private var currentInning: Inning =
        Inning(maxOvers, teams.first.players.map { it.id }, teams.second.players.map { it.id }, null)

    fun scoreCard() = currentInning.scoreCard()

    fun registerBall(ball: Ball) {
        currentInning.registerBall(ball)
    }

    fun startSecondInning() {
        if (currentInning.isCompleted())
            currentInning = Inning(
                maxOvers,
                teams.second.players.map { it.id },
                teams.first.players.map { it.id },
                currentInning.scoreCard().teamScore.run + 1
            )
    }

    fun startNewOver(bowler: Player) {
        currentInning.setBowler(bowler.id)
    }

    fun setNewBatsman(batsman: Player) {
        currentInning.setBatsman(batsman.id)
    }
}