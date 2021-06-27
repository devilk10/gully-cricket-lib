package org.gali.cricket.domain

class Inning(private val maxOver: Int, private val battingTeamPlayers: List<Int>) {

    private val overs = mutableListOf(Over(number = 0, balls = listOf()))
    private val batsmanScore = battingTeamPlayers.map { BatsmanScore(it, 0) }.toMutableList()
    private var onStrikePlayerIndex = 0
    private var onNonStrikePlayerIndex = 1

    fun registerBall(ball: Ball): Over {
        updateScoreOnStrikeBatsman(ball)
        updateStrike(ball)
        val currentOver = currentOver()

        return if (currentOver.isCompleted()) {
            Over(currentOver.number + 1, listOf(ball)).apply { overs.add(this) }
        } else {
            currentOver.copy(balls = currentOver.balls + ball).apply { overs[number] = this }
        }.also {
            if (it.isCompleted()) {
                changeStrike()
            }
        }
    }


    fun scoreCard(): ScoreCardSummary {
        return ScoreCardSummary(
            teamScore = TeamScore(
                run = overs.sumOf { it.totalRuns() },
                wickets = overs.sumOf { it.totalWickets() },
                overNumber = currentOver().number,
                ballNumber = currentOver().numberOfLegalBalls()
            ),
            strikerScore = batsmanScore[onStrikePlayerIndex],
            nonStrikerScore = batsmanScore[onNonStrikePlayerIndex],
            bowlerScore = BowlerScore(id = 0, runs = 0, legalBalls = 0, wickets = 0),
        )
    }

    fun isCompleted(): Boolean = overs.size == maxOver && currentOver().isCompleted()

    private fun currentOver() = overs.last()

    private fun updateScoreOnStrikeBatsman(ball: Ball) {
        val score = batsmanScore[onStrikePlayerIndex]
        val updatedScore = score.copy(run = score.run + ball.playerScoredRun())
        batsmanScore[onStrikePlayerIndex] = updatedScore
    }

    private fun updateStrike(ball: Ball) {
        if (ball.run % 2 != 0) changeStrike()
    }

    private fun changeStrike() {
        val swapped = Pair(onStrikePlayerIndex, onNonStrikePlayerIndex)
        onStrikePlayerIndex = swapped.second
        onNonStrikePlayerIndex = swapped.first
    }
}