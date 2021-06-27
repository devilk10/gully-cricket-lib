package org.gali.cricket.domain

import kotlin.math.max

class Inning(private val maxOver: Int, battingTeamPlayers: List<Int>, bowlingTeamPlayers: List<Int>) {

    private val overs = mutableListOf(Over(number = 0, balls = listOf()))
    private val batsmanScore = battingTeamPlayers.map { BatsmanScore(it, 0, 0) }.toMutableList()
    private val bowlingScore = bowlingTeamPlayers.map { BowlerScore(it, 0, 0, 0) }.toMutableList()

    private var onStrikePlayerIndex = 0
    private var onNonStrikePlayerIndex = 1
    private var bowlerIndex = 0

    fun registerBall(ball: Ball): Over {
        updateScoreOnStrikeBatsman(ball)
        updateBowlerScore(ball)
        updateStrike(ball)
        val currentOver = currentOver()

        return if (currentOver.isCompleted()) {
            Over(currentOver.number + 1, listOf(ball)).apply { overs.add(this) }
        } else {
            currentOver.copy(balls = currentOver.balls + ball).apply { overs[number] = this }
        }.also {
            if (it.isCompleted()) {
                changeStrike()
                updateBowler()
            }
        }
    }

    private fun updateBowlerScore(ball: Ball) {
        val bowlerScore = bowlingScore[bowlerIndex]

        bowlingScore[bowlerIndex] = bowlerScore.copy(
            runs = bowlerScore.runs + ball.totalRun(),
            legalBalls = bowlerScore.legalBalls + if (ball.isLegal()) 1 else 0,
            wickets = bowlerScore.wickets + if (ball.hasWicket()) 1 else 0
        )
    }

    private fun updateBowler() {
        bowlerIndex += 1

        if (bowlerIndex == bowlingScore.size) {
            bowlerIndex = 0
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
            bowlerScore = bowlingScore[bowlerIndex]
        )
    }

    fun isCompleted(): Boolean = overs.size == maxOver && currentOver().isCompleted()

    private fun currentOver() = overs.last()

    private fun updateScoreOnStrikeBatsman(ball: Ball) {
        val score = batsmanScore[onStrikePlayerIndex]
        val updatedScore = score.copy(run = score.run + ball.playerScoredRun(), balls = score.balls + 1)
        batsmanScore[onStrikePlayerIndex] = updatedScore
    }

    private fun updateStrike(ball: Ball) {
        if (ball.hasWicket()) {
            onStrikePlayerIndex = max(onStrikePlayerIndex, onNonStrikePlayerIndex) + 1
        }
        if (ball.run % 2 != 0) changeStrike()
    }

    private fun changeStrike() {
        val swapped = Pair(onStrikePlayerIndex, onNonStrikePlayerIndex)
        onStrikePlayerIndex = swapped.second
        onNonStrikePlayerIndex = swapped.first
    }
}