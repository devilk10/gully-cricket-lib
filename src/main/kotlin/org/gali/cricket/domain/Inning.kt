package org.gali.cricket.domain

class Inning(private val maxOver: Int) {
    private val overs = mutableListOf(Over(number = 0, balls = listOf()))

    fun registerBall(ball: Ball): Over {
        val currentOver = currentOver()

        return if (currentOver.isCompleted()) {
            Over(currentOver.number + 1, listOf(ball)).apply { overs.add(this) }
        } else {
            currentOver.copy(balls = currentOver.balls + ball).apply { overs[number] = this }
        }
    }


    fun scoreCard(): ScoreCard {
        return ScoreCard(
            score = Score(
                run = overs.sumBy { it.totalRuns() },
                wickets = overs.sumBy { it.totalWickets() },
                overNumber = currentOver().number,
                ballNumber = currentOver().numberOfLegalBalls()
            )
        )
    }

    fun isCompleted(): Boolean = overs.size == maxOver && currentOver().isCompleted()

    private fun currentOver() = overs.last()
}
