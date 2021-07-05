package org.gali.cricket.domain

data class Over(val number: Int, val balls: List<Ball>) {
    fun isCompleted(): Boolean = balls.filter { it.isLegal() }.count() == 6
    fun totalRuns(): Int = balls.sumOf { it.totalRun() }
    fun totalWickets(): Int = balls.filter { it.hasWicket() }.count()
    fun numberOfLegalBalls(): Int = balls.filter { it.isLegal() }.count()
}

class Overs(private val size: Int) {
    private val overs = mutableListOf(Over(number = 0, balls = listOf()))

    fun addBall(ball: Ball): Over {
        val currentOver = currentOver()

        return if (currentOver.isCompleted()) {
            Over(currentOver.number + 1, listOf(ball)).apply { overs.add(this) }
        } else {
            currentOver.copy(balls = currentOver.balls + ball).apply { overs[number] = this }
        }
    }

    fun isCompleted(): Boolean = overs.size == size && currentOver().isCompleted()

    fun currentOverNumber(): Int = currentOver().number

    fun currentOverBalls(): Int = currentOver().numberOfLegalBalls()

    fun totalRuns(): Int = overs.sumOf { it.totalRuns() }

    fun totalWicket(): Int = overs.sumOf { it.totalWickets() }

    private fun currentOver() = overs.last()
}