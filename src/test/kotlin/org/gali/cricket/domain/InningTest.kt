package org.gali.cricket.domain

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InningTest {

    @Test
    fun `should complete inning when last over is completed`() {
        val inning = Inning(2, listOf(0, 1), listOf(12))
        val noWicketBall = NoWicketBall(1)

        repeat(12) {
            inning.registerBall(noWicketBall)
        }

        assertTrue { inning.isCompleted() }
    }

    @Test
    fun `should give score for zero balls`() {
        val inning = Inning(2, listOf(0, 1), listOf(12))
        val actualScoreCard = inning.scoreCard()

        val expectedTeamScore = TeamScore(
            run = 0,
            wickets = 0,
            overNumber = 0,
            ballNumber = 0,
            inningState = InningState.IN_PROGRESS,
        )

        assertEquals(expectedTeamScore, actualScoreCard.teamScore)
    }

    @Test
    fun `should gives score for 1 ball`() {
        val inning = Inning(2, listOf(0, 1), listOf(12))
        val ball = NoWicketBall(4)
        inning.registerBall(ball)
        val actualScoreCard = inning.scoreCard()
        val expectedTeamScore = TeamScore(
            run = 4,
            wickets = 0,
            overNumber = 0,
            ballNumber = 1,
            inningState = InningState.IN_PROGRESS,
        )

        assertEquals(expectedTeamScore, actualScoreCard.teamScore)
    }

    @Test
    fun `should give score for 1 over and 1 ball`() {
        val inning = Inning(2, listOf(0, 1), listOf(12))
        val ball = NoWicketBall(4)
        val wideBall = WideBall()

        repeat(7) {
            inning.registerBall(ball)
            inning.registerBall(wideBall)
        }
        val actualScoreCard = inning.scoreCard()
        val expectedTeamScore = TeamScore(
            run = 35,
            wickets = 0,
            overNumber = 1,
            ballNumber = 1,
            inningState = InningState.IN_PROGRESS,
        )

        assertEquals(expectedTeamScore, actualScoreCard.teamScore)
    }

    @Test
    fun `should update score when wicket is fallen`() {
        val inning = Inning(2, listOf(0, 1, 2), listOf(12))
        val ball = WicketBall(wicket = Bowled(playerId = 0), run = 0)
        inning.registerBall(ball)

        val actualScoreCard = inning.scoreCard()
        val expectedScoreCard = TeamScore(
            run = 0,
            wickets = 1,
            overNumber = 0,
            ballNumber = 1,
            inningState = InningState.IN_PROGRESS,
        )

        assertEquals(expectedScoreCard, actualScoreCard.teamScore)
    }

    @Test
    fun `should update batsman score card`() {
        val inning = Inning(2, listOf(0, 1, 2), listOf(12))
        val ball = NoWicketBall(4)
        inning.registerBall(ball)

        val actualScoreCard = inning.scoreCard()
        val expectedStrikerScore = BatsmanScore(0, 4, 1, BattingState.NOT_BATTED)
        val expectedNonStrikerScore = BatsmanScore(1, 0, 0, BattingState.NOT_BATTED)

        assertEquals(expectedStrikerScore, actualScoreCard.strikerScore)
        assertEquals(expectedNonStrikerScore, actualScoreCard.nonStrikerScore)
    }

    @Test
    fun `should update non strikers score card`() {
        val inning = Inning(2, listOf(0, 1, 2), listOf(12))
        inning.registerBall(NoWicketBall(2))
        inning.registerBall(NoWicketBall(3))
        inning.registerBall(NoWicketBall(3))
        inning.registerBall(NoWicketBall(2))

        val actualScoreCard = inning.scoreCard()
        val expectedStrikerScore = BatsmanScore(0, 7, 3, BattingState.NOT_BATTED)
        val expectedNonStrikerScore = BatsmanScore(1, 3, 1, BattingState.NOT_BATTED)

        assertEquals(expectedNonStrikerScore, actualScoreCard.nonStrikerScore)
        assertEquals(expectedStrikerScore, actualScoreCard.strikerScore)
    }

    @Test
    fun `should update bowler score card`() {
        val inning = Inning(2, listOf(0, 1, 2), listOf(12))
        inning.registerBall(NoWicketBall(2))
        inning.registerBall(NoWicketBall(3))
        inning.registerBall(NoWicketBall(3))
        inning.registerBall(WideBall(0))
        inning.registerBall(NoWicketBall(2))
        inning.registerBall(WicketBall(0, Bowled(0)))

        val actualScoreCard = inning.scoreCard()
        val expectedBowlerScoreCard = BowlerScore(12, 11, 5, 1)

        assertEquals(expectedBowlerScoreCard, actualScoreCard.bowlerScore)
    }

    @Test
    fun `should update batsman on wicket`() {
        val inning = Inning(6, listOf(1, 2, 3), listOf(12, 13, 14))

        inning.registerBall(WicketBall(0, Bowled(playerId = 1)))

        val scoreCard = inning.scoreCard()

        assertEquals(BatsmanScore(id = 1, runs = 0, balls = 1, battingState = BattingState.OUT), scoreCard.strikerScore)
        assertEquals(
            BatsmanScore(id = 2, runs = 0, balls = 0, battingState = BattingState.NOT_BATTED),
            scoreCard.nonStrikerScore
        )
    }

    @Test
    fun `should set new batsman when striker gets out`() {
        val inning = Inning(6, listOf(0, 1, 2), listOf(12, 13))
        inning.registerBall(WicketBall(0, Bowled(0)))

        inning.setBatsman(2)
        val scoreCard = inning.scoreCard()

        assertEquals(BatsmanScore(1, 0, 0, BattingState.NOT_BATTED), scoreCard.nonStrikerScore)
        assertEquals(BatsmanScore(2, 0, 0, BattingState.BATTING), scoreCard.strikerScore)
    }

    @Test
    fun `should set new batsman when non striker gets out`() {
        val inning = Inning(6, listOf(0, 1, 2), listOf(12, 13))
        inning.registerBall(WicketBall(1, RunOut(1, 12)))

        inning.setBatsman(2)
        val scoreCard = inning.scoreCard()

        assertEquals(BatsmanScore(0, 1, 1, BattingState.NOT_BATTED), scoreCard.nonStrikerScore)
        assertEquals(BatsmanScore(2, 0, 0, BattingState.BATTING), scoreCard.strikerScore)
    }

    @Test
    fun `should set new bowler when over is finished`() {
        val inning = Inning(6, listOf(0, 1, 2), listOf(12, 13, 14))
        repeat(6) {
            inning.registerBall(NoWicketBall(1))
        }
        inning.setBowler(14)
        repeat(3) {
            inning.registerBall(NoWicketBall(1))
        }
        val scoreCard = inning.scoreCard()

        assertEquals(BowlerScore(14, 3, 3, 0), scoreCard.bowlerScore)
    }

    @Test
    fun `should mark innings completed when last over is completed`() {
        val inning = Inning(1, listOf(0, 1, 2), listOf(12, 13, 14))
        repeat(6) {
            inning.registerBall(NoWicketBall(1))
        }
        val teamScore = inning.scoreCard().teamScore
        assertEquals(
            teamScore,
            TeamScore(run = 6, wickets = 0, overNumber = 0, ballNumber = 6, inningState = InningState.Completed)
        )
    }
}