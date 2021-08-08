package org.gali.cricket.domain

class Inning(maxOver: Int, battingTeamPlayers: List<Int>, bowlingTeamPlayers: List<Int>, private val target: Int?) {

    private val overs = Overs(maxOver)
    private val batsmanScore =
        battingTeamPlayers.map { BatsmanScore(it, 0, 0, BattingState.NOT_BATTED, 0, 0) }.toMutableList()
    private val bowlingScore =
        bowlingTeamPlayers.map { BowlerScore(it, 0, 0, 0) }.toMutableList()

    private var onStrikePlayerIndex = 0
    private var onNonStrikePlayerIndex = 1
    private var bowlerIndex = 0

    init {
        batsmanScore[0].battingState = BattingState.BATTING
        batsmanScore[1].battingState = BattingState.BATTING
    }

    fun registerBall(ball: Ball) {
        updateScoreOnStrikeBatsman(ball)
        updateBowlerScore(ball)
        updateStrike(ball)
        overs.addBall(ball)
        if (overs.currentOverFinished()) {
            changeStrike()
        }
    }

    private fun updateBowlerScore(ball: Ball) {
        val bowlerScore = bowlingScore[bowlerIndex]

        bowlingScore[bowlerIndex] = bowlerScore.copy(
            runs = bowlerScore.runs + ball.runsWithPenalty(),
            legalBalls = bowlerScore.legalBalls + if (ball.isLegal()) 1 else 0,
            wickets = bowlerScore.wickets + if (ball.hasWicket()) 1 else 0
        )
    }


    fun scoreCard(): ScoreCardSummary {
        return ScoreCardSummary(
            teamScore = TeamScore(
                run = overs.totalRuns(),
                wickets = overs.totalWicket(),
                overNumber = overs.currentOverNumber(),
                ballNumber = overs.currentOverBalls(),
                inningState = if (inningsCompleted()) InningState.Completed else InningState.IN_PROGRESS
            ),
            strikerScore = batsmanScore[onStrikePlayerIndex],
            nonStrikerScore = batsmanScore[onNonStrikePlayerIndex],
            bowlerScore = bowlingScore[bowlerIndex],
            target = target
        )
    }

    private fun inningsCompleted(): Boolean {
        val isAllOut = overs.totalWicket() == this.batsmanScore.size - 1
        val isTargetAchieved = target?.let { it <= overs.totalRuns() } ?: false
        return overs.isCompleted() || isAllOut || isTargetAchieved
    }

    private fun updateScoreOnStrikeBatsman(ball: Ball) {
        val score: BatsmanScore = batsmanScore[onStrikePlayerIndex]
        val incFourBy = if (ball.isFour()) 1 else 0
        val incSixBy = if (ball.isSix()) 1 else 0
        val updatedScore = score.copy(
            runs = score.runs + ball.playerScoredRun(),
            noOfFours = score.noOfFours + incFourBy,
            noOfSixes = score.noOfSixes + incSixBy,
            balls = score.balls + 1
        )
        batsmanScore[onStrikePlayerIndex] = updatedScore
    }

    private fun updateStrike(ball: Ball) {
        if (ball.hasWicket()) {
            val wicket = ball.wicket!!
            if (batsmanScore[onStrikePlayerIndex].id == wicket.playerId) {
                batsmanScore[onStrikePlayerIndex].battingState = BattingState.OUT
            } else {
                batsmanScore[onNonStrikePlayerIndex].battingState = BattingState.OUT
            }
        }
        //Todo ball.run.run change it with meaningful name
        if (ball.run.run % 2 != 0) changeStrike()
    }

    private fun changeStrike() {
        val swapped = Pair(onStrikePlayerIndex, onNonStrikePlayerIndex)
        onStrikePlayerIndex = swapped.second
        onNonStrikePlayerIndex = swapped.first
    }

    fun setBatsman(newBatsman: Int) {
        batsmanScore[newBatsman].battingState = BattingState.BATTING
        if (batsmanScore[onStrikePlayerIndex].battingState == BattingState.OUT) onStrikePlayerIndex = newBatsman
        else onNonStrikePlayerIndex = newBatsman
    }

    fun setBowler(playerId: Int) {
        bowlerIndex = bowlingScore.indexOfFirst {
            it.id == playerId
        }
    }

    fun isCompleted(): Boolean = overs.isCompleted()

    fun addPlayer(player: Int) {
        batsmanScore.add(BatsmanScore(player, 0, 0, BattingState.NOT_BATTED, noOfFours = 0, noOfSixes = 0))
    }
}